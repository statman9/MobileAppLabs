package com.example.josh.lab2;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.content.ContentValues;
import android.renderscript.Sampler;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.Key;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Josh on 10/2/2017.
 */

public class CommentsDataSource {
    private SQLiteDatabase db;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {MySQLiteHelper.Column_ID,
            MySQLiteHelper.Column_InputType,
            MySQLiteHelper.Column_ActivityType,
            MySQLiteHelper.Column_Date,
            MySQLiteHelper.Column_Duration,
            MySQLiteHelper.Column_Distance,
            MySQLiteHelper.Column_Calories,
            MySQLiteHelper.Column_HeartRate,
            MySQLiteHelper.Column_Comment,
            MySQLiteHelper.Column_Latitudes,
            MySQLiteHelper.Column_Longitudes
    };

    private static final String TAG = "DBDemo";

    public CommentsDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Comment createComment(String inputType, String activityType, long activityDateTime, double activityDuration, int activityDistance, int activityCalories, int activityHeartRate, String comment, String latitudes, String longitudes) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.Column_Comment, comment);
        values.put(MySQLiteHelper.Column_InputType, inputType);
        values.put(MySQLiteHelper.Column_ActivityType, activityType);
        values.put(MySQLiteHelper.Column_Calories, activityCalories);
        values.put(MySQLiteHelper.Column_Date, activityDateTime);
        values.put(MySQLiteHelper.Column_Duration, activityDuration);
        values.put(MySQLiteHelper.Column_Distance, activityDistance);
        values.put(MySQLiteHelper.Column_HeartRate, activityHeartRate);
        values.put(MySQLiteHelper.Column_Latitudes, latitudes);
        values.put(MySQLiteHelper.Column_Longitudes, longitudes);
        long insertId = db.insert(
                MySQLiteHelper.Table_Comments,
                null,
                values
        );
        Map<String, Map<String, String>> record = new HashMap<>();
        Cursor cursor = db.query(MySQLiteHelper.Table_Comments, allColumns, MySQLiteHelper.Column_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Comment newComment = cursorToComment(cursor);
        Map<String, String> attributes = new HashMap();
        attributes.put("ID", String.valueOf(insertId));
        attributes.put(MySQLiteHelper.Column_ActivityType, activityType);
        attributes.put(MySQLiteHelper.Column_InputType, inputType);
        attributes.put(MySQLiteHelper.Column_Calories, String.valueOf(activityCalories));
        attributes.put(MySQLiteHelper.Column_Comment, comment);
        attributes.put(MySQLiteHelper.Column_Date, String.valueOf(activityDateTime));
        attributes.put(MySQLiteHelper.Column_Distance, String.valueOf(activityDistance));
        attributes.put(MySQLiteHelper.Column_Duration, String.valueOf(activityDuration));
        attributes.put(MySQLiteHelper.Column_HeartRate, String.valueOf(activityHeartRate));
        attributes.put(MySQLiteHelper.Column_Latitudes, latitudes);
        attributes.put(MySQLiteHelper.Column_Longitudes, longitudes);

        record.put("Record", attributes);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Records");

        myRef.child("id"+String.valueOf(insertId)).setValue(record);
        
        cursor.close();
        return newComment;
    }

    public void deleteComment(Comment comment) {
        long id = comment.getId();
        System.out.println("Comment deleted with id: " + id);
        db.delete(MySQLiteHelper.Table_Comments, MySQLiteHelper.Column_ID + " = " + id, null);
    }

    public void deleteAllComments() {
            System.out.println("All comments have been deleted.");
            db.delete(MySQLiteHelper.Table_Comments, null, null);
    }

    public Comment getComment(long id) {
        Cursor cursor = db.query(MySQLiteHelper.Table_Comments, allColumns, MySQLiteHelper.Column_ID + " = " + id, null, null, null, null);
        cursor.moveToFirst();
        Comment comment = cursorToComment(cursor);

        cursor.close();
        return comment;
    }

    public List<String> getAllComments() {
        List<String> comments = new ArrayList<String>();
        Cursor cursor = db.query(MySQLiteHelper.Table_Comments, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Comment comment = cursorToComment(cursor);
            String commentStr = comment.toString();
            comments.add(commentStr);
            cursor.moveToNext();
        }
        cursor.close();
        return comments;
    }

    private Comment cursorToComment(Cursor cursor) {
        Comment comment = new Comment();
        comment.setId(cursor.getLong(0));
        comment.setInputType(cursor.getString(1));
        comment.setActivityType(cursor.getString(2));
        Date activityDate = new Date(cursor.getLong(3));
        comment.setActivityDateTime(activityDate);
        comment.setActivityDuration(cursor.getDouble(4));
        comment.setActivityDistance(cursor.getInt(5));
        comment.setActivityCalories(cursor.getInt(6));
        comment.setActivityHeartRate(cursor.getInt(7));
        comment.setComment(cursor.getString(8));
        comment.setActivityLatitudes(cursor.getString(9));
        comment.setActivityLongitudes(cursor.getString(10));
        return comment;
    }
}
