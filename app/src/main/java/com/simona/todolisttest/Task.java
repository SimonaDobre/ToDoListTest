package com.simona.todolisttest;

public class Task {

    private String task;
    private int priority;

    public Task(String task, int priority) {
        this.task = task;
        this.priority = priority;
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

    public void changePriority(int newPriority) {
        priority = newPriority;
    }

    public void addTask() {
        // Nu stiu cum pot adauga un task la array-ul de task-uri definit in MainActivity,
        // folosind aceasta metoda - addTask - din clasa Task.
        // Array-ul de task-uri e obligatoriu sa fie in MainActivity pentru ca acolo am nevoie de el
        // pentru a-l afisa in tasksListRecyclerView, pe masura ce adaug noi task-uri,
        // sau pentru a edita sau a sterge anumite task-uri.
    }


}
