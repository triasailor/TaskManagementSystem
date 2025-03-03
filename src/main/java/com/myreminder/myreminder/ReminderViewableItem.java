package com.myreminder.myreminder;

import java.time.LocalDate;

public class ReminderViewableItem {

    int RemId,TaskId;
    public String taskTitle;
    public ReminderCat reminderCategory;
    public LocalDate reminderAlertDate, taskDeadline;

    public ReminderViewableItem(int remId,int taskId,String tasktitle,ReminderCat remCat,LocalDate remAlertDt,LocalDate taskDeadlne)
    {
       this.RemId = remId;
       this.TaskId = taskId;
       this.taskTitle = tasktitle;
       this.reminderCategory = remCat;
       this.reminderAlertDate = remAlertDt;
       this.taskDeadline = taskDeadlne;
    }

    public int getRemId() {
        return RemId;
    }

    public void setRemId(int remId) {
        RemId = remId;
    }

    public int getTaskId() {
        return TaskId;
    }

    public void setTaskId(int taskId) {
        TaskId = taskId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public ReminderCat getReminderCategory() {
        return reminderCategory;
    }

    public void setReminderCategory(ReminderCat reminderCategory) {
        this.reminderCategory = reminderCategory;
    }

    public LocalDate getReminderAlertDate() {
        return reminderAlertDate;
    }

    public void setReminderAlertDate(LocalDate reminderAlertDate) {
        this.reminderAlertDate = reminderAlertDate;
    }

    public LocalDate getTaskDeadline() {
        return taskDeadline;
    }

    public void setTaskDeadline(LocalDate taskDeadline) {
        this.taskDeadline = taskDeadline;
    }
}
