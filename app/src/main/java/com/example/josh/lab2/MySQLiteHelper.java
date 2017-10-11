package com.example.josh.lab2;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

/**
 * Created by Josh on 10/2/2017.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {
    private static final String Database_Name = "activity.db";
    public static final String Table_Comments = "Activity";
    public static final String Column_ID = "_id";
    public static final String Column_InputType = "InputType";
    public static final String Column_ActivityType = "ActivityType";
    public static final String Column_Date = "ActivityDateTime";
    public static final String Column_Duration = "ActivityDuration";
    public static final String Column_Distance = "ActivityDistance";
    public static final String Column_Calories = "ActivityCalories";
    public static final String Column_HeartRate = "ActivityHeartRate";
    public static final String Column_Comment = "ActivityComment";
    public static final int Database_Version = 1;
    private static final String Database_Create = "create table "
            + Table_Comments + "(" + Column_ID + " integer primary key autoincrement, "
            + Column_InputType + " text not null, "
            + Column_ActivityType + " text not null, "
            + Column_Date + " datetime not null, "
            + Column_Duration + " double not null, "
            + Column_Distance + " integer not null, "
            + Column_Calories + " integer not null, "
            + Column_HeartRate + " integer not null, "
            + Column_Comment + " text not null"
            + ");";

    public MySQLiteHelper(Context context) {
        super(context, Database_Name, null, Database_Version);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Database_Create);
    }

    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
        db.execSQL("DROP TABLE IF EXISTS " + Table_Comments);
        onCreate(db);
    }
}
