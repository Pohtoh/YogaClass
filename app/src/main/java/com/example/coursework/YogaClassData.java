package com.example.coursework;

public class YogaClassData {
    public static final String TABLE_NAME = "yogaClass";
    public static final String COLUMN_ID = "classID";
    public static final String COLUMN_DAY = "day";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_NUMBER_OF_PEOPLE = "numberOfPeople";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_CLASS_TYPE = "classType";
    public static final String COLUMN_DESCRIPTION = "description";


    public YogaClassData() {

    }

    public YogaClassData(int id, String classType, String day, String time, int duration, int numberOfPeople, int price, String description) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private int id;
    private String day;
    private String time;
    private int duration;
    private int numberOfPeople;
    private int price;
    private String classType;
    private String description;


    public YogaClassData(String day, String time, int duration, int numberOfPeople, int price, String classType, String description) {
        this.day = day;
        this.time = time;
        this.duration = duration;
        this.numberOfPeople = numberOfPeople;
        this.price = price;
        this.classType = classType;
        this.description = description;
    }

    @Override
    public String toString() {
        return "YogaClassData{" +
                "day=" + day +
                "time=" + time +
                ", duration=" + duration +
                ", numberOfPeople=" + numberOfPeople +
                ", price=" + price +
                ", classType='" + classType + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_DAY + " TEXT,"
                    + COLUMN_TIME + " INTEGER,"
                    + COLUMN_DURATION + " INTEGER,"
                    + COLUMN_NUMBER_OF_PEOPLE + " INTEGER,"
                    + COLUMN_PRICE + " INTEGER,"
                    + COLUMN_CLASS_TYPE + " TEXT,"
                    + COLUMN_DESCRIPTION + " TEXT" + ")";
}
