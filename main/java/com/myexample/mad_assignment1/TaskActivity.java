package com.myexample.mad_assignment1;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskActivity extends Fragment {

    private TextView tvTitle;
    private ImageView btnAddTask;
    private ExpandableListView elvTask;
    private DatabaseManager myDbManager;
    private ArrayList<String> groups;
    private HashMap<String, ArrayList<ToDoTask>> listHashMap;
    private ArrayList<ToDoTask> taskList_completed;
    private ArrayList<ToDoTask> taskList_not_completed;
    private Intent intent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvTitle = view.findViewById(R.id.tvTaskList);
        btnAddTask = view.findViewById(R.id.imgAddTask);
        elvTask = view.findViewById(R.id.elvTask);

        taskList_completed = new ArrayList<ToDoTask>();
        taskList_not_completed = new ArrayList<ToDoTask>();
        listHashMap = new HashMap<String, ArrayList<ToDoTask>>();
        myDbManager = new DatabaseManager(getActivity());

        getList();

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getActivity(), TaskDetailActivity.class);
                startActivity(intent);
            }
        });

        elvTask.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                ToDoTask t = listHashMap.get(groups.get(groupPosition)).get(childPosition);
                intent = new Intent(getActivity(), TaskDetailActivity.class);
                intent.putExtra("tId", t.getTaskId());
                startActivity(intent);
                return false;
            }
        });
    }

    public void getList(){
        myDbManager.openReadable();

        ArrayList<ToDoTask> taskList = myDbManager.getAllTasks();

        for(int i = 0; i < taskList.size(); i++){
            ToDoTask task = taskList.get(i);
            if(task.getIsCompleted() == 0){
                taskList_not_completed.add(task);
            } else if (task.getIsCompleted() == 1) {
                taskList_completed.add(task);
            }
        }

        groups = new ArrayList<String>();
        groups.add("Not Completed");
        groups.add("Completed");

        listHashMap.put(groups.get(0), taskList_not_completed);
        listHashMap.put(groups.get(1), taskList_completed);

        final TaskAdapter arrayAdpt = new TaskAdapter(getActivity(), groups, listHashMap);
        elvTask.setAdapter(arrayAdpt);
        tvTitle.setText("To-Do-Task (" + taskList.size() + ")");

    }
}
