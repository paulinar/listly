package com.codepath.listly.view;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.codepath.listly.R;
import com.codepath.listly.model.Database;
import com.codepath.listly.model.Task;
import com.codepath.listly.presenter.ActivityManager;

/**
 * Created by paulina.
 */

public class AllTasksActivity extends Activity {

    private ListView allTasksListView;
    private ListView allTasksListView2;
    private Database database;
    private Cursor allTasksCursor; // cursor queries all tasks from the database
    private SimpleCursorAdapter allTasksAdapter; // adapter for all tasks
    private SimpleCursorAdapter allTasksAdapter2;
    public static final int ADD_TASK_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_tasks_listview);

        // custom font
//        TextView txt = (TextView) findViewById(R.id.task_title);
//        Typeface font = Typeface.createFromAsset(getAssets(), "CaviarDreams.ttf");
//        txt.setTypeface(font);

        allTasksListView = (ListView) findViewById(R.id.ListView_all_tasks);
        allTasksListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                taskItemClickHandler(arg0, arg1, arg2);
            }
        });

        allTasksListView2 = (ListView) findViewById(R.id.ListView2_all_tasks);
        allTasksListView2.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                taskItemClickHandler(arg0, arg1, arg2);
            }
        });

        // connect to the database
        database = new Database(this);
        database.open();

        loadTasksIntoListView();

    }

    // load all Tasks from database and put them into the ListView
    public void loadTasksIntoListView() {

        if (this.database != null) {

            // Get all Tasks
            allTasksCursor = database.getAllTasks();
            startManagingCursor(allTasksCursor);

            // Get data from which column
            String[] from = new String[]{
                    Database.TASK_TABLE_COLUMN_TITLE
            };

            // Put data to which components in layout
            int[] to = new int[]{
                    R.id.task_title
            };

            // repeat previous steps for displaying priority level
            String[] from2 = new String[]{
                    Database.TASK_TABLE_COLUMN_PRIORITY
            };

            int[] to2 = new int[]{
                    R.id.priority_level_display
            };

            // initialize the custom adapter for the ListView
            allTasksAdapter = new CustomTaskAdapter(this,
                    R.layout.activity_view_task_item, allTasksCursor, from, to);

            allTasksAdapter2 = new CustomTaskAdapter(this,
                    R.layout.activity_view_task_item, allTasksCursor, from2, to2);

            allTasksAdapter2.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Cursor cursor, int column) {

                    String index = allTasksCursor.getString(column);
                    String textToDisplay;
                    int color;

                    TextView tv = (TextView) view;

                    if (index.equals("0")) {
                        textToDisplay = "HIGH";
                        color = Color.parseColor("#d96679");
                    } else if (index.equals("1")) {
                        textToDisplay = "MEDIUM";
                        color = Color.parseColor("#ffc153");
                    } else {
                        textToDisplay = "LOW";
                        color = Color.parseColor("#2bbf98");
                    }

                    tv.setTextColor(color);
                    tv.setTypeface(null, Typeface.BOLD);
                    tv.setText(textToDisplay);
                    return true;
                }
            });

            // set the adapter for the ListViews
            this.allTasksListView.setAdapter(allTasksAdapter);
            this.allTasksListView2.setAdapter(allTasksAdapter2);

        }
    }

    private void taskItemClickHandler(AdapterView<?> adapterView, View listView, int selectedItemId) {

        Task selectedTask = new Task();

        allTasksCursor.moveToFirst();
        allTasksCursor.move(selectedItemId);

        // set data for the selected task
        selectedTask.setId(allTasksCursor.getString(allTasksCursor.getColumnIndex(Database.TASK_TABLE_COLUMN_ID)));
        selectedTask.setTitle(allTasksCursor.getString(allTasksCursor.getColumnIndex(Database.TASK_TABLE_COLUMN_TITLE)));
        selectedTask.getDueDate().setTimeInMillis(allTasksCursor.getLong(allTasksCursor.getColumnIndex(Database.TASK_TABLE_COLUMN_DUE_DATE)));
        selectedTask.setNote(allTasksCursor.getString(allTasksCursor.getColumnIndex(Database.TASK_TABLE_COLUMN_NOTES)));
        selectedTask.setPriorityLevel(allTasksCursor.getInt(allTasksCursor.getColumnIndex(Database.TASK_TABLE_COLUMN_PRIORITY)));
        selectedTask.setStatus(allTasksCursor.getInt(allTasksCursor.getColumnIndex(Database.TASK_TABLE_COLUMN_STATUS)));

        // pass on data to TaskDetailActivity and start that activity to display the data
        ActivityManager.viewTaskDetail(this, selectedTask);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionbar_add:
                ActivityManager.addNewTask(this, this.database);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_all_tasks, menu);
        return true;
    }

}
