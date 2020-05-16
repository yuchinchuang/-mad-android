package com.myexample.mad_assignment1;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listGroup;
    private HashMap<String, ArrayList<ToDoTask>> listHashMap;

    public TaskAdapter(@NonNull Context context, List<String> listGroup,
                        HashMap<String, ArrayList<ToDoTask>> listHashMap) {

        this.context = context;
        this.listGroup = listGroup;
        this.listHashMap = listHashMap;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listHashMap.get(this.listGroup.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final ToDoTask task = (ToDoTask) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.rowlayout_task_child, null);
        }

        TextView tvTName = (TextView) convertView
                .findViewById(R.id.tvTName);
        TextView tvTLocation = (TextView) convertView
                .findViewById(R.id.tvTLocation);

        tvTName.setText(task.getTaskName());
        tvTLocation.setText(task.gettLocation());

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listHashMap.get(this.listGroup.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listGroup.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listGroup.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.rowlayout_task_parent, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.tvGroup);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle + "(" + getChildrenCount(groupPosition) + ")");

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
