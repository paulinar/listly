package com.codepath.listly.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.UUID;

/**
 * Created by paulina.
 */

// Everything dealing with the SQLite database goes in this class
public class Database {

	private DatabaseHelper databaseHelper;
	private SQLiteDatabase sqLiteDatabase;

	private final Context context; // current context (activity)

	public static final String DATABASE_NAME = "listly.db";
	public static final int DATABASE_VERSION = 2;
	
	/* TASK TABLE */
	public static final String TASK_TABLE_NAME = "_task";
	public static final String TASK_TABLE_COLUMN_ID = "_id";
	public static final String TASK_TABLE_COLUMN_TITLE = "_title";
	public static final String TASK_TABLE_COLUMN_DUE_DATE = "_due_date";
	public static final String TASK_TABLE_COLUMN_NOTES = "_note";
	public static final String TASK_TABLE_COLUMN_PRIORITY = "_priority";
	public static final String TASK_TABLE_COLUMN_STATUS = "_status";

    // prepare SQL query for creating Task table
	public static final String TASK_TABLE_CREATE
			= "create table " + TASK_TABLE_NAME
			+ " ( "
			+ TASK_TABLE_COLUMN_ID + " text primary key, "
			+ TASK_TABLE_COLUMN_TITLE + " text not null, "
			+ TASK_TABLE_COLUMN_DUE_DATE + " integer not null, "
			+ TASK_TABLE_COLUMN_NOTES + " text,"
			+ TASK_TABLE_COLUMN_PRIORITY + " text not null, "
			+ TASK_TABLE_COLUMN_STATUS + " integer not null, "
            + "foreign key ( " + TASK_TABLE_COLUMN_ID + " ) references " + TASK_TABLE_NAME + " ( " + TASK_TABLE_COLUMN_ID + " ) "
            + " );";

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context, String name, CursorFactory factory, int version){
			super(context, name, factory, version); // need to override constructor
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(Database.TASK_TABLE_CREATE); // create Task table by executing SQL query
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("Drop table if exists " + Database.TASK_TABLE_NAME);
			onCreate(db);
		}
		
	}

	public Database(Context context){
        this.context = context; // pass currenct Activity to the context
	}

	public Database open() { // connect to DB
		databaseHelper = new DatabaseHelper(context, this.DATABASE_NAME, null, this.DATABASE_VERSION);
		sqLiteDatabase = databaseHelper.getWritableDatabase();
		return this;
	}

	public void close() { // disconnect from DB
        databaseHelper.close();
	}

	public void insertTaskIntoDB(Task task){
		ContentValues initialValues = new ContentValues();
		initialValues.put(TASK_TABLE_COLUMN_ID, task.getId());
		initialValues.put(TASK_TABLE_COLUMN_TITLE, task.getTitle());
		initialValues.put(TASK_TABLE_COLUMN_DUE_DATE, task.getDueDate().getTimeInMillis());
		initialValues.put(TASK_TABLE_COLUMN_NOTES, task.getNotes());
		initialValues.put(TASK_TABLE_COLUMN_PRIORITY, task.getPriorityLevel());
		initialValues.put(TASK_TABLE_COLUMN_STATUS, task.getStatus());
		sqLiteDatabase.insert(TASK_TABLE_NAME, null, initialValues);

	}

	public Cursor getAllTasks(){
		return sqLiteDatabase.query(TASK_TABLE_NAME,
				new String[] {TASK_TABLE_COLUMN_ID, TASK_TABLE_COLUMN_TITLE, TASK_TABLE_COLUMN_DUE_DATE, TASK_TABLE_COLUMN_NOTES, TASK_TABLE_COLUMN_PRIORITY, TASK_TABLE_COLUMN_STATUS},
				null, null, null, null, null);
	}

	public Cursor getTaskById(String taskId){
		return sqLiteDatabase.query(TASK_TABLE_NAME,
				new String[] {TASK_TABLE_COLUMN_ID, TASK_TABLE_COLUMN_TITLE, TASK_TABLE_COLUMN_DUE_DATE, TASK_TABLE_COLUMN_NOTES, TASK_TABLE_COLUMN_PRIORITY, TASK_TABLE_COLUMN_STATUS},
				TASK_TABLE_COLUMN_ID + " = '" + taskId + "'", null, null, null, null);
	}

	public void editExistingTask(Task task){
		ContentValues updateValues = new ContentValues();
		updateValues.put(TASK_TABLE_COLUMN_TITLE, task.getTitle());
		updateValues.put(TASK_TABLE_COLUMN_NOTES, task.getNotes());
		updateValues.put(TASK_TABLE_COLUMN_DUE_DATE, task.getDueDate().getTimeInMillis());
		updateValues.put(TASK_TABLE_COLUMN_PRIORITY, task.getPriorityLevel());
		updateValues.put(TASK_TABLE_COLUMN_STATUS, task.getStatus());
		sqLiteDatabase.update(TASK_TABLE_NAME, updateValues, TASK_TABLE_COLUMN_ID + " = '" + task.getId() + "'", null);
	}

	public void deleteTask(Task task){
        deleteTaskByID(task.getId());
	}

	public void deleteTaskByID(String taskId){
		sqLiteDatabase.delete(TASK_TABLE_NAME, TASK_TABLE_COLUMN_ID + " = '" + taskId + "'", null);
	}

	public String getNewTaskId(){
		String uuid = null;
		Cursor cursor = null;

		do {
			uuid = UUID.randomUUID().toString();
			cursor = getTaskById(uuid);
		} while (cursor.getCount() > 0);
		
		return uuid; // return randomly generated task id
	}
}
