package com.example.tamirmishali.trainingmanager;

import static android.view.View.FOCUS_RIGHT;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.example.tamirmishali.trainingmanager.Exercise.Exercise;
import com.example.tamirmishali.trainingmanager.Exercise.ExerciseViewModel;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstract;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstractViewModel;
import com.example.tamirmishali.trainingmanager.Routine.RoutineViewModel;
import com.example.tamirmishali.trainingmanager.Set.Set;
import com.example.tamirmishali.trainingmanager.Set.SetViewModel;
import com.example.tamirmishali.trainingmanager.Workout.Workout;
import com.example.tamirmishali.trainingmanager.Workout.WorkoutViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

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

// Todo (22.01.2023):
//  - (MEDIUM) Rearrange the EA fields in a logical way that the probability of a pair of fields to be empty
//  are in a same line and then vanish the line
//  - (LOW) if there is no previous set data, keep it empty, not 'N/A'.
//

public class ExpandableListAdapter extends BaseExpandableListAdapter{

    private static final String TAG = "ExpandableListAdapter";

    private Context _context;
    private List<Integer> _listDataHeader; // header titles
    private HashMap<Integer, List<Set>> _listDataChildPrev;
    private HashMap<Integer, List<Set>> _listDataChildCurrent;
    private final SetViewModel setViewModel;
    private ExerciseAbstractViewModel exerciseAbstractViewModel;
    private ExerciseViewModel exerciseViewModel;
    private final Workout currentWorkout, prevWorkout;

    // source - https://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/
    public ExpandableListAdapter(Context context, Workout currentWorkout, Workout prevWorkout,
                                 SetViewModel setViewModel,
                                     ExerciseViewModel exerciseViewModel,
                                     ExerciseAbstractViewModel exerciseAbstractViewModel) {
        this._context = context;
        this.currentWorkout = currentWorkout;
        this.prevWorkout = prevWorkout;

        this.setViewModel = setViewModel;
        this.exerciseAbstractViewModel = exerciseAbstractViewModel;
        this.exerciseViewModel = exerciseViewModel;

        // get Exercise's ExerciseAbstract id's as the header of this ExpandableList:
        this._listDataHeader = getExercisesEAIds(currentWorkout.getExercises());

        this._listDataChildPrev = fillDataChildPrev(prevWorkout,currentWorkout);
        this._listDataChildCurrent = fillDataChildCurrent(currentWorkout);

        // Force the layout direction to be left-to-right
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            ((Activity) context).getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

    }

    private HashMap<Integer,List<Set>> fillDataChildCurrent(Workout currentWorkout) {
        HashMap<Integer,List<Set>> dataChildCurrent = new HashMap<>();
        List<Exercise> exerciseList = currentWorkout.getExercises();
        if (exerciseList == null || exerciseList.isEmpty())
            return dataChildCurrent;

        // Fill HashMap with EA id's and list of sets
        for (Exercise exercise : exerciseList) {
            exercise.setSets(setViewModel.getSetsForExercise(exercise.getId()));
            dataChildCurrent.put(exercise.getId_exerciseabs(), exercise.getSets());
        }

        return dataChildCurrent;
    }

    private HashMap<Integer,List<Set>> fillDataChildPrev(Workout prevWorkout, Workout currentWorkout) {
        HashMap<Integer,List<Set>> dataChildPrev = new HashMap<>();

        // go over all exercises in current workout:
        for (Exercise currentExercise : currentWorkout.getExercises()) {

            // get sets of current workout
            List<Set> currentSets = setViewModel.getSetsForExercise(currentExercise.getId());
            if (currentSets == null){
                currentSets = new ArrayList<>();
            }

            // get sets of prev workout
            List<Set> prevSets;

            // check if the exercise exists in prevWorkout:
            int prevExercise_index = 0;
            Exercise prevExercise;
            boolean exerciseFound = isExerciseInExerciseList(currentExercise, prevWorkout.getExercises());
            if(exerciseFound) {
                // if found, get the prevExercise index and then its Sets:
                prevExercise_index = indexOfExerciseInExerciseList(currentExercise, prevWorkout.getExercises());

                // get Exercise Sets
                prevExercise = prevWorkout.getExercises().get(prevExercise_index);
                prevSets = prevExercise.getSets();//setViewModel.getSetsForExercise(prevExercise.getId());
            }
            else{
                prevExercise= new Exercise();
                prevExercise.setId(0);
                prevSets = new ArrayList<>();
            }

            // make sure both current and prev has the same amount of sets to show in the adapter
            // if i have more sets in current than in prev, add empty sets to prev so it will
            //  be viewed correctly in child view:
            if (prevSets.size() < currentSets.size()){
                final int diff = currentSets.size()-prevSets.size();

                for (int j=0; j<diff; j++){
                    Set newSet = new Set(0,prevExercise.getId(),-2.0,-2);
                    prevSets.add(newSet);
                }
            }
            dataChildPrev.put(currentExercise.getId_exerciseabs(), prevSets);
        }
        return dataChildPrev;
    }


