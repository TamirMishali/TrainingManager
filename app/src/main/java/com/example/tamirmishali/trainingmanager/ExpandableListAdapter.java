package com.example.tamirmishali.trainingmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tamirmishali.trainingmanager.Exercise.Exercise;
import com.example.tamirmishali.trainingmanager.Exercise.ExerciseViewModel;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstractViewModel;
import com.example.tamirmishali.trainingmanager.Set.Set;
import com.example.tamirmishali.trainingmanager.Set.SetViewModel;
import com.example.tamirmishali.trainingmanager.Workout.Workout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

//dictionary
// 0.0 = in view : 0.0   - didn't do it - normal number
//-1.0 = in view : empty - new set, was not inserted with value yet
//-2.0 = in view : N/A   - when prev set doesn't exist but current set is.

//Prev      Curr    Comments
//X         X       Doesn't exist.
//X         V       p.start : View=N/A, Val=-2.0    | c.start : View=empty, Val=-1.0
//                  p.end   : View=N/A, Val=-2.0    | c.end   : View=num,   Val=num
//V         X       Delete Set from (prev - Hash, current - Hash & DB)
//V         V       p.start : View=num1, Val=num1    | c.start : View=empty, Val=-1.0
//                  p.end   : View=num1, Val=num1    | c.end   : View=num2,  Val=num2


//TO:DO
//1. make added sets appear at the moment they are added
//3. delete set logic

public class ExpandableListAdapter extends BaseExpandableListAdapter{

    private static final String TAG = "ExpandableListAdapter";

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<Set>> _listDataChildPrev;
    private HashMap<String, List<Set>> _listDataChildCurrent;
    private SetViewModel setViewModel;
    private ExerciseAbstractViewModel exerciseAbstractViewModel;
    private ExerciseViewModel exerciseViewModel;
    private final Workout currentWorkout, prevWorkout;

    // source - https://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/
    public ExpandableListAdapter(Context context, Workout currentWorkout, Workout prevWorkout,/*List<String> listDataHeader,
                                 HashMap<String, List<Set>> listChildDataPrev,
                                 HashMap<String, List<Set>> listChildDataCurrent,*/
                                 SetViewModel setViewModel,
                                     ExerciseViewModel exerciseViewModel,
                                     ExerciseAbstractViewModel exerciseAbstractViewModel) {
        this._context = context;
        this.currentWorkout = currentWorkout;
        this.prevWorkout = prevWorkout;

        this._listDataHeader = getExNames(currentWorkout.getExercises());
        this._listDataChildPrev = fillDataChildPrev(prevWorkout,currentWorkout);
        this._listDataChildCurrent = fillDataChildCurrent(currentWorkout);

        this.setViewModel = setViewModel;
        this.exerciseAbstractViewModel = exerciseAbstractViewModel;
        this.exerciseViewModel = exerciseViewModel;

    }

    private HashMap<String,List<Set>> fillDataChildCurrent(Workout currentWorkout) {
        HashMap<String,List<Set>> dataChildCurrent = new HashMap<>();
        for(int i=0 ; i<currentWorkout.getExercises().size(); i++){
            dataChildCurrent.put(currentWorkout.getExercises().get(i).getExerciseAbstract().generateExerciseAbstractName(),
                    currentWorkout.getExercises().get(i).getSets());
        }
        return dataChildCurrent;

    }

