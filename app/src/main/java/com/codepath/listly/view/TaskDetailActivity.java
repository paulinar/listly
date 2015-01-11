package com.codepath.listly.view;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.codepath.listly.R;
import com.codepath.listly.presenter.ActivityManager;
import com.codepath.listly.presenter.ConfirmActionDialog;
import com.codepath.listly.model.Database;
import com.codepath.listly.model.Task;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by paulina.
 */

public class TaskDetailActivity extends Activity {

	private Task task;
	public static final int EDIT_TASK_REQUEST_CODE = 1;
	private Database database;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_task_detail);

		// connect to the database
		database = new Database(this);
		database.open();

        // enable back button on action bar for returning to the list of tasks
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

		// retrieve Task object passed in as an Intent extra
		Bundle taskDetailBundle = this.getIntent().getExtras();
		try{
			this.task = (Task) taskDetailBundle.getSerializable(Task.TASK_EXTRA_KEY);
		} catch (Exception ex){
			ex.printStackTrace();
		}

		// pass Task's data onto the View
	    this.putDataIntoView();
	}

	private void putDataIntoView(){

		if (this.task == null) {
			this.finish();
		} else {

            // set all data (e.g. title, due date, notes)

			TextView taskTitleTextView = (TextView) findViewById(R.id.task_content);
			taskTitleTextView.setText(this.task.getTitle());

			TextView taskDueDateTextView = (TextView) findViewById(R.id.due_date_content);
			Calendar dueDate = this.task.getDueDate();
			String dueDateString = dueDate.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US) + " "
					+ dueDate.get(Calendar.DATE) + " "
					+ dueDate.get(Calendar.YEAR);
			taskDueDateTextView.setText(dueDateString);

			TextView taskNoteTextView = (TextView) findViewById(R.id.notes_content);
			taskNoteTextView.setText(this.task.getNotes());

			TextView priorityTextView = (TextView) findViewById(R.id.priority_level_content);
			String priorityString;

			switch (this.task.getPriorityLevel()) {
                case Task.HIGH_PRIORITY:
                    priorityString = this.getString(R.string.priority_level_high);
                    break;
                case Task.MEDIUM_PRIORITY:
                    priorityString = this.getString(R.string.priority_level_medium);
                    break;
                default:
                    priorityString = this.getString(R.string.priority_level_low);
                    break;
			}

			priorityTextView.setText(priorityString);

			TextView completionTextView = (TextView) findViewById(R.id.status_content);
			String completionString;
			if (this.task.getStatus() == Task.TASK_DONE) {
				completionString = getString(R.string.status_done);
			} else {
				completionString = getString(R.string.status_to_do);
			}
			completionTextView.setText(completionString);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.view_task_detail, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == EDIT_TASK_REQUEST_CODE) {
			this.task = (Task) data.getExtras().getSerializable(Task.TASK_EXTRA_KEY);
			this.putDataIntoView();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){ // hit back button to return to list of tasks
            case android.R.id.home:
                this.finish();
                return true;
		case R.id.actionbar_edit:
			ActivityManager.editExistingTask(this, this.task);
			return true;
		case R.id.actionbar_delete:
			ConfirmActionDialog.showConfirmDeleteDialogForTask(this, this.task, this.database);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
