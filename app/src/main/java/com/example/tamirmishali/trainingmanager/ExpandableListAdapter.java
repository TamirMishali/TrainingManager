package com.example.tamirmishali.trainingmanager;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.media.Image;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tamirmishali.trainingmanager.Set.Set;
import com.example.tamirmishali.trainingmanager.Set.SetViewModel;
import com.example.tamirmishali.trainingmanager.Workout.EditWorkouts;


public class ExpandableListAdapter extends BaseExpandableListAdapter{

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<Set>> _listDataChild;
    private SetViewModel _setViewModel;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<Set>> listChildData,SetViewModel setViewModel) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this._setViewModel = setViewModel;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Set childSet = (Set) getChild(groupPosition,childPosition);
        //final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.workoutnow_list_item_expandableview, null);
        }

/*        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItemEditextPrevReps);*/

        EditText editTextWeight = (EditText) convertView
                .findViewById(R.id.lblListItemEditextNowWeight);
        EditText editTextReps = (EditText) convertView
                .findViewById(R.id.lblListItemEditextNowReps);

        editTextWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        TextView txtListChildWeight = (TextView) convertView
                .findViewById(R.id.lblListItemEditextPrevWeight);
        TextView txtListChildReps = (TextView) convertView
                .findViewById(R.id.lblListItemEditextPrevReps);

        ImageButton deleteImageButton = (ImageButton) convertView.findViewById(R.id.delete_button_list_item);
        deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(_context);
                alert.setTitle(R.string.delete_entry_dialog_title);
                alert.setMessage(R.string.delete_workout_dialog);
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        _listDataChild.get(_listDataHeader.get(groupPosition)).remove(childPosition);
                        ExpandableListAdapter.this.notifyDataSetChanged();

                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // close dialog
                        dialog.cancel();

                        //https://stackoverflow.com/questions/31787272/android-recyclerview-itemtouchhelper-revert-swipe-and-restore-view-holder
                        ExpandableListAdapter.this.notifyDataSetChanged();

                    }
                });
                alert.show();

            }
        });

        /*txtListChild.setText(childText);*/
        txtListChildWeight.setText(String.valueOf(childSet.getWeight()));
        txtListChildReps.setText(String.valueOf(childSet.getReps()));


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
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
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.workoutnow_list_group_expandableview, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        ImageButton addImageButton = (ImageButton) convertView.
                findViewById(R.id.add_button_group_item);
        addImageButton.setFocusable(Boolean.FALSE);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

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
