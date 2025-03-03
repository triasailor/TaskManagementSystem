package com.myreminder.myreminder;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TaskReminderController {

    @FXML public Text completedTasks;
    @FXML public Text delayedTasks;
    @FXML public Text next7DaysTasks;
    @FXML public ListView<String> categoriesList;
    @FXML public ListView<String> prioritiesList;
    @FXML public TableView<Task> tasksTableview;
    @FXML public TableColumn<Task,Integer> taskIdCol;
    @FXML public TableColumn<Task,String> taskTitleCol,taskDescrCol,taskCatCol,taskPriorCol;
    @FXML public TableColumn<Task,Status> taskStatCol;
    @FXML public TableColumn<Task, LocalDate> taskDeadlCol;
    @FXML public Button newTaskButton,updateTaskButton,updateCatButton,updatePriorButton,deleteTaskButton,setReminderButton;
    @FXML public DatePicker newTaskDatepicker;
    @FXML public Button addNewTaskButton;
    @FXML public TextField newTaskTitleTextfield;
    @FXML public TextField newTaskDescrTextfield;
    @FXML public ChoiceBox<String> addNewTaskPriorChkbox;
    @FXML public ChoiceBox<String> addNewTaskCategChkbox;
    @FXML public TextField addNewPriorTxtField;
    @FXML public Button addNewPriorButton;
    @FXML public Button addNewCategButton;
    @FXML public TextField addNewCategTxtField;
    @FXML public Button deleteCategButton;
    @FXML public Button deletePriorButton;
    @FXML public TextField updateCatTxtField, updatePriorTxtField;
    @FXML public Button catFormUpdateButton,priorFormUpdateButton;
    @FXML public TextField updateTaskDescrTxtField;
    @FXML public TextField updateTaskTitleTxtField;
    @FXML public DatePicker updateTaskDatepicker,reminderAlertDatePicker;
    @FXML public ChoiceBox<String> updateTaskPriorBox;
    @FXML public ChoiceBox<String> updateTaskCategBox;
    @FXML public ChoiceBox<Status> updateTaskStatusBox;
    @FXML public ChoiceBox<ReminderCat> reminderCatChoiceBox;
    @FXML public Button updateFormTaskButton;
    @FXML public TableView<ReminderViewableItem> remindersTableview;
    @FXML public TableColumn<ReminderViewableItem,Integer> remIdCol;
    @FXML public TableColumn<ReminderViewableItem,Integer> remTaskIdCol;
    @FXML public TableColumn<ReminderViewableItem,String> remTaskTitleCol;
    @FXML public TableColumn<ReminderViewableItem,ReminderCat> remCatCol;
    @FXML public TableColumn<ReminderViewableItem,LocalDate> remAlertDateCol,remTaskDeadlineCol;
    @FXML public Button deleteReminderButton,setReminderFormButton,searchTaskButton;
    @FXML public TableView<Task> foundtasksTableview;
    @FXML public TableColumn<Task,Integer> foundtaskIdCol;
    @FXML public TableColumn<Task,String> foundtaskTitleCol,foundtaskCatCol,foundtaskPriorCol;
    @FXML public TextField searchTitle,searchCategory,searchPriority;


    task_pool taskPool = new task_pool();

    public ObservableList<String> categoryItems, priorityItems;
    public ObservableList<Task> tasksTableMembers,foundTaskMembers;
    public ObservableList<ReminderViewableItem> reminderTableMembers;

    public int nextTaskID,totalTsks, totalComplTsks,totalDelayedTsks,next7DaysTsks;

    @FXML public Text totalTasks ;



    public void setTotalTasks(String totalTasks) {
        this.totalTasks.setText(totalTasks);

    }

    public void setCompletedTasks(String completedTasks) {
        this.completedTasks.setText(completedTasks);
    }

    public void setDelayedTasks(String delayedTasks) {
       this.delayedTasks.setText(delayedTasks);
    }

    public void setNext7DaysTasks(String next7DaysTasks) {
        this.next7DaysTasks.setText(next7DaysTasks);
    }

    public void renewUpperPaneNumbers()
    {
        int newTotalTasks =tasksTableMembers.size(), newCompletedTasks=0,newDelayedTasks=0,newNext7DaysTasks =0;
        LocalDate today = LocalDate.now();
        for(Task task : tasksTableMembers )
        {
            if(task.getStatus()==Status.COMPLETED)
                newCompletedTasks++;
            if(task.getStatus()==Status.DELAYED)
                newDelayedTasks++;
            if(task.getDeadline().isBefore(today.plusDays(7)) && task.getStatus()!=Status.COMPLETED)
                newNext7DaysTasks++;
        }
        setCompletedTasks(String.valueOf(newCompletedTasks));
        setTotalTasks(String.valueOf(newTotalTasks));
        setDelayedTasks(String.valueOf(newDelayedTasks));
        setNext7DaysTasks(String.valueOf(newNext7DaysTasks));
    }

    public void setCategoriesList(List<Category> categoryList) {
        List<String> catstringList = new ArrayList<>();
       for(Category cat : categoryList)
       {
          catstringList.add(cat.getCategoryName());
       }
       categoryItems = FXCollections.observableArrayList ( catstringList);
       categoriesList.setItems(categoryItems);
    }

    public void setPrioritiesList(List<Priority> priorityList) {
        List<String> priorstringList = new ArrayList<>();
        for(Priority prior : priorityList)
        {
            priorstringList.add(prior.getPriorityName());
        }
        priorityItems = FXCollections.observableArrayList (priorstringList);
        prioritiesList.setItems(priorityItems);
    }

    public void populateTaskTableView(task_pool tpool)
    {

        this.taskIdCol.setCellValueFactory(cellData->new SimpleObjectProperty<>(cellData.getValue().getID()));
        this.taskTitleCol.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getTitle()));
        this.taskDescrCol.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getDescription()));
        this.taskCatCol.setCellValueFactory(cellData->new SimpleObjectProperty<>(cellData.getValue().getCategory()));
        this.taskPriorCol.setCellValueFactory(cellData->new SimpleObjectProperty<>(cellData.getValue().getPriority()));
        this.taskStatCol.setCellValueFactory(cellData->new SimpleObjectProperty<>(cellData.getValue().getStatus()));
        this.taskDeadlCol.setCellValueFactory(cellData->new SimpleObjectProperty<>(cellData.getValue().getDeadline()));
        tasksTableMembers = FXCollections.observableArrayList(tpool.taskList);
        tasksTableview.setItems(tasksTableMembers);

    }

    public void populateReminderTableView(task_pool tpool)
    {
        remTaskIdCol.setCellValueFactory(cellData->new SimpleObjectProperty<>(cellData.getValue().getTaskId()));
        remIdCol.setCellValueFactory(cellData->new SimpleObjectProperty<>(cellData.getValue().getRemId()));
        remTaskTitleCol.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getTaskTitle()));
        remCatCol.setCellValueFactory(cellData->new SimpleObjectProperty<>(cellData.getValue().getReminderCategory()));
        remAlertDateCol.setCellValueFactory(cellData->new SimpleObjectProperty<>(cellData.getValue().getReminderAlertDate()));
        remTaskDeadlineCol.setCellValueFactory(cellData->new SimpleObjectProperty<>(cellData.getValue().getTaskDeadline()));
        reminderTableMembers = FXCollections.observableArrayList(tpool.reminderViewableItemList);
        remindersTableview.setItems(reminderTableMembers);

    }
    public void initializeAddNewTaskCheckBoxes()
    {
        addNewTaskCategChkbox.setItems(categoryItems);
        addNewTaskPriorChkbox.setItems(priorityItems);
    }
    public void initializeUpdateTaskCheckBoxes()
    {
        updateTaskCategBox.setItems(categoryItems);
        updateTaskPriorBox.setItems(priorityItems);
    }

    public void initializeStatusCheckBox()
    {
        ObservableList<Status> StatusMembers = FXCollections.observableArrayList(Status.DELAYED,Status.COMPLETED,Status.IN_PROGRESS,Status.POSTPONED);
        updateTaskStatusBox.setItems(StatusMembers);
    }

    public void initializeSetReminderChkbox()
    {
        ObservableList<ReminderCat> reminderCategoryMembers = FXCollections.observableArrayList(ReminderCat.ONE_DAY_BEFORE,ReminderCat.ONE_WEEK_BEFORE,ReminderCat.ONE_MONTH_BEFORE,ReminderCat.SPECIFIC_DATE_BEFORE);
        reminderCatChoiceBox.setItems(reminderCategoryMembers);
    }


    public void addNewPriority()
    {
        String newPrior = addNewPriorTxtField.getText();
        priorityItems.add(newPrior);
    }
    public void deletePriority()
    {
        String focusedPriority = prioritiesList.getFocusModel().getFocusedItem();

        //Delete except for Default Category
        if (!Objects.equals(focusedPriority, "Default"))
        {
            priorityItems.remove(focusedPriority);
            //Render to default priority all associated tasks
            setDefaultPriorityOFTasksWithDelPrior(focusedPriority);
        }
        else
        {

            FXMLLoader newfxmlLoader = new FXMLLoader(TaskReminder.class.getResource("defaultCaution.fxml"));
            Parent root1 = null;
            Stage stage = new Stage();
            try {
                root1 = newfxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(stage);
            Scene scene1 = new Scene(root1, 645, 375);
            dialog.setTitle("Caution Message");
            dialog.setScene(scene1);
            dialog.show();
        }
    }


    public void setDefaultPriorityOFTasksWithDelPrior(String deletedPriority)
    {
        for(Task task : tasksTableMembers)
        {
            if(Objects.equals(task.getPriority(), deletedPriority))
            {
                task.setPriority("Default");
            }

        }
    }

    public void addNewCategory()
    {    String newCat = addNewCategTxtField.getText();
         categoryItems.add(newCat);
    }
    public void deleteCategory()
    {
        String focusedCategory = categoriesList.getFocusModel().getFocusedItem();
        categoryItems.remove(focusedCategory);

        //Delete all associated category tasks
        for(int taskIndex=0;taskIndex< tasksTableMembers.size();taskIndex++)
        {
            if(Objects.equals(tasksTableMembers.get(taskIndex).getCategory(), focusedCategory))
            {
                tasksTableview.getFocusModel().focus(taskIndex);
                deleteTask();
            }
        }
    }


    public void newTaskButtonClicked()
    {

    }
    public void updateTaskButtonClicked()
    {

    }
    public void updateCategoryButtonClicked()
    {

    }
    public void updatePriorityButtonClicked()
    {

    }
    public void setReminderButtonClicked()
    {

    }
    public void initializeScene (String totalTasks, String completedTasks, String delayedTasks, String next7daysTasks,List<Category> categoryList,List<Priority> priorityList)
    {     setTotalTasks(totalTasks);
          setCompletedTasks(completedTasks);
          setDelayedTasks(delayedTasks);
          setNext7DaysTasks(next7daysTasks);
          setCategoriesList(categoryList);
          setPrioritiesList(priorityList);

    }

    public void createNewTask(MouseEvent mouseEvent) {
    }

    public void updateFocusedCategory() {

        String focusedCategory = categoriesList.getFocusModel().getFocusedItem();
        String newCategoryName = updateCatTxtField.getText();
        int indexOfFocusedCategory = categoryItems.indexOf(focusedCategory);
        categoryItems.remove(indexOfFocusedCategory);
        categoryItems.add(indexOfFocusedCategory,newCategoryName);

        //Update the name of the changed Category of Tasks
        for(Task task : tasksTableMembers)
        {
            if(Objects.equals(task.getCategory(), focusedCategory))
            {
               task.setCategory(newCategoryName);
            }
        }
    }

    public void updateFocusedPriority() {
        String focusedPriority = prioritiesList.getFocusModel().getFocusedItem();
        //Except for Default Priority
        if(!Objects.equals(focusedPriority, "Default"))
        {
            String newPriorityName = updatePriorTxtField.getText();
            int indexOfFocusedPriority = priorityItems.indexOf(focusedPriority);
            priorityItems.remove(indexOfFocusedPriority);
            priorityItems.add(indexOfFocusedPriority,newPriorityName);

            //Update the name of the changed Priority of Tasks
            for(Task task : tasksTableMembers)
            {
                if(Objects.equals(task.getPriority(), focusedPriority))
                {
                    task.setPriority(newPriorityName);
                }
            }
        }
        else
        {
            FXMLLoader newfxmlLoader = new FXMLLoader(TaskReminder.class.getResource("defaultCaution.fxml"));
            Parent root1 = null;
            Stage stage = new Stage();
            try {
                root1 = newfxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(stage);
            Scene scene1 = new Scene(root1, 645, 375);
            dialog.setTitle("Caution Message");
            dialog.setScene(scene1);
            dialog.show();
        }
    }


    public void addNewTask ()
    {
        String newTaskTitle = newTaskTitleTextfield.getText();
        String newTaskDescr = newTaskDescrTextfield.getText();
        String newTaskCateg = addNewTaskCategChkbox.getValue();
        String newTaskPrior = addNewTaskPriorChkbox.getValue();
        LocalDate newTaskDeadline = newTaskDatepicker.getValue();
        Status newTaskStatus = Status.OPEN;
        int newTaskID = nextTaskID;
        nextTaskID++;
        List<Reminder> newTaskReminderList = new ArrayList<>();
        int nextRemId = 0;
        Task newTask = new Task(newTaskID,newTaskTitle,newTaskDescr,newTaskCateg,newTaskPrior,newTaskStatus,newTaskDeadline,newTaskReminderList,nextRemId);
        tasksTableMembers.add(newTask);
        renewUpperPaneNumbers();
    }
    public void updateTask ()
    {   //Old Task
        Task focusedTask = tasksTableview.getFocusModel().getFocusedItem();
        int indexOfFocusedTask = tasksTableMembers.indexOf(focusedTask);
        //Updated Task
        String updatedTaskTitle = updateTaskTitleTxtField.getText();
        if(updatedTaskTitle.isEmpty())
            updatedTaskTitle = focusedTask.getTitle();
        String updatedTaskDescr = updateTaskDescrTxtField.getText();
        if(updatedTaskDescr.isEmpty())
            updatedTaskDescr = focusedTask.getDescription();
        String updatedTaskCateg = updateTaskCategBox.getValue();
        if(updatedTaskCateg==null)
            updatedTaskCateg = focusedTask.getCategory();
        String updatedTaskPrior = updateTaskPriorBox.getValue();
        if(updatedTaskPrior==null)
            updatedTaskPrior = focusedTask.getPriority();
        LocalDate updatedTaskDate = updateTaskDatepicker.getValue();
        if(updatedTaskDate ==null)
            updatedTaskDate = focusedTask.getDeadline();
        Status updateTaskStatus = updateTaskStatusBox.getValue();
        if(updateTaskStatus ==null)
            updateTaskStatus = focusedTask.getStatus();
        int updatedTaskID = focusedTask.getID();
        int updatedTaskNextRemId = focusedTask.getNextReminderId();
        List<Reminder> completedTaskVoidReminderList = new ArrayList<>();
        List<Reminder> updatedTaskRemList = focusedTask.reminderList;
        LocalDate today = LocalDate.now();
        //In case Completed
        if(updateTaskStatus==Status.COMPLETED && focusedTask.getStatus()!=Status.COMPLETED)
        {
            //Delete associated reminders and decreasing the next reminder ID of specific task
            for(ReminderViewableItem rem : reminderTableMembers)
            {
                if(rem.getTaskId()==focusedTask.getID())
                {
                    reminderTableMembers.remove(rem);
                    focusedTask.setNextReminderId(focusedTask.getNextReminderId()-1);
                }
            }
            updatedTaskNextRemId = focusedTask.getNextReminderId();
            updatedTaskRemList = completedTaskVoidReminderList;
            updatedTaskDate = today;
        } else {
            //In case date is set to past the task will be considered delayed and deadline set to today

            if(updatedTaskDate.isBefore(today))
            {
                updateTaskStatus = Status.DELAYED;
                updatedTaskDate = today;
            }
        }


        Task updatedTask = new Task(updatedTaskID,updatedTaskTitle,updatedTaskDescr,updatedTaskCateg,updatedTaskPrior,updateTaskStatus,updatedTaskDate,updatedTaskRemList,updatedTaskNextRemId);
        tasksTableMembers.remove(indexOfFocusedTask);
        tasksTableMembers.add(indexOfFocusedTask,updatedTask);
        renewUpperPaneNumbers();
    }

    public void deleteTask ()
    {
        Task focusedTask = tasksTableview.getFocusModel().getFocusedItem();
        int indexOfFocusedTask = tasksTableMembers.indexOf(focusedTask);

        //Delete associated reminders and decreasing the next reminder ID of specific task
        for(ReminderViewableItem rem : reminderTableMembers)
        {
            if(rem.getTaskId()==focusedTask.getID())
            {
                reminderTableMembers.remove(rem);
                focusedTask.setNextReminderId(focusedTask.getNextReminderId()-1);
            }
        }
        tasksTableMembers.remove(indexOfFocusedTask);
        renewUpperPaneNumbers();
    }
    public void addNewReminderOnTask()
    {
        Task focusedTask = tasksTableview.getFocusModel().getFocusedItem();
        int indexOfFocusedTask = tasksTableMembers.indexOf(focusedTask);

        int newRemId = focusedTask.getNextReminderId();
        focusedTask.setNextReminderId(newRemId+1);
        int newRemTaskId = focusedTask.getID();
        String newRemTaskTitle = focusedTask.getTitle();
        ReminderCat newRemCat = reminderCatChoiceBox.getValue();
        LocalDate alertdate = reminderAlertDatePicker.getValue();
        LocalDate deadline = focusedTask.getDeadline();
        LocalDate today = LocalDate.now();
        if(focusedTask.getStatus()==Status.COMPLETED)
        {
            popSetReminderCautionWindow();
            return;
        }
            //If at least one form field is valid we proceed
            if (newRemCat != null || alertdate != null) {   //In case specific date has been input we do not care about Reminder Category
                if (alertdate != null) {
                    //Alert date must be before Task Deadline
                    if (alertdate.isBefore(deadline)&& !alertdate.isBefore(today)) {
                        newRemCat = ReminderCat.SPECIFIC_DATE_BEFORE;
                    } else
                    {
                        popSetReminderCautionWindow();
                        return;
                    }
                } else
                //We proceed with the selected Reminder Category
                {
                    if (newRemCat == ReminderCat.SPECIFIC_DATE_BEFORE && alertdate == null) {
                        popSetReminderCautionWindow();
                    } else {   //Date Validation could be inserted here
                        if (newRemCat == ReminderCat.ONE_DAY_BEFORE) {
                            alertdate = deadline.minusDays(1);
                            if (alertdate.isAfter(deadline) || alertdate.isBefore(today))
                            {
                                popSetReminderCautionWindow();
                                return;
                            }
                        } else if (newRemCat == ReminderCat.ONE_WEEK_BEFORE) {
                            alertdate = deadline.minusDays(7);
                            if (alertdate.isAfter(deadline) || alertdate.isBefore(today))
                            {
                                popSetReminderCautionWindow();
                                return;
                            }
                        } else {
                            alertdate = deadline.minusDays(30);
                            if (alertdate.isAfter(deadline) || alertdate.isBefore(today))
                            {
                                popSetReminderCautionWindow();
                                return;
                            }
                        }
                    }

                }

            } else {
                popSetReminderCautionWindow();
                return;
            }
        //After the above Date Validation the reminder can be added on the selected task
        ReminderViewableItem newRemItem = new ReminderViewableItem(newRemId,newRemTaskId,newRemTaskTitle,newRemCat,alertdate,deadline);
        reminderTableMembers.add(newRemItem);
    }
    public void deleteReminder() {

        ReminderViewableItem focusedReminder = remindersTableview.getFocusModel().getFocusedItem();
        int indexOfFocusedRem = reminderTableMembers.indexOf(focusedReminder);
        reminderTableMembers.remove(indexOfFocusedRem);
        for(Task task : tasksTableMembers)
        {
            if(focusedReminder.getTaskId()==task.getID())
            {
                task.setNextReminderId(task.getNextReminderId()-1);
            }
        }
    }

    public void popSetReminderCautionWindow()
    {
        FXMLLoader newfxmlLoader = new FXMLLoader(TaskReminder.class.getResource("setReminderRules.fxml"));
        Parent root1 = null;
        Stage stage = new Stage();
        try {
            root1 = newfxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);
        Scene scene1 = new Scene(root1, 645, 375);
        dialog.setTitle("Caution Message");
        dialog.setScene(scene1);
        dialog.show();
    }

    public void populateSearchTable() {
        String searchdTitle = searchTitle.getText();
        String searchdCategory = searchCategory.getText();
        String searchdPriority = searchPriority.getText();
        if(searchdTitle.isEmpty()&&searchdCategory.isEmpty()&&searchdPriority.isEmpty())
            return;
        List<Task> newSearchTaskList = new ArrayList<>();
        for(Task task : tasksTableMembers)
        {
            if(!searchdTitle.isEmpty() && task.getTitle().contains(searchdTitle))
            {
                newSearchTaskList.add(task);
                continue;
            }
            if(!searchdCategory.isEmpty() && Objects.equals(task.getCategory(), searchdCategory))
            {
                newSearchTaskList.add(task);
                continue;
            }
            if(!searchdPriority.isEmpty() && Objects.equals(task.getPriority(), searchdPriority))
            {
                newSearchTaskList.add(task);
            }
        }
        foundtaskIdCol.setCellValueFactory(cellData->new SimpleObjectProperty<>(cellData.getValue().getID()));
        foundtaskTitleCol.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getTitle()));
        foundtaskCatCol.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getCategory()));
        foundtaskPriorCol.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getPriority()));
        foundTaskMembers = FXCollections.observableArrayList(newSearchTaskList);
        foundtasksTableview.setItems(foundTaskMembers);
    }
}