    private HashMap<String,List<Set>> fillDataChildPrev(Workout prevWorkout, Workout currentWorkout) {
        HashMap<String,List<Set>> dataChildPrev = new HashMap<>();
        for(int i=0 ; i<prevWorkout.getExercises().size(); i++){ //for(int i=0 ; i<prevWorkout.getExercises().size(); i++){
            if(isExerciseInExerciseList(prevWorkout.getExercises().get(i),currentWorkout.getExercises())){
                //if exists in current Workout then insert to prev dataChild +
                //if prev sets has less sets than current sets
                List<Set> prevSets = prevWorkout.getExercises().get(i).getSets();
                List<Set> currentSets = currentWorkout.getExercises().get(i).getSets();

                if (prevSets.size() < currentSets.size()){
                     final int diff = currentSets.size()-prevSets.size();

                    for (int j=0; j<diff; j++){
                        Set newSet = new Set(0,prevWorkout.getExercises().get(i).getId(),-2.0,-2);
                        prevSets.add(newSet);
                    }
                }

                dataChildPrev.put(prevWorkout.getExercises().get(i).getExerciseAbstract().generateExerciseAbstractName(),
                        prevWorkout.getExercises().get(i).getSets());
            }
        }
        return dataChildPrev;
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


    //https://stackoverflow.com/questions/10120212/how-to-determine-if-an-input-in-edittext-is-an-integer
    public Boolean validSetData(String repsWeight){
        Boolean valid = Boolean.FALSE;

        try {
            Double repsI = Double.parseDouble(repsWeight);
            Log.i("",repsWeight+" is a number");
            valid = Boolean.TRUE;
        } catch (NumberFormatException e) {
            Log.i("",repsWeight+" is not a number");
        }

        return valid;
    }


    public String numToStrView(double num) {
        String strView;

        if(num == -2.0)
            strView = "N/A";
        else if (num == -1.0)
            strView = "";
        else
            strView = Double.toString(num);

        return strView.replace(".0","");
    }


    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        //final Set childSet = getChild(groupPosition,childPosition);
        final List<Set> childSet = getChild(groupPosition,childPosition);
        //final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.workoutnow_list_item_expandableview, null);
        }

/*        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItemEditextPrevReps);*/

        final EditText editTextWeight = convertView
                .findViewById(R.id.lblListItemEditextNowWeight);
        final EditText editTextReps = convertView
                .findViewById(R.id.lblListItemEditextNowReps);

        TextView txtListChildWeight = convertView
                .findViewById(R.id.lblListItemEditextPrevWeight);
        TextView txtListChildReps = convertView
                .findViewById(R.id.lblListItemEditextPrevReps);

        ImageButton deleteImageButton = convertView.findViewById(R.id.delete_button_list_item);
        deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(_context);
                alert.setTitle(R.string.delete_entry_dialog_title);
                alert.setMessage(R.string.delete_set_dialog);
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        Set appendingDeleteSet = _listDataChildCurrent.get(_listDataHeader.get(groupPosition)).get(childPosition);
                        _listDataChildPrev.get(_listDataHeader.get(groupPosition)).remove(childPosition);
                        _listDataChildCurrent.get(_listDataHeader.get(groupPosition)).remove(childPosition);
                        setViewModel.delete(appendingDeleteSet);
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