    @Override
    public List<Set> getChild(int groupPosition, int childPosition) {
        List<Set> objectList = new ArrayList<>();
        objectList.add((Set) Objects.requireNonNull(_listDataChildPrev.get(this._listDataHeader.get(groupPosition)))
                .get(childPosition));
        objectList.add((Set) Objects.requireNonNull(_listDataChildCurrent.get(this._listDataHeader.get(groupPosition)))
                .get(childPosition));

        return objectList;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // This part was not correct but no error was invoked.
        //  This caused no sets to be presented even tho they where in listData objects and in DB.
        Integer EA_id = this._listDataHeader.get(groupPosition);
        List<Set> sets = this._listDataChildCurrent.get(EA_id);
        if (sets == null)
            sets = new ArrayList<Set>();
        return sets.size();
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
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        // get List of 2 sets. i=0 is prev and i=1 is current:
        final List<Set> childSet = getChild(groupPosition,childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.workoutnow_list_item_expandableview, null);
        }

/*        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItemEditextPrevReps);*/
//        ImageView completedExercise = null;
//        if (parent != null)
//            completedExercise = (ImageView) parent.findViewById((R.id.completed_exercise_group_item));

        final EditText editTextWeight = (EditText) convertView
                .findViewById(R.id.lblListItemEditextNowWeight);
        final EditText editTextReps = (EditText) convertView
                .findViewById(R.id.lblListItemEditextNowReps);

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

        ImageButton duplicateSetImageButton = (ImageButton) convertView.findViewById(R.id.arrow_forward_button_duplicate_set);
        duplicateSetImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Set destDuplicatedSet = _listDataChildCurrent.get(_listDataHeader.get(groupPosition)).get(childPosition);
                Set srcDuplicatedSet = _listDataChildPrev.get(_listDataHeader.get(groupPosition)).get(childPosition);
                Log.d(TAG, "duplicateSetImageButton.onClick 1 Start. current weight is " + destDuplicatedSet.getWeight() + ".\nParent has focus=" + parent.hasFocus());
//                parent.requestFocus();
                //v.requestFocus();
//                Log.d(TAG, "duplicateSetImageButton.onClick 2 Parent request focus. \nHas focus=" + parent.hasFocus());
                destDuplicatedSet.setWeight(srcDuplicatedSet.getWeight());
//                Log.d(TAG, "duplicateSetImageButton.onClick 3 Duplicated weight");
                destDuplicatedSet.setReps(srcDuplicatedSet.getReps());
//                Log.d(TAG, "duplicateSetImageButton.onClick 4 Duplicated reps");
                setViewModel.update(destDuplicatedSet);
//                Log.d(TAG, "duplicateSetImageButton.onClick 5 Save set to DB");
                notifyDataSetChanged();
//                Log.d(TAG, "duplicateSetImageButton.onClick 6 notifyDataSetChanged triggered");
                //parent.requestFocus();
                //editTextWeight.clearFocus();
//                Log.d(TAG, "duplicateSetImageButton.onClick 7 Cleared focus triggered");
            }
        });

        //https://stackoverflow.com/questions/10627137/how-can-i-know-when-an-edittext-loses-focus
//        ImageView finalCompletedExercise = completedExercise;
        editTextWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Log.d(TAG, "editTextWeight.setOnFocusChangeListener lost focus." +
                            "\ngroupPosition=" + String.valueOf(groupPosition) +
                            "\nchildPosition=" + String.valueOf(childPosition));
                    String weightStr = editTextWeight.getText().toString();
                    double weightVal;
                    // if weight data is legit: not empty and valid double, convert string from view to double
                    // otherwise, insert the default number -1.0 and set weight view to be empty
                    if (!weightStr.isEmpty() && validSetData(weightStr))
                        weightVal = Double.parseDouble(weightStr);
                    else{
                        weightVal = -1.0;
                        // That caused the problem that i was unable to insert new data to weight and reps
                        //editTextWeight.setText("");
                    }

                    // Update set data in DB:
                    // the next if is responsible to avoid the scenario where a Set has been deleted
                    // and then trying to access it in the HashMap object after losing focus:
                    if (childPosition < _listDataChildCurrent.get(_listDataHeader.get(groupPosition)).size()){
                        Set editedSet =_listDataChildCurrent.get(_listDataHeader.get(groupPosition)).get(childPosition);
                        editedSet.setWeight(weightVal);
                        setViewModel.update(editedSet);
//                        _listDataChildCurrent.get(_listDataHeader.get(groupPosition)).set(childPosition, editedSet);
                    }

                }
