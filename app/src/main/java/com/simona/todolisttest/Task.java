package com.simona.todolisttest;

public class Task {

    private String task;
    private int priority;
    private String deadLine;
    boolean accomplished;

    public Task(String task, int priority, String deadLine, boolean accomplished) {
        this.task = task;
        this.priority = priority;
        this.deadLine = deadLine;
        this.accomplished = accomplished;
    }

    public String getTask() {
        return task;
    }

    public int getPriority() {
        return priority;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }

    public boolean isAccomplished() {
        return accomplished;
    }

    public void setAccomplished(boolean accomplished) {
        this.accomplished = accomplished;
    }

    public void changeAccomplishment(){
        accomplished = !accomplished;
    }

}
