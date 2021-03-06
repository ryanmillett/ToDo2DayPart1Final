package edu.orangecoastcollege.cs273.todo2day;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private List<Task> allTasksList = new ArrayList<>();

    // Reference to the database:
    private DBHelper mDB;
    // Reference to the widgets needed
    private EditText mDescriptionEditText;
    private ListView mTaskListView;
    // Reference to custom list adapter
    TaskListAdapter mTaskListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDB = new DBHelper(this);
        mDescriptionEditText = (EditText) findViewById(R.id.taskEditText);
        mTaskListView = (ListView) findViewById(R.id.taskListView);


//        deleteDatabase(DBHelper.DATABASE_NAME);
//
//        allTasksList.add(new Task("Study for CS A273 Midterm", false));
//        allTasksList.add(new Task("Get that laundry done!", false));
//        allTasksList.add(new Task("Master explicit intents", true));
//        allTasksList.add(new Task("Play League of Legends", true));
//
//        DBHelper db = new DBHelper(this);
//        for (Task t : allTasksList)
//            db.addTask(t);
//
//        allTasksList = db.getAllTasks();
//        Log.i(TAG, "Displaying all Tasks after adding:");
//        for (Task t : allTasksList)
//            Log.i(TAG, t.toString());
//
//        db.deleteTask(allTasksList.get(2));
//        db.deleteTask(allTasksList.get(3));
//        allTasksList = db.getAllTasks();
//        Log.i(TAG, "Displaying all Tasks after deleting T3 and T4:");
//        for (Task t : allTasksList)
//            Log.i(TAG, t.toString());
//
//
//        Task updatedTask = allTasksList.get(0);
//        updatedTask.setDescription("Pretend to study for CS A273 Midterm");
//        updatedTask.setIsDone(true);
//        db.updateTask(updatedTask);
//
//        allTasksList = db.getAllTasks();
//        Log.i(TAG, "Displaying all Tasks after updating T1:");
//        for (Task t : allTasksList)
//            Log.i(TAG, t.toString());
//
//
//        Task t2 = db.getSingleTask(2);
//        Log.i(TAG, "Displaying Task T2:");
//        Log.i(TAG, t2.toString());
//
//        db.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Database related stuff:
        // 1) Populate the list from the database (using DBHelper)
        allTasksList = mDB.getAllTasks();
        // 2) Connect the ListView with the custom list adapter
        mTaskListAdapter = new TaskListAdapter(this, R.layout.task_item, allTasksList);
        mTaskListView.setAdapter(mTaskListAdapter);
    }

    public void addTask(View v) {
        // Check to see if the description is empty or null
        String description = mDescriptionEditText.getText().toString();
        if (TextUtils.isEmpty(description))
            Toast.makeText(this, "Please enter a description", Toast.LENGTH_LONG).show();
        else {
            // Create the task
            Task newTask = new Task(description, false);
            // Add it to the database
            mDB.addTask(newTask);
            // Add it to the List
            allTasksList.add(newTask);
            // Notify the list adapter that it has been changed
            mTaskListAdapter.notifyDataSetChanged();
            // Clear out the EditText
            mDescriptionEditText.setText("");
        }
    }

    public void clearAllTasks(View v) {
        mDB.deleteAllTasks();
        allTasksList.clear();
        mTaskListAdapter.notifyDataSetChanged();
    }

    public void changeTaskStatus(View v) {
        CheckBox selectedCheckBox = (CheckBox) v;
        Task selectedTask = (Task) selectedCheckBox.getTag();
        // Update the task
        selectedTask.setIsDone(selectedCheckBox.isChecked());
        // Update the database
        mDB.updateTask(selectedTask);
    }
}
