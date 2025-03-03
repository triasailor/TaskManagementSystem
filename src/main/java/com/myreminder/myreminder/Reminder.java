package com.myreminder.myreminder;

import java.time.LocalDate;


/**
 * This class interacts with {@link ReminderCat}
 * The {@code Reminder} class creates an instance of Reminder on a specific task
 * <p>
 *  This class includes getter and setter methods for a Reminder object
 *  </p>

 * @author Triantafyllos Papoutsis
 * @version 1.0
 * @since 2025-02-27
 */
public class Reminder {
    LocalDate reminderDate,taskDeadline;
    ReminderCat reminderCat;
    int id;
    /**
     * {@code Reminder} Constructs a Reminder
     * See {@link ReminderCat} for details.
     * @param reminderCat The reminder category
     * @param alertDate The alert date of the reminder
     * @param deadline The actual deadline of the associated task
     * @param  ID the ID of the reminder of the specific task
     */
    public Reminder (ReminderCat reminderCat, LocalDate alertDate,LocalDate deadline,int ID)
    {

        this.taskDeadline = deadline;
        this.id = ID;
        this.reminderCat = reminderCat;
        if (reminderCat == ReminderCat.SPECIFIC_DATE_BEFORE)
        {
            //Date Validation
            this.reminderDate = alertDate;

        }
        else
        {   //Date Validation could be inserted here
            if (reminderCat == ReminderCat.ONE_DAY_BEFORE)
            {
              this.reminderDate = deadline.minusDays(1);
            }
            else if (reminderCat == ReminderCat.ONE_WEEK_BEFORE)
            {
                this.reminderDate = deadline.minusDays(7);
            }
            else
            {
                this.reminderDate = deadline.minusDays(30);
            }
        }

    }

    /**
     *
     * @return The value of the reminder Id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id the value that we set for the id of the reminder
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return The reminder alert date
     */
    public LocalDate getReminderDate() {
        return reminderDate;
    }

    /**
     *
     * @param reminderDate The date that we set for reminder alert date
     */
    public void setReminderDate(LocalDate reminderDate) {
        this.reminderDate = reminderDate;
    }

    /**
     * See {@link ReminderCat} for details.
     * @return The reminder category of the reminder
     */
    public ReminderCat getReminderCat() {
        return reminderCat;
    }

    /**
     * See {@link ReminderCat} for details.
     * @param reminderCat The category we set for the reminder object
     */
    public void setReminderCat(ReminderCat reminderCat) {
        this.reminderCat = reminderCat;
    }
}
