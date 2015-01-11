package com.codepath.listly.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.codepath.listly.model.Database;
import com.codepath.listly.model.Task;
import com.codepath.listly.view.AllTasksActivity;
import com.codepath.listly.view.EditTaskActivity;
import com.codepath.listly.view.TaskDetailActivity;

/**
 * Created by paulina.
 */

public class ActivityManager {

	public static void viewTaskDetail(Activity sourceActivity, Task task){
		// 1. create new intent
		// 2. put Task into bundle
        // 3. put bundle into intent's extra
        // 4. launch the intent
		Intent viewTaskDetailIntent = new Intent(sourceActivity, TaskDetailActivity.class);
		Bundle viewTaskDetailBundle = new Bundle();
		viewTaskDetailBundle.putSerializable(Task.TASK_EXTRA_KEY, task);
		viewTaskDetailIntent.putExtras(viewTaskDetailBundle);
		sourceActivity.startActivity(viewTaskDetailIntent);
	}

	public static void editExistingTask(Activity sourceActivity, Task existingTask){
		Intent editExistingTaskIntent = new Intent(sourceActivity, EditTaskActivity.class);
		Bundle editExistingTaskBundle = new Bundle();
		editExistingTaskBundle.putSerializable(Task.TASK_EXTRA_KEY, existingTask);
		editExistingTaskIntent.putExtras(editExistingTaskBundle);
		sourceActivity.startActivityForResult(editExistingTaskIntent, TaskDetailActivity.EDIT_TASK_REQUEST_CODE);
	}

	public static void addNewTask(Activity sourceActivity, Database database){
        Intent addNewTaskIntent = new Intent(sourceActivity, EditTaskActivity.class);
        sourceActivity.startActivityForResult(addNewTaskIntent, AllTasksActivity.ADD_TASK_REQUEST_CODE);
    }
}
