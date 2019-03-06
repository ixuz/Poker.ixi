package web;

import org.apache.logging.log4j.Logger;
import org.iota.ict.utils.Constants;

import java.util.LinkedList;
import java.util.List;

/**
 * Framework for a thread that can be restarted. Allows to register sub-threads that will be automatically started and terminated.
 * */
public abstract class RestartableThread implements Restartable, Runnable {

    protected Logger logger;
    protected State state = new StateTerminated();
    protected Thread runningThread;
    protected List<Restartable> subWorkers = new LinkedList<>();

    protected RestartableThread(Logger Logger) {
        this.logger = Logger;
    }

    public void onStart() {
    }

    public void onTerminate() {
    }

    public void onStarted() {
    }

    public void onTerminated() {
    }

    @Override
    public synchronized void start() {
        state.start();
    }

    @Override
    public synchronized void terminate() {
        state.terminate();
    }

    protected class State implements Restartable {

        protected final String name;

        protected State(String name) {
            this.name = name;
        }

        public void start() {
            throwIllegalStateException("start");
        }

        public void terminate() {
            throwIllegalStateException("terminate");
        }

        protected void throwIllegalStateException(String actionName) {
            throw new IllegalStateException("Action '" + actionName + "' cannot be performed from state '" + name + "'.");
        }
    }

    protected class StateTerminated extends State {
        protected StateTerminated() {
            super("terminated");
        }

        @Override
        public void start() {
            if (!Constants.TESTING && logger != null)
                logger.debug("starting ...");
            state = new StateStarting();
            onStart();
            for (Restartable subWorker : subWorkers)
                subWorker.start();
            state = new StateRunning();
            runningThread = new Thread(RestartableThread.this, RestartableThread.this.getClass().getName());
            runningThread.start();
            onStarted();
            if (!Constants.TESTING && logger != null)
                logger.debug("started");
        }
    }

    protected class StateStarting extends State {
        protected StateStarting() {
            super("starting");
        }
    }

    protected class StateRunning extends State {
        protected StateRunning() {
            super("running");
        }

        @Override
        public void terminate() {
            if (!Constants.TESTING && logger != null)
                logger.debug("terminating ...");
            state = new StateTerminating();
            onTerminate();
            while (runningThread.isAlive())
                safeSleep(1);
            for (Restartable subWorker : subWorkers)
                subWorker.terminate();
            state = new StateTerminated();
            onTerminated();
            if (!Constants.TESTING && logger != null)
                logger.debug("terminated");
        }

        private void safeSleep(long ms) {
            try {
                Thread.sleep(ms);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected class StateTerminating extends State {
        protected StateTerminating() {
            super("terminate");
        }
    }

    public boolean isRunning() {
        return state instanceof StateRunning;
    }
}
