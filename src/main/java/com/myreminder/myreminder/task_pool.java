package com.myreminder.myreminder;
import java.io.FileReader ;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

/**
 * The {@code task_pool} class provides file read , file write of our data, as well as
 * some supplementary utility functions that help us initialize the upper pane of the
 * graphical layout.
 *
 * @author Triantafyllos Papoutsis
 * @version 1.0
 * @since 2025-02-27
 */
public class task_pool {

    public List<Task> taskList = new ArrayList<>();
    public List<Category> categoryList = new ArrayList<>();

    public List<Priority> priorityList = new ArrayList<>();

    public List<ReminderViewableItem> reminderViewableItemList = new ArrayList<>();
    public int nextTaskId;

    /**
     * Opens the file tasks.json and initializes all the app data in program memory, after the use
     * of app classes.
     */
    public task_pool ()
    {


        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader("src/main/medialab/tasks.json"));
            JSONObject jsonObject =  (JSONObject) obj;
            String nxtTaskId= (String) jsonObject.get("nextTaskID");
            try {
                this.nextTaskId = Integer.parseInt(nxtTaskId);
            }
            catch (NumberFormatException e) {
                this.nextTaskId = 1000;
            }
            JSONArray categories = (JSONArray) jsonObject.get("CategoriesList");

            for (Object o : categories) {
                JSONObject category = (JSONObject) o;
                String name = (String) category.get("name");
                //Initialization of CategoryList
                Category newcategory = new Category(name);
                this.categoryList.add(newcategory);
            }
            JSONArray priorities = (JSONArray) jsonObject.get("PrioritiesList");

