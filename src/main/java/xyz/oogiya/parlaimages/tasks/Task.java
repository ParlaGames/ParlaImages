package xyz.oogiya.parlaimages.tasks;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public abstract class Task implements Runnable {

    private Future<?> future = null;

    @Override
    public abstract void run();

    public abstract void onComplete();

    public abstract boolean checkForDuplicate();

    public final void printException() {
        if (this.future != null) {
            try {
                this.future.get();
            } catch (ExecutionException ex) {
                Throwable rootException = ex.getCause();
                rootException.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public final boolean isDone() {
        return (this.future != null) ? this.future.isDone() : false;
    }

    public final void setFuture(Future<?> future) {
        this.future = future;
    }

    public final Future<?> getFuture() {
        return this.future;
    }

}
