package com.example.tamirmishali.trainingmanager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.tamirmishali.trainingmanager.Exercise.Exercise;
import com.example.tamirmishali.trainingmanager.Exercise.ExerciseViewModel;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstract;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstractViewModel;
import com.example.tamirmishali.trainingmanager.Routine.RoutineViewModel;
import com.example.tamirmishali.trainingmanager.Set.Set;
import com.example.tamirmishali.trainingmanager.Set.SetViewModel;
import com.example.tamirmishali.trainingmanager.Workout.Workout;


public class ExpandableListAdapter extends BaseExpandableListAdapter{

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<Set>> _listDataChildPrev;
    private HashMap<String, List<Set>> _listDataChildCurrent;
    private SetViewModel setViewModel;
    private ExerciseAbstractViewModel exerciseAbstractViewModel;
    private ExerciseViewModel exerciseViewModel;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<Set>> listChildDataPrev,
                                 HashMap<String, List<Set>> listChildDataCurrent,
                                 SetViewModel setViewModel,
                                 ExerciseViewModel exerciseViewModel,
                                 ExerciseAbstractViewModel exerciseAbstractViewModel) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChildPrev = listChildDataPrev;
        this._listDataChildCurrent = listChildDataCurrent;
        this.setViewModel = setViewModel;
        this.exerciseAbstractViewModel = exerciseAbstractViewModel;
        this.exerciseViewModel = exerciseViewModel;

    }

    @Override
    public List<Set> getChild(int groupPosition, int childPosition) {
        List<Set> objectList = new ArrayList<>();
        objectList.add((Set) _listDataChildPrev.get(this._listDataHeader.get(groupPosition))
                .get(childPosition));
        objectList.add((Set) _listDataChildCurrent.get(this._listDataHeader.get(groupPosition))
                .get(childPosition));
        return objectList;

    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final List<Set> childSet = getChild(groupPosition,childPosition);
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
                alert.setMessage(R.string.delete_set_dialog);
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        //_setViewModel.delete((_listDataChildPrev.get(_listDataHeader.get(groupPosition))).get(childPosition));
                        //don't forget to delete from DB too
                        _listDataChildPrev.get(_listDataHeader.get(groupPosition)).remove(childPosition);

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
        txtListChildWeight.setText(String.valueOf(childSet.get(0).getWeight()));
        txtListChildReps.setText(String.valueOf(childSet.get(0).getReps()));

        editTextWeight.setText(String.valueOf(childSet.get(1).getWeight()));
        editTextReps.setText(String.valueOf(childSet.get(1).getReps()));

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChildPrev.get(this._listDataHeader.get(groupPosition))
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
        final String headerTitle = (String) getGroup(groupPosition);
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
                //create new set and insert it to DB and then array

/*                Set newSet = new Set()
                _listDataChildPrev.get(headerTitle).add();*/
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

    private void updateChangedWorkoutInDB(Workout workout){

    }


}
