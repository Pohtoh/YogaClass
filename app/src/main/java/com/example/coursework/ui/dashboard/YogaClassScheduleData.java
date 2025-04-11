package com.example.coursework;

public class YogaClassScheduleData {
    public YogaClassScheduleData(int id,int yogaClassID, String date, String teacher, String description) {
        this.id = id;
        this.yogaClassID = yogaClassID;
        this.date = date;
        this.teacher = teacher;
        this.description = description;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getYogaClassID() {
        return yogaClassID;
    }

    public void setYogaClassID(int yogaClassID) {
        this.yogaClassID = yogaClassID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private int id;
    private int yogaClassID;
    private String date;
    private String teacher;
    private String description;

    public static final String TABLE_NAME = "yogaClassSchedule";
    public static final String COLUMN_ID = "classID";
    public static final String COLUMN_YOGA_CLASS_ID = "yogaClassID";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TEACHER = "Teacher";
    public static final String COLUMN_DESCRIPTION = "description";

    public YogaClassScheduleData() {

    }
    public YogaClassScheduleData(String date, String teacher, String description) {
        this.date = date;
        this.teacher = teacher;
        this.description = description;
    }
    @Override
    public String toString() {
        return "YogaClassScheduleData{" +
                "date='" + date + '\'' +
                ", teacher='" + teacher + '\'' +
                ", description='" + description + '\'' +
                '}';

    }
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_YOGA_CLASS_ID + " INTEGER, "
                    + COLUMN_DATE + " TEXT, "
                    + COLUMN_TEACHER + " TEXT, "
                    + COLUMN_DESCRIPTION + " TEXT, "
                    + "FOREIGN KEY(yogaClassID) REFERENCES yogaClass(classID) ON DELETE CASCADE"
                    + ")";
}
