package xyz.oogiya.parlaimages.tasks;

import xyz.oogiya.parlaimages.util.Utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class BackgroundExecutor {

    private ExecutorService executorService;
    private LinkedList<Task> taskQueue = new LinkedList<>();

    private boolean isClosed = false;

    private final int MAX_TASKS = Utils.MAX_TASKS;

    public BackgroundExecutor() {
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.taskQueue = new LinkedList<Task>();
    }

    public int getTasksRemaining() { return this.taskQueue.size(); }

    public boolean isFull() { return (this.taskQueue.size() > this.MAX_TASKS) ? true : false; }

    public boolean processTaskQueue() {
        boolean processed = false;
        Task task = this.taskQueue.poll();
        if (task != null) {
            if (task.isDone()) {
                task.printException();
                task.onComplete();

                processed = true;
            } else {
                this.taskQueue.push(task);
            }
        }
        return !processed;
    }

    public boolean processRemainingTasks(int attempts, int delay) {
        while (this.taskQueue.size() > 0 && attempts > 0) {
            if (this.processTaskQueue()) {
                try {
                    Thread.sleep(delay);
                } catch (Exception ex) {
                    ex.printStackTrace();
                } attempts--;
            }
        }
        return (attempts <= 0);
    }

    private void taskLeftPerType() {
        HashMap<String, Object> tasksLeft = new HashMap<>();
        for (Task task : this.taskQueue) {
            String className = task.getClass().toString();
            if (tasksLeft.containsKey(className)) {
                tasksLeft.put(className, ((Integer)tasksLeft.get(className)) + 1);
            } else {
                tasksLeft.put(className, 1);
            }
        }
    }

    public boolean close() {
        boolean error = true;
        try {
            this.taskLeftPerType();
            this.executorService.shutdown();
            this.processRemainingTasks(50, 5);
            error = !this.executorService.awaitTermination(10L, TimeUnit.SECONDS);
            error = false;
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } this.isClosed = true;
        return error;
    }

    public boolean addTask(Task task) {
        if (this.getTasksRemaining() > MAX_TASKS) {
            return false;
        }
        if (!task.checkForDuplicate()) {
            Future<?> future = this.executorService.submit(task);
            task.setFuture(future);
            this.taskQueue.add(task);
            return true;
        }
        return isClosed;

    }

}
