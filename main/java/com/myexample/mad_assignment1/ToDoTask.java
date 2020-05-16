package com.myexample.mad_assignment1;

public class ToDoTask {
    private int taskId;
    private String taskName;
    private String tLocation;
    private int isCompleted = 0;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String gettLocation() {
        return tLocation;
    }

    public void settLocation(String tLocation) {
        this.tLocation = tLocation;
    }


    public int getIsCompleted() {
        return isCompleted;
    }

    public void setCompleted(int completed) {
        isCompleted = completed;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