//                else{
//                    editTextWeight.setSelectAllOnFocus(true);
//                }
            }
        });

//        ImageView finalCompletedExercise1 = completedExercise;
        editTextReps.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Log.d(TAG, "editTextReps.setOnFocusChangeListener lost focus." +
                            "\ngroupPosition=" + String.valueOf(groupPosition) +
                            "\nchildPosition=" + String.valueOf(childPosition));
                    String repStr = editTextReps.getText().toString();
                    int repVal;
                    // if rep data is legit: not empty, convert string from view to int.
                    // otherwise, insert the default number -1 and set rep view to be empty
                    if (!repStr.isEmpty() && validSetData(repStr))
                        repVal = Integer.parseInt(repStr);
                    else{
                        repVal = -1;
                        // That caused the problem that i was unable to insert new data to weight and reps
                        //editTextReps.setText("");
                    }

                    // Update set data in DB:
                    // the next if is responsible to avoid the scenario where a Set has been deleted
                    // and then trying to access it in the HashMap object after losing focus:
                    if (childPosition < _listDataChildCurrent.get(_listDataHeader.get(groupPosition)).size()){
                        Set editedSet =_listDataChildCurrent.get(_listDataHeader.get(groupPosition)).get(childPosition);
                        editedSet.setReps(repVal);
                        setViewModel.update(editedSet);
//                        _listDataChildCurrent.get(_listDataHeader.get(groupPosition)).set(childPosition, editedSet);
                    }

                }
                else if (hasFocus) {
                    editTextWeight.clearFocus(); // Clear focus from lblListItemEditextNowWeight
                }
            }
        });


/*        txtListChildWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Log.d(TAG, "txtListChildWeight.setOnFocusChangeListener lost focus." +
                            "\ngroupPosition=" + String.valueOf(groupPosition) +
                            "\nchildPosition=" + String.valueOf(childPosition));

                }
            }
        });

        txtListChildReps.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Log.d(TAG, "txtListChildReps.setOnFocusChangeListener lost focus." +
                            "\ngroupPosition=" + String.valueOf(groupPosition) +
                            "\nchildPosition=" + String.valueOf(childPosition));

                }
            }
        });*/

        txtListChildWeight.setText(numToStrView(childSet.get(0).getWeight()));
        txtListChildReps.setText(numToStrView((double)childSet.get(0).getReps()));

        editTextWeight.setText(numToStrView(childSet.get(1).getWeight()));
        editTextReps.setText(numToStrView((double)childSet.get(1).getReps()));

        // Log.d(TAG, "insert Set data into TextViews.");


        return convertView;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        Integer EA_id = (Integer) getGroup(groupPosition);
        ExerciseAbstract exerciseAbstract = exerciseAbstractViewModel.getExerciseAbsFromId(EA_id);
        String headerTitle = exerciseAbstract.generateExerciseAbstractName();

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.workoutnow_list_group_expandableview, null);
        }


        TextView lblListHeader = convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);


        // insert ExerciseAbstract extended information to extended_ea_info elements:
        TextView ea_load_type = convertView.findViewById(R.id.text_view_exerciseabs_load_type_value);
        ea_load_type.setText(exerciseAbstract.getLoad_type());
//        ea_load_type.setSelected(true); // Failed attempt for scrolling text

        TextView ea_separate_sides = convertView.findViewById(R.id.text_view_exerciseabs_separate_hands_value);
        if (exerciseAbstract.getSeparate_sides().isEmpty()){
            convertView.findViewById(R.id.text_view_exerciseabs_separate_hands_title).setVisibility(View.GONE);
            ea_separate_sides.setVisibility(View.GONE);
        }
        else
            ea_separate_sides.setText(exerciseAbstract.getSeparate_sides());

        TextView ea_position = convertView.findViewById(R.id.text_view_exerciseabs_position_value);
        ea_position.setText(exerciseAbstract.getPosition());

        TextView ea_angle = convertView.findViewById(R.id.text_view_exerciseabs_angle_value);
        ea_angle.setText(exerciseAbstract.getAngle());

        TextView ea_grip_width = convertView.findViewById(R.id.text_view_exerciseabs_grip_width_value);
        ea_grip_width.setText(exerciseAbstract.getGrip_width());

        TextView ea_thumbs_dir = convertView.findViewById(R.id.text_view_exerciseabs_thumbs_direction_value);
        ea_thumbs_dir.setText(exerciseAbstract.getThumbs_direction());

        // if parent/header is extended, show the extended information in this layout:
        LinearLayout extendedEAInfo = convertView.findViewById(R.id.extended_ea_info);
        if (isExpanded)
            extendedEAInfo.setVisibility(View.VISIBLE);
        else
            extendedEAInfo.setVisibility(View.GONE);


        // Show user if all exercise sets are full:
        ImageView completedExercise = convertView.findViewById(R.id.completed_exercise_group_item);

        // Im not sure what is best, to get sets from DB or going over ExpandableList HashMap that might be null.
