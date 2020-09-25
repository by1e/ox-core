package cn.by1e.ox.core.util.chainexecutors;

import cn.by1e.ox.core.internal.VoidInvoker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * @author bangquan.qian
 * @date 2020-08-13 14:44
 */
public class ChainedExecutors {

    private static final int MAX_TIMEOUT_SECONDS = 5;

    private static final int POOL_SIZE = 16;

    private static final ExecutorService POOL = Executors.newFixedThreadPool(POOL_SIZE);

    private enum InvokeMode {
        SYNC(0),
        ASYNC(1),
        ;

        private int mode;

        InvokeMode(int mode) {
            this.mode = mode;
        }
    }

    private enum InvokeJobMode {
        AHEAD(0),
        BODY(1),
        CATCH(2),
        FINAL(3),
        ;

        private int mode;

        InvokeJobMode(int mode) {
            this.mode = mode;
        }
    }

    private static class InvokerJob {
        private VoidInvoker invoker = null;

        private Consumer<Throwable> consumer = null;

        private int mode = InvokeMode.SYNC.mode;

        private int index = 0;

        private int type = InvokeJobMode.BODY.mode;
    }

    public enum ResultMode {
        SUCCESS(0),
        BIZ_ERR(1),
        OTR_ERR(2),
        ;

        private int mode;

        ResultMode(int mode) {
            this.mode = mode;
        }
    }

    public static class ResultItem {
        private int res = ResultMode.SUCCESS.mode;

        private Throwable e = null;

        /**
         * @see InvokerJob#index
         */
        private int index = 0;

        /**
         * @see InvokerJob#type
         */
        private int type;

        /**
         * @see InvokerJob#mode
         */
        private int mode;
    }

    public static class Result {
        private boolean success = true;

        private RuntimeException exception = null;

        private List<ResultItem> details = null;
    }

    public static class Executor {

        private final List<InvokerJob> bodyInvokerJobs = new ArrayList<>();

        private InvokerJob aheadInvokeJob;

        private InvokerJob onCatchInvokeJob;

        private InvokerJob onFinalInvokerJob;

        private InvokerJob newJob(Consumer<Throwable> consumer, int mode, int type) {
            return newJob(consumer, null, mode, type);
        }

        private InvokerJob newJob(VoidInvoker invoker, int mode, int type) {
            return newJob(null, invoker, mode, type);
        }

        private InvokerJob newJob(Consumer<Throwable> consumer, VoidInvoker invoker, int mode, int type) {
            if (invoker == null && consumer == null) {
                return null;
            }
            InvokerJob job = new InvokerJob();
            job.invoker = invoker;
            job.consumer = consumer;
            job.mode = mode;
            job.type = type;
            return job;
        }

        private Executor then(VoidInvoker invoker, int mode) {
            if (invoker != null) {
                InvokerJob job = newJob(invoker, mode, InvokeJobMode.BODY.mode);
                bodyInvokerJobs.add(job);
                job.index = bodyInvokerJobs.indexOf(job);
            }
            return this;
        }

        public Executor ahead(VoidInvoker invoker) {
            this.aheadInvokeJob = newJob(invoker, InvokeMode.SYNC.mode, InvokeJobMode.AHEAD.mode);
            return this;
        }

        public Executor then(VoidInvoker invoker) {
            return then(invoker, InvokeMode.SYNC.mode);
        }

        public Executor asyncThen(VoidInvoker invoker) {
            return then(invoker, InvokeMode.ASYNC.mode);
        }

        public Executor onCatch(Consumer<Throwable> consumer) {
            this.onCatchInvokeJob = newJob(consumer, InvokeMode.SYNC.mode, InvokeJobMode.CATCH.mode);
            return this;
        }

        public Executor onFinal(VoidInvoker invoker) {
            this.onFinalInvokerJob = newJob(invoker, InvokeMode.SYNC.mode, InvokeJobMode.FINAL.mode);
            return this;
        }

        public Future<Result> asyncExecute() {
            return POOL.submit(this::execute);
        }

        public boolean executeFailFast() {
            Result result = execute();
            if (result.exception != null) {
                throw result.exception;
            }
            if (!result.success) {
                throw new RuntimeException("execute failure");
            }
            return result.success;
        }

        public Result execute() {
            return doExecute();
        }

        private Result doExecute() {
            boolean success = true;
            RuntimeException exception = null;
            List<ResultItem> details = new CopyOnWriteArrayList<>();
            try {
                List<Callable<ResultItem>> callables = new ArrayList<>();
                try {
                    try {
                        try {
                            invokeJob(details, callables, aheadInvokeJob);
                            for (InvokerJob job : bodyInvokerJobs) {
                                invokeJob(details, callables, job);
                            }
                        } finally {
                            invokeAsyncJob(details, callables);
                        }
                    } catch (Throwable e) {
                        success = false;
                        invokeJob(details, callables, onCatchInvokeJob, e);
                    } finally {
                        invokeJob(details, callables, onFinalInvokerJob);
                    }
                } finally {
                    invokeAsyncJob(details, callables);
                }
            } catch (RuntimeException e) {
                exception = e;
            } catch (Throwable e) {
                exception = new RuntimeException(e);
            }
            if (exception != null) {
                success = false;
            }
            Result result = new Result();
            result.success = success;
            result.details = new ArrayList<>(details);
            result.exception = exception;
            return result;
        }

        private void invokeAsyncJob(List<ResultItem> details, List<Callable<ResultItem>> callables) {
            List<Future<ResultItem>> futures = new ArrayList<>();
            for (int idx = 0; idx < callables.size(); idx++) {
                futures.add(POOL.submit(callables.get(idx)));
                callables.remove(idx--);
            }
            waitFutureDone(details, futures);
        }

        private void waitFutureDone(List<ResultItem> details, List<Future<ResultItem>> futures) {
            for (Future<ResultItem> future : futures) {
                try {
                    future.get(MAX_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                } catch (Throwable e) {
                    ResultItem item = new ResultItem();
                    item.res = ResultMode.OTR_ERR.mode;
                    item.e = e;
                    details.add(item);
                    // throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e);
                }
            }
        }

        private void invokeJob(List<ResultItem> details, List<Callable<ResultItem>> callables, InvokerJob job, Throwable e) {
            if (job == null) {
                return;
            }
            int mode = job.mode;
            if (InvokeMode.SYNC.mode == mode) {
                invokeInvoker(details, job, e);
            } else if (InvokeMode.ASYNC.mode == mode) {
                callables.add(() -> invokeInvoker(details, job, e));
            }
        }

        private void invokeJob(List<ResultItem> details, List<Callable<ResultItem>> callables, InvokerJob job) {
            invokeJob(details, callables, job, null);
        }

        private ResultItem invokeInvoker(List<ResultItem> details, InvokerJob job, Throwable e) {
            ResultItem item = new ResultItem();
            item.res = ResultMode.SUCCESS.mode;
            item.index = job.index;
            item.mode = job.mode;
            item.type = job.type;
            details.add(item);
            try {
                if (e == null) {
                    job.invoker.invoke();
                } else if (job.consumer != null) {
                    job.consumer.accept(e);
                }
            } catch (Throwable t) {
                item.res = ResultMode.BIZ_ERR.mode;
                item.e = t;
                throw t instanceof RuntimeException ? (RuntimeException) t : new RuntimeException(t);
            }
            return item;
        }

    }

    public static Executor newExecutor() {
        return new Executor();
    }

}
