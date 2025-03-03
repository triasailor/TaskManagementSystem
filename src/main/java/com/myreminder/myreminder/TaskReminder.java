package com.myreminder.myreminder;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;



public class TaskReminder extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(TaskReminder.class.getResource("medialabTaskReminder.fxml"));
        task_pool taskPool = new task_pool();
        Parent root = fxmlLoader.load();
        TaskReminderController controller = fxmlLoader.getController();
        Scene scene = new Scene(root, 645, 375);
        controller.totalTsks = taskPool.taskList.size();
        controller.totalDelayedTsks = Integer.parseInt(taskPool.getTotalDelayedTasksNum());
        controller.totalComplTsks = Integer.parseInt(taskPool.getTotalCompletedTasksNum());
        controller.next7DaysTsks = Integer.parseInt(taskPool.getNext7DaysTasksNum());
        String totalTs = String.valueOf(taskPool.taskList.size());
        String completedTs = taskPool.getTotalCompletedTasksNum();
        String delayedTs = taskPool.getTotalDelayedTasksNum();
        String next7Ts = taskPool.getNext7DaysTasksNum();
        controller.nextTaskID = taskPool.nextTaskId;
        controller.initializeScene(totalTs, completedTs, delayedTs, next7Ts, taskPool.categoryList, taskPool.priorityList);
        controller.populateTaskTableView(taskPool);
        controller.populateReminderTableView(taskPool);
        stage.setTitle("MediaLab Assistant");
        stage.setScene(scene);
        stage.show();

        //Termination of the program and data persistence to tasks.json
        stage.setOnCloseRequest(event -> {
            // Prevent default close action
            event.consume();
            System.out.println("Window is closing...");
            //Data persistence here
           /*
  "PrioritiesList": [{"name":"Default" },{"name": "High"},{"name": "Very High"},{"name":"Utmost" }],
  "TasksList": [
  {"id":"0",
    "Title": "Ergasia Polumesa",
    "Description": "Task Reminder Desktop App written in Java for the ECE NTUA Course Multimedia Technology",
    "Category": "Academic",
    "Priority": "Default",
    "Status": "OPEN",
    "Deadline": "2025-02-10",
    "RemindersList": [
      {"reminderID":"0",
        "reminderCategory": "ONE_WEEK_BEFORE",
        "alertdate": "2025-04-03"
      }],"NewReminderId": "1"}
            */
            //NextTaskID
            taskPool.nextTaskId = controller.nextTaskID;
            //Categories
            List<Category> currentCategoryList = new ArrayList<>();
            for(String cat : controller.categoryItems)
            {
                Category newCat = new Category(cat);
                currentCategoryList.add(newCat);
            }
            taskPool.categoryList = currentCategoryList;
            //Priorities
            List<Priority> currentPriorityList = new ArrayList<>();
            for(String prior : controller.priorityItems)
            {
                Priority newPrior = new Priority(prior);
                currentPriorityList.add(newPrior);
            }
            taskPool.priorityList = currentPriorityList;
            //Tasks
            List<Task> currentTaskList = new ArrayList<>();
            for(Task task : controller.tasksTableMembers)
            {
                int newTaskID = task.getID();
                String newTaskTitle = task.getTitle();
                String newTaskDescription = task.getDescription();
                String newTaskCategory = task.getCategory();
                String newTaskPriority = task.getPriority();
                Status newTaskStatus = task.getStatus();
                LocalDate newTaskDeadline = task.getDeadline();
                int newTaskNextReminderId = task.getNextReminderId();
                //Task Reminder List build
                List<Reminder> newTaskReminderList = new ArrayList<>();
                for(ReminderViewableItem reminderItem : controller.reminderTableMembers)
                {
                    if(reminderItem.getTaskId() == newTaskID)
                    {
                        int newTaskRemId = reminderItem.getRemId();
                        ReminderCat newTaskRemCat = reminderItem.getReminderCategory();
                        LocalDate newTaskReminderAlertDate = reminderItem.getReminderAlertDate();
                        LocalDate newTaskReminderTaskDeadline = reminderItem.getTaskDeadline();
                        Reminder newTaskReminder = new Reminder(newTaskRemCat,newTaskReminderAlertDate,newTaskReminderTaskDeadline,newTaskRemId);
                        newTaskReminderList.add(newTaskReminder);
                    }
                }
                Task newTask = new Task(newTaskID,newTaskTitle,newTaskDescription,newTaskCategory,newTaskPriority,newTaskStatus,newTaskDeadline,newTaskReminderList,newTaskNextReminderId);
                currentTaskList.add(newTask);
            }
            taskPool.taskList = currentTaskList;
            //tasks.json File Write
            taskPool.writeTasksJson();
            //Final App Termination
            stage.close();

        });

        //New Task Window
        controller.newTaskButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                FXMLLoader newfxmlLoader = new FXMLLoader(TaskReminder.class.getResource("addTask.fxml"));
                Parent root = null;
                newfxmlLoader.setController(controller);
                try {
                    root = newfxmlLoader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                final Stage dialog = new Stage();

                Platform.runLater(controller::initializeAddNewTaskCheckBoxes);
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(stage);
                Scene scene = new Scene(root, 645, 375);
                dialog.setTitle("New Task Form");
                dialog.setScene(scene);
                dialog.show();
            }
        });

        //Update Task Window
        controller.updateTaskButton.setOnMouseClicked(mouseEvent -> {
            FXMLLoader newfxmlLoader = new FXMLLoader(TaskReminder.class.getResource("updateTask.fxml"));
            Parent root1 = null;
            newfxmlLoader.setController(controller);
            try {
                root1 = newfxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            final Stage dialog = new Stage();
            Platform.runLater(controller::initializeUpdateTaskCheckBoxes);
            Platform.runLater(controller::initializeStatusCheckBox);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(stage);
            Scene scene1 = new Scene(root1, 645, 375);
            dialog.setTitle("Update Task Form");
            dialog.setScene(scene1);
            dialog.show();
        });

        //Update Category Window
        controller.updateCatButton.setOnMouseClicked(mouseEvent -> {
            FXMLLoader newfxmlLoader = new FXMLLoader(TaskReminder.class.getResource("updateCategory.fxml"));
            Parent root1 = null;
            newfxmlLoader.setController(controller);
            try {
                root1 = newfxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(stage);
            Scene scene1 = new Scene(root1, 645, 375);
            dialog.setTitle("Update Category Form");
            dialog.setScene(scene1);
            dialog.show();
        });
        //Update Priority Window
        controller.updatePriorButton.setOnMouseClicked(mouseEvent -> {
            FXMLLoader newfxmlLoader = new FXMLLoader(TaskReminder.class.getResource("updatePriority.fxml"));
            Parent root1 = null;
            newfxmlLoader.setController(controller);
            try {
                root1 = newfxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(stage);
            Scene scene1 = new Scene(root1, 645, 375);
            dialog.setTitle("Update Priority Form");
            dialog.setScene(scene1);
            dialog.show();
        });
        //Set Reminder On Task Window
        controller.setReminderButton.setOnMouseClicked(mouseEvent -> {
            FXMLLoader newfxmlLoader = new FXMLLoader(TaskReminder.class.getResource("setReminder.fxml"));
            Parent root1 = null;
            newfxmlLoader.setController(controller);
            try {
                root1 = newfxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            final Stage dialog = new Stage();
            Platform.runLater(controller::initializeSetReminderChkbox);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(stage);
            Scene scene1 = new Scene(root1, 645, 375);
            dialog.setTitle("Set Reminder Form");
            dialog.setScene(scene1);
            dialog.show();
        });

        //Pop-up Window
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);
        VBox dialogVbox = new VBox(20);
        String delayedTasksTotal = String.valueOf(taskPool.totalDelayedTasks());
        Color popupPaint;
        Text popupText;
        if(taskPool.totalDelayedTasks()==0)
        {
            popupText = new Text("Good Job! You are on track! No tasks Delayed found!");
            popupPaint = Color.GREEN;

        }else
        {
            popupText= new Text("Please be aware that after retrieving your data, it seems you have "+delayedTasksTotal+" task(s) delayed!");
            popupPaint = Color.LIGHTCORAL;
        }

        popupText.setStroke(popupPaint);
        TextAlignment textAlignment = TextAlignment.CENTER;
        popupText.setTextAlignment(textAlignment);
        popupText.setFont(new Font(20));
        popupText.setWrappingWidth(300);
        dialogVbox.getChildren().add(popupText);
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setTitle("Delayed Tasks Info");
        dialog.setScene(dialogScene);
        dialog.show();
    }

    public static void main(String[] args) {
        launch();
    }
}