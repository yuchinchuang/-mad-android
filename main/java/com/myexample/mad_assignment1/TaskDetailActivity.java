package com.myexample.mad_assignment1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class TaskDetailActivity extends AppCompatActivity {

    private TextView tvTitle;
    private EditText etTName;
    private EditText etLocation;
    private Spinner spStatus;
    private TableRow tblRow_status;
    private Button btnAddNewTask;
    private Button btnBackToList;
    private Button btnUpdateTask;
    private DatabaseManager myDbManager;
    private int taskId;
    private ToDoTask task;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        tvTitle = findViewById(R.id.tvTitle_task);
        etTName = findViewById(R.id.etTName);
        etLocation = findViewById(R.id.etTLocation);
        spStatus = findViewById(R.id.spStatus);
        tblRow_status = findViewById(R.id.tblRow_taskStatus);
        btnAddNewTask = findViewById(R.id.btnAddNewTask);
        btnBackToList = findViewById(R.id.btnBackToList);
        btnUpdateTask = findViewById(R.id.btnUpdateTask);

        myDbManager = new DatabaseManager(TaskDetailActivity.this);

        intent = getIntent();
        taskId = intent.getIntExtra("tId", -1);

        if (taskId == -1){
            tblRow_status.setVisibility(View.GONE);
            btnUpdateTask.setVisibility(View.GONE);
            btnAddNewTask.setVisibility(View.VISIBLE);
            tvTitle.setText("Add To-Do-Task");
            btnBackToList.setText("Cancel");
        } else {
            tblRow_status.setVisibility(View.VISIBLE);
            btnUpdateTask.setVisibility(View.VISIBLE);
            btnAddNewTask.setVisibility(View.GONE);
            tvTitle.setText("Edit To-Do-Task");
            btnBackToList.setText("Back to List");

            task = myDbManager.getTaskRecord(taskId);
            etTName.setText(task.getTaskName());
            etLocation.setText(task.gettLocation());
            spStatus.setSelection(task.getIsCompleted());
        }

        btnAddNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(String.valueOf(etTName.getText()).length() != 0){
                    ToDoTask t = new ToDoTask();
                    t.setTaskName(String.valueOf(etTName.getText()));
                    t.settLocation(String.valueOf(etLocation.getText()));

                    myDbManager.addTask(t);
                    BackToList();
                } else {
                    Toast.makeText(TaskDetailActivity.this,
                            "Task name cannot be empty!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        btnBackToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackToList();
            }
        });

        btnUpdateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(String.valueOf(etTName.getText()).length()!= 0){
                    ToDoTask t = new ToDoTask();

                    t.setTaskId(taskId);
                    t.setTaskName(String.valueOf(etTName.getText()));
                    t.settLocation(String .valueOf(etLocation.getText()));
                    int isComplete = 0;
                    if (String.valueOf(spStatus.getSelectedItem()).equalsIgnoreCase("complete")){
                        isComplete = 1;
                    }
                    t.setCompleted(isComplete);
                    myDbManager.updateTask(t);
                    BackToList();
                } else {
                    Toast.makeText(TaskDetailActivity.this,
                            "Task name cannot be empty!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void BackToList(){
        intent = new Intent(TaskDetailActivity.this, MainActivity.class);
        intent.putExtra("fragment", "task");
        startActivity(intent);
    }
}