            for (Object o : priorities) {
                JSONObject priority = (JSONObject) o;

                String name = (String) priority.get("name");
                //Initialization of CategoryList
                Priority newpriority = new Priority(name);
                this.priorityList.add(newpriority);
            }
            // loop array
            JSONArray tasks = (JSONArray) jsonObject.get("TasksList");
            for(Object o : tasks)
            {
                JSONObject task = (JSONObject) o;
                String ID = (String) task.get("id");
                String title = (String) task.get("Title");
                String description = (String) task.get("Description");
                String category = (String) task.get("Category");
                String priority = (String) task.get("Priority");
                String status = (String) task.get("Status");
                Status status1 = Status.valueOf(status);
                String deadline = (String) task.get("Deadline");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                formatter = formatter.withLocale( Locale.ITALIAN);
                LocalDate deadLine;
                deadLine = LocalDate.parse(deadline, formatter);
                //!!Elegxos olwn twn ergasiwn pou den einai Completed kai
                //Exei perasei to deadline tous na allaksei to status tous se delayed
                LocalDate datenow = LocalDate.now();
                if(datenow.isAfter(deadLine) && status1!=Status.COMPLETED)
                {
                   status1=Status.DELAYED;
                }
                int newID =  Integer.parseInt(ID);

                //Initialization of reminders list
                List<Reminder> taskReminderList = new ArrayList<>();
                JSONArray reminders = (JSONArray)  task.get("RemindersList");
                for(Object j : reminders)
                {
                    JSONObject reminder = (JSONObject) j;
                    String reminderCategory = (String) reminder.get("reminderCategory");
                    ReminderCat reminderCategory1 = ReminderCat.valueOf(reminderCategory);
                    String alertdate = (String) reminder.get("alertdate");
                    LocalDate alertDate;
                    alertDate = LocalDate.parse(alertdate, formatter);
                    String reminderId = (String) reminder.get("reminderID");
                    int reminderID = Integer.parseInt(reminderId);
                    Reminder newreminder = new Reminder(reminderCategory1, alertDate,deadLine,reminderID);
                    taskReminderList.add(newreminder);
                    ReminderViewableItem newReminderViewableItem = new ReminderViewableItem(reminderID,newID,title,reminderCategory1,alertDate,deadLine);
                    this.reminderViewableItemList.add(newReminderViewableItem);
                }
                //Read nextReminderId
                String nextTaskReminderIdString = (String) task.get("NewReminderId");
                int nextTaskReminderId = Integer.parseInt(nextTaskReminderIdString);
                //Initialization of taskList
                Task newtask = new Task(newID,title,description,category,priority,status1,deadLine,taskReminderList,nextTaskReminderId);
                this.taskList.add(newtask);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Increments the variable nextTaskId. This utility is not actually indispensable to the
     * app function, as it is executed by the rest of the app code.
     */
    public void incrementNextTaskId ()
    {
        this.nextTaskId++;
    }

    /**
     *  Function to initially fill the Total Completed Tasks number of the upper pane of our Application
     */

    public String getTotalCompletedTasksNum ()
    {
        int sum =0;
        for (int i=0;i<taskList.size();i++)
        {
            if(taskList.get(i).getStatus() == Status.COMPLETED)
            {
                sum++;
            }
        }
        return String.valueOf(sum);
    }
    /**
     *  Function to initially fill the Total Delayed Tasks (String type) number of the upper pane of our Application
     */
    public String getTotalDelayedTasksNum()
    {
        int sum =0;
        for (int i=0;i<taskList.size();i++)
        {
            if(taskList.get(i).getStatus() == Status.DELAYED)
            {
                sum++;
            }
        }
        return String.valueOf(sum);
    }
    /**
     *  Function to initially fill the Total next 7 days Tasks number of the upper pane of our Application
     */
    public String getNext7DaysTasksNum()
    {
        int sum =0;
        LocalDate today = LocalDate.now();
        for (int i=0;i<taskList.size();i++)
        {
            if(taskList.get(i).getDeadline().isBefore(today.plusDays(7))&& taskList.get(i).getStatus()!=Status.COMPLETED)
            {
                sum++;
            }
        }
        return String.valueOf(sum);

    }
    /**
     *  Function to initially fill the Total Delayed Tasks (int type) number of the upper pane of our Application
     */
    public int totalDelayedTasks ()
    {
        int sum = 0;
        for(Task task : taskList)
        {
            if (task.getStatus() == Status.DELAYED)
            {
                sum++;
            }
        }
        return sum;
    }

    /**
     * Function that saves the data upon program termination
     */

    public void writeTasksJson()
    {
       /* Apli domi tou tasks.json
           {"nextTaskID":"10",
            "CategoriesList":[{"name":"academic"},{"name":"professional"},{"name":"fitness"},{"name":"social"},{"name":"unknown"},{"name":"music"},{"name":"fun"}],
            "PrioritiesList":[{"name":"Default"},{"name":"High"},{"name":"Very High"},{"name":"Utmost"}],
            "TasksList":[{"Status":"COMPLETED",
                          "Description":"Task Reminder Desktop App written in Java for the ECE NTUA Course Multimedia Technology",
                          "Category":"Academic",
                          "Priority":"Utmost",
                          "NewReminderId":"2",
                          "Deadline":"2025-02-27",
                          "Title":"Ergasia Polumesa",
                          "id":"0",
                          "RemindersList":[]}
             */

        //Dimiourgia JSON object
        JSONObject jsonObject = new JSONObject();
        //Prosthiki nextTaskID

        jsonObject.put("nextTaskID",String.valueOf(nextTaskId));

        //Write CategoriesList to object
        JSONArray catList = new JSONArray();
        for(Category category : categoryList)
        {
            JSONObject catObj = new JSONObject();
            catObj.put("name",category.getCategoryName());
            catList.add(catObj);
        }
        //Prosthiki CategoriesList
        jsonObject.put("CategoriesList",catList);
        //Dimiourgia JSON array PrioritiesList
        JSONArray priorList = new JSONArray();
        for(Priority priority : priorityList)
        {
            JSONObject priorObj = new JSONObject();
            priorObj.put("name",priority.getPriorityName());
            priorList.add(priorObj);
        }
        //Prosthiki PrioritiesList
        jsonObject.put("PrioritiesList",priorList);
        //Dimiourgia JSON array TasksList
        JSONArray tskList = new JSONArray();
        for(Task task : taskList)
        {
            JSONObject taskObj = new JSONObject();
            taskObj.put("id",String.valueOf(task.getID()));
            taskObj.put("Title",task.getTitle());
            taskObj.put("Description",task.getDescription());
            taskObj.put("Category",task.getCategory());
            taskObj.put("Priority",task.getPriority());
            taskObj.put("Status",task.getStatus().toString());
            taskObj.put("Deadline",task.getDeadline().toString());
            taskObj.put("NewReminderId",String.valueOf(task.getNextReminderId()));
            JSONArray taskRemList = new JSONArray();
            for(Reminder taskRem : task.reminderList)
            {
                JSONObject remObj = new JSONObject();
                remObj.put("reminderID",String.valueOf(taskRem.getId()));
                remObj.put("reminderCategory",taskRem.getReminderCat().toString());
                remObj.put("alertdate",taskRem.getReminderDate().toString());
                taskRemList.add(remObj);
            }
            taskObj.put("RemindersList",taskRemList);
            tskList.add(taskObj);
        }
        //Prosthiki TasksList
        jsonObject.put("TasksList",tskList);

        //File Write
        try {
            FileWriter file = new FileWriter("src/main/medialab/tasks.json");
            file.write(jsonObject.toJSONString());
            file.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
        System.out.println("JSON file created: "+jsonObject);

    }
    }

