package com.example.todolist;


import database.TaskContract;

import database.TaskDBHelper;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import database.TaskContract;
import database.TaskDBHelper;

public class MainActivity extends ListActivity {

	private TaskDBHelper helper;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_login);
		updateUI();
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
	    getMenuInflater().inflate(R.menu.menu,menu);
	    return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.action_add_task:
	    	 
	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setTitle("Add a task");
	        builder.setMessage("Fill in the task details");
	       // final EditText inputField = new EditText(this);
	        LinearLayout layout = new LinearLayout(this);
	        layout.setOrientation(LinearLayout.VERTICAL);

	        final EditText titleBox = new EditText(this);
	        titleBox.setHint("Thing to be done");
	        layout.addView(titleBox);

	        final EditText descriptionBox = new EditText(this);
	        descriptionBox.setHint("Date of execution");
	        layout.addView(descriptionBox);

	        builder.setView(layout);
	        
	        
	        
	        
	        
	        
	        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialogInterface, int i) {
	            	String task = titleBox.getText().toString()+"\n"+descriptionBox.getText().toString();
	            	Log.d("MainActivity",task);
	            	 
	            	TaskDBHelper helper = new TaskDBHelper(MainActivity.this);
	            	SQLiteDatabase db = helper.getWritableDatabase();
	            	ContentValues values = new ContentValues();
	            	 
	            	values.clear();
	            	values.put(TaskContract.Columns.TASK,task);
	            	 
	            	db.insertWithOnConflict(TaskContract.TABLE,null,values,
	            	                                SQLiteDatabase.CONFLICT_IGNORE);
	            	updateUI();
	                
	            }
    
	        });
	        
	        builder.setNegativeButton("Cancel",null);
	     
	        builder.create().show();
	        return true;
	 
	        default:
	            return false;
	    }
}
	public void onDoneButtonClick(View view) {
	    View v = (View) view.getParent();
	    TextView taskTextView = (TextView) v.findViewById(R.id.taskTextView);
	    String task = taskTextView.getText().toString();
	 
	    String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
	                    TaskContract.TABLE,
	                    TaskContract.Columns.TASK,
	                    task);
	 
	 
	    helper = new TaskDBHelper(MainActivity.this);
	    SQLiteDatabase sqlDB = helper.getWritableDatabase();
	    sqlDB.execSQL(sql);
	    updateUI();
	}
	private void updateUI() {
	    helper = new TaskDBHelper(MainActivity.this);
	    SQLiteDatabase sqlDB = helper.getReadableDatabase();
	    Cursor cursor = sqlDB.query(TaskContract.TABLE,
	            new String[]{TaskContract.Columns._ID, TaskContract.Columns.TASK},
	            null,null,null,null,null);
	 
	    ListAdapter listAdapter = new SimpleCursorAdapter(
	            this,
	            R.layout.task_view,
	            cursor,
	            new String[] { TaskContract.Columns.TASK},
	            new int[] { R.id.taskTextView},
	            0
	    );
	    this.setListAdapter(listAdapter);
	}
}