//        Exercise exercise = exerciseViewModel.getExerciseForWorkout(EA_id, currentWorkout.getId());
//        exercise.setSets(setViewModel.getSetsForExercise(exercise.getId()));
//        if (areAllSetsFilled(exercise.getSets()))
//            completedExercise.setVisibility(View.VISIBLE);
//        else
//            completedExercise.setVisibility(View.GONE);

        if (_listDataChildCurrent != null && _listDataChildCurrent.get(EA_id) != null){
//            setCompletedExerciseImageView(completedExercise, _listDataChildCurrent.get(EA_id));
            // Basically, there is a function in Exercise that do the same, it just doesn't feel
            // right to use it that way, so the same function is written here too.
            if (areAllSetsFilled(_listDataChildCurrent.get(EA_id)) && !_listDataChildCurrent.get(EA_id).isEmpty())
                completedExercise.setVisibility(View.VISIBLE);
            else
                completedExercise.setVisibility(View.GONE);
        }



        ImageButton addImageButton = convertView.findViewById(R.id.add_button_group_item);
        addImageButton.setFocusable(Boolean.FALSE);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create new set and insert it to DB and then array
                // Exercise exercise = getExerciseFromName(headerTitle, currentWorkout.getExercises());
                // Log.v(TAG, "set size" +String.valueOf(exercise.getSets().size()));

                Exercise exercise = exerciseViewModel.getExerciseForWorkout(EA_id, currentWorkout.getId());
                Set newSet = new Set(exercise.getId(),-1.0,-1);
                setViewModel.insert(newSet);
                exercise.setSets(setViewModel.getSetsForExercise(exercise.getId()));
                _listDataChildCurrent = fillDataChildCurrent(currentWorkout);
                _listDataChildPrev = fillDataChildPrev(prevWorkout,currentWorkout);



/*                Log.v(TAG, "set size" +String.valueOf(exercise.getSets().size()));
                _listDataChildCurrent.clear();
                _listDataChildCurrent = fillDataChildCurrent(currentWorkout);*/
                //ExpandableListAdapter.this._listDataChildCurrent.put(exercise.getExerciseAbstract().getName(),setViewModel.getSetsForExercise(exercise.getId()));
                //_listDataChildCurrent.put(exercise.getExerciseAbstract().getName(),setViewModel.getSetsForExercise(exercise.getId()));
                //ExpandableListAdapter.this.notifyDataSetInvalidated();//notifyDataSetChanged();
                //notifyDataSetChanged();
                ExpandableListAdapter.this.notifyDataSetChanged();
                //_listDataChildCurrent.get(headerTitle).add();

                Toast.makeText(_context, "Set added to " + exerciseAbstract.generateExerciseAbstractName(), Toast.LENGTH_SHORT).show();
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






    // My Functions:

    private Boolean isExerciseInExerciseList(Exercise exercise, List<Exercise> exerciseList){
        for (Exercise exercise_i : exerciseList) {
            if (exercise.getId_exerciseabs() == exercise_i.getId_exerciseabs())
                return true;
        }
        return false;
    }

    void setCompletedExerciseImageView(ImageView imageView, List<Set> setList){
        if (imageView == null)
            return;
        if (areAllSetsFilled(setList))
            imageView.setVisibility(View.VISIBLE);
        else
            imageView.setVisibility((View.GONE));

    }

    private Boolean areAllSetsFilled(List<Set> setList){
        for (Set set_i : setList) {
            if (set_i.getReps() < 0 || set_i.getWeight() < 0)
                return false;
        }
        return true;
    }

    private int indexOfExerciseInExerciseList(Exercise exercise, List<Exercise> exerciseList){
        // return the index of exercise in an exercise list if found. if not, return -1.
        Iterator<Exercise> iterator = exerciseList.iterator();

        for(int i=0 ; i<exerciseList.size(); i++){
            if (exercise.getId_exerciseabs() == exerciseList.get(i).getId_exerciseabs())
                return i;

        }
        return -1;
    }

    private List<Integer> getExercisesEAIds(List<Exercise> exerciseList){
        // get EA id's as the header of this ExpandableList:
        List<Integer> EAs_IDs = new ArrayList<Integer>();

        for (Exercise exercise : exerciseList) {
            // assuming the private integer variable is named "myInt"
            EAs_IDs.add(exercise.getId_exerciseabs());
        }
        return EAs_IDs;
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

}

