package com.simona.todolisttest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task implements Comparable {

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

    @Override
    public int compareTo(Object o) {
        Task t = (Task) o;
        SimpleDateFormat transformStringToData = new SimpleDateFormat("dd-MM-yyyy");
        Date d1 = null, d2 = null;
        try {
            d1 = transformStringToData.parse(deadLine);
            d2 = transformStringToData.parse(t.getDeadLine());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d1.compareTo(d2);
    }
}
