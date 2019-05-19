package com.example.j.abandonedskiareas;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Jess on 11/29/16.
 */

//Used http://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/ for this - I had no idea what I was doing

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> dataHeader;
    private HashMap<String, List<String>> dataChild;
    public ExpandableListAdapter(Context context, List<String> dataHeader,HashMap<String, List<String>> dataChild){
        this.context = context;
        this.dataHeader = dataHeader;
        this.dataChild = dataChild;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition){
        return this.dataChild.get(this.dataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);
        if(convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item,null);
        }
        TextView childList = (TextView) convertView.findViewById(R.id.lblListItem);
        childList.setText(childText);
        return convertView;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if(convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }

        TextView listHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        listHeader.setTypeface(null, Typeface.BOLD);
        listHeader.setText(headerTitle);
        return convertView;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.dataHeader.get(groupPosition);
    }

    public boolean isChildSelectable(int groupPosition, int childPosition){
        return true;
    }

    public int getGroupCount() {
        return this.dataHeader.size();
    }
    public int getChildrenCount(int groupPosition) {
        return this.dataChild.get(this.dataHeader.get(groupPosition))
                .size();
    }
    public boolean hasStableIds() {
        return false;
    }
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

}