        ImageButton duplicateSetImageButton = convertView.findViewById(R.id.arrow_forward_button_duplicate_set);
        duplicateSetImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Set destDuplicatedSet = _listDataChildCurrent.get(_listDataHeader.get(groupPosition)).get(childPosition);
                Set srcDuplicatedSet = _listDataChildPrev.get(_listDataHeader.get(groupPosition)).get(childPosition);
                destDuplicatedSet.setWeight(srcDuplicatedSet.getWeight());
                destDuplicatedSet.setReps(srcDuplicatedSet.getReps());
                setViewModel.update(destDuplicatedSet);
                notifyDataSetChanged();

            }
        });

        //https://stackoverflow.com/questions/10627137/how-can-i-know-when-an-edittext-loses-focus
        editTextWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (validSetData(editTextWeight.getText().toString())){

                        //save set
                        //this returns relevant set
                        //if delete set while edit text has focus on gui, then code can't obtain
                        //set cuz it doesn't exists. it was just deleted and then the onFocusChanged
                        //flag gets up.
                        if (childPosition < _listDataChildCurrent.get(_listDataHeader.get(groupPosition)).size()){
                            Set editedSet =_listDataChildCurrent.get(_listDataHeader.get(groupPosition)).get(childPosition);
                            editedSet.setWeight(Double.valueOf(editTextWeight.getText().toString()));
                            setViewModel.update(editedSet);
                        }
                    }
                }
                else{
                    editTextWeight.setSelectAllOnFocus(true);
                }
            }
        });

        editTextReps.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (validSetData(editTextReps.getText().toString())){

                        //save set
                        //this returns relevant set
                        if (childPosition < _listDataChildCurrent.get(_listDataHeader.get(groupPosition)).size()) {
                            Set editedSet = _listDataChildCurrent.get(_listDataHeader.get(groupPosition)).get(childPosition);
                            editedSet.setReps(Integer.valueOf(editTextReps.getText().toString()));
                            setViewModel.update(editedSet);
                        }
                    }
                    else{
                        editTextReps.setSelectAllOnFocus(true);
                    }
                }
            }
        });

        txtListChildWeight.setText(numToStrView(childSet.get(0).getWeight()));
        txtListChildReps.setText(numToStrView((double)childSet.get(0).getReps()));

        editTextWeight.setText(numToStrView(childSet.get(1).getWeight()));
        editTextReps.setText(numToStrView((double)childSet.get(1).getReps()));


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChildPrev.get(this._listDataHeader.get(groupPosition)).size();
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


        TextView lblListHeader = convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        ImageButton addImageButton = convertView.
                findViewById(R.id.add_button_group_item);
        addImageButton.setFocusable(Boolean.FALSE);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create new set and insert it to DB and then array
                Exercise exercise = getExerciseFromName(headerTitle, currentWorkout.getExercises());
                Log.v(TAG, "set size" +String.valueOf(exercise.getSets().size()));
                Workout workout = currentWorkout;
                Set newSet = new Set(exercise.getId(),-1.0,-1);
                setViewModel.insert(newSet);
                exercise.setSets(setViewModel.getSetsForExercise(exercise.getId()));
                _listDataChildPrev = fillDataChildPrev(prevWorkout,currentWorkout);
                _listDataChildCurrent = fillDataChildCurrent(currentWorkout);



/*                Log.v(TAG, "set size" +String.valueOf(exercise.getSets().size()));
                _listDataChildCurrent.clear();
                _listDataChildCurrent = fillDataChildCurrent(currentWorkout);*/
                //ExpandableListAdapter.this._listDataChildCurrent.put(exercise.getExerciseAbstract().getName(),setViewModel.getSetsForExercise(exercise.getId()));
                //_listDataChildCurrent.put(exercise.getExerciseAbstract().getName(),setViewModel.getSetsForExercise(exercise.getId()));
                //ExpandableListAdapter.this.notifyDataSetInvalidated();//notifyDataSetChanged();
                //notifyDataSetChanged();
                ExpandableListAdapter.this.notifyDataSetChanged();
                //_listDataChildCurrent.get(headerTitle).add();

                Toast.makeText(_context, "Set added to " + exercise.getExerciseAbstract().generateExerciseAbstractName(), Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }


    private Exercise getExerciseFromName(String exerciseName, List<Exercise> exerciseList) {
        Exercise exercise = new Exercise();
        for(int i=0; i<exerciseList.size();i++){
            if(exerciseName == exerciseList.get(i).getExerciseAbstract().generateExerciseAbstractName()) return exerciseList.get(i);
        }
        return exercise;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private List<String> getExNames(List<Exercise> exercises) {
        List<String> headers = new ArrayList<>();
        for(int i=0; i<exercises.size(); i++){
            headers.add(exercises.get(i).getExerciseAbstract().generateExerciseAbstractName());
        }
        return headers;
    }


    //@NonNull//this is a marker, does nothing but telling it can never be null
    private Boolean isExerciseInExerciseList(Exercise exercise, List<Exercise> exerciseList){
        Iterator<Exercise> iterator = exerciseList.iterator();
        //Exercise currentExercise;

        while (iterator.hasNext()) {
            //currentExercise = iterator.next();
            if (exercise.getId_exerciseabs() == iterator.next().getId_exerciseabs()/*currentExercise.getId_exerciseabs()*/)
                return true;
        }
        return false;
    }

}
