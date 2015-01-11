package com.codepath.listly.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.listly.R;
import com.codepath.listly.presenter.ConfirmActionDialog;
import com.codepath.listly.model.Database;
import com.codepath.listly.model.Task;

import java.util.Calendar;

/**
 * Created by paulina.
 */

// if a Task exists, this activity will edit it. Else, this activity will create a new Task
public class EditTaskActivity extends Activity {

	private Task task = null;
	private int userAction;
	private final int USER_ACTION_EDIT = 1;
	private final int USER_ACTION_ADD = 2;
	private Database database;

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent resultIntent;
		Bundle resultBundle;

		switch (item.getItemId()) {

            case R.id.actionbar_cancel:

            case android.R.id.home:
                // set the result for the previous activity
                resultIntent = new Intent();
                resultBundle = new Bundle();
                resultBundle.putSerializable(Task.TASK_EXTRA_KEY, this.task);
                resultIntent.putExtras(resultBundle);
                setResult(TaskDetailActivity.EDIT_TASK_REQUEST_CODE, resultIntent);

                ConfirmActionDialog.showConfirmCancelDialog(this);
                return true;

            case R.id.actionbar_save:

                if (this.userAction == USER_ACTION_ADD) {
                    addNewTaskToDB();
                } else {
                    editExistingTask();
                    resultIntent = new Intent();
                    resultBundle = new Bundle();
                    resultBundle.putSerializable(Task.TASK_EXTRA_KEY, this.task);
                    resultIntent.putExtras(resultBundle);
                    setResult(TaskDetailActivity.EDIT_TASK_REQUEST_CODE, resultIntent);
                }

                finish(); // close the activity
                return true;

            default:
                return super.onOptionsItemSelected(item);
            }
	}

	@Override
	public void onBackPressed() {

		Intent resultIntent = new Intent();
		Bundle resultBundle = new Bundle();
		resultBundle.putSerializable(Task.TASK_EXTRA_KEY, this.task);
		resultIntent.putExtras(resultBundle);
		setResult(TaskDetailActivity.EDIT_TASK_REQUEST_CODE, resultIntent);

		ConfirmActionDialog.showConfirmCancelDialog(this);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_task);

		// connect to the database
		database = new Database(this);
		database.open();

		// retrieve Task object from Intent's passed in extras
		Bundle modifyTaskBundle = this.getIntent().getExtras();
		try {
			this.task = (Task) modifyTaskBundle.getSerializable(Task.TASK_EXTRA_KEY);
		} catch (Exception ex){
			ex.printStackTrace();
		}

		if (task != null){
			this.userAction = this.USER_ACTION_EDIT;
			loadTaskDataOnForm(); // retrieve data from Task and load it onto form fields
		} else {
			this.task = new Task(); 			// if Task doesn't exist, create it
			this.userAction = this.USER_ACTION_ADD;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.modify_task, menu);
		return true;
	}

	private void editExistingTask() {
		loadNewDataOntoTask();
		database.editExistingTask(this.task); // database needs to update its information about the Task
	}

	private void loadNewDataOntoTask(){

		// retrieve data from input fields and put into Task object

		String taskTitle = ((EditText)findViewById(R.id.edit_text_task_title)).getText().toString();
		this.task.setTitle(taskTitle);

		DatePicker taskDueDatePicker = (DatePicker) findViewById(R.id.date_picker_due_date);
		this.task.getDueDate().set(Calendar.DATE, taskDueDatePicker.getDayOfMonth());
		this.task.getDueDate().set(Calendar.MONTH, taskDueDatePicker.getMonth());
		this.task.getDueDate().set(Calendar.YEAR, taskDueDatePicker.getYear());

		String taskNote = ((EditText)findViewById(R.id.edit_text_note)).getText().toString();
		this.task.setNote(taskNote);

		int priorityLevel = ((Spinner)findViewById(R.id.spinner_priority_level)).getSelectedItemPosition();
		this.task.setPriorityLevel(priorityLevel);

		int status = ((Spinner)findViewById(R.id.spinner_status)).getSelectedItemPosition();
		this.task.setStatus(status);
	}

	private void addNewTaskToDB(){
		loadNewDataOntoTask();
		String taskId = database.getNewTaskId(); // set Task ID before adding Task to DB
		this.task.setId(taskId);
		this.database.insertTaskIntoDB(this.task);
	}

	private void loadTaskDataOnForm(){

		if (this.userAction == this.USER_ACTION_EDIT) {

            // retrieve data from Task object and put into form fields

			EditText taskTitleEditText = (EditText) findViewById(R.id.edit_text_task_title);
			taskTitleEditText.setText(this.task.getTitle());

			DatePicker taskDueDatePicker = (DatePicker) findViewById(R.id.date_picker_due_date);
			taskDueDatePicker.updateDate(this.task.getDueDate().get(Calendar.YEAR),
					this.task.getDueDate().get(Calendar.MONTH),
					this.task.getDueDate().get(Calendar.DATE));

			EditText taskNoteEditText = (EditText) findViewById(R.id.edit_text_note);
			taskNoteEditText.setText(this.task.getNotes());

			Spinner taskPriorityLevelSpinner = (Spinner) findViewById(R.id.spinner_priority_level);
			taskPriorityLevelSpinner.setSelection(this.task.getPriorityLevel());

			Spinner completionStatusSpinner = (Spinner) findViewById(R.id.spinner_status);
			completionStatusSpinner.setSelection(this.task.getStatus());
		}
	}

}
