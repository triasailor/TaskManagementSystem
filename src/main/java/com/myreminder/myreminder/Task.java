package com.myreminder.myreminder;
import java.time.LocalDate;
import java.util.*;

public class Task {

    public String title, description, category, priority;
    public int ID,nextReminderId;

    public Status status;
    public LocalDate deadline ;

    public List<Reminder> reminderList = new ArrayList<>();

    public Task(int Id,String Title, String Description , String Category, String Priority, Status Status, LocalDate date, List<Reminder> reminderlist,int newReminderId)
    {
        this.ID = Id;
        this.title = Title;
        this.description = Description;
        this.category = Category;
        this.priority = Priority;
        this.status = Status;
        this.deadline = date;
        this.reminderList = reminderlist;
        this.nextReminderId = newReminderId;

    }

    public int getNextReminderId() {
        return nextReminderId;
    }

    public void setNextReminderId(int nextReminderId) {
        this.nextReminderId = nextReminderId;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public List<Reminder> getReminderList() {
        return reminderList;
    }

    public void setReminderList(List<Reminder> reminderList) {
        this.reminderList = reminderList;
    }
}
