package com.example.tamirmishali.trainingmanager;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
//import android.util.SparseBooleanArray;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tamirmishali.trainingmanager.Exercise.Exercise;
import com.example.tamirmishali.trainingmanager.Exercise.ExerciseViewModel;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstract;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstractViewModel;
import com.example.tamirmishali.trainingmanager.Set.Set;
import com.example.tamirmishali.trainingmanager.Set.SetViewModel;
import com.example.tamirmishali.trainingmanager.Workout.Workout;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class WorkoutRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_EXERCISE_HEADER = 0;
    private static final int VIEW_TYPE_SET_ROW = 1;

    private transient Context context;
    private List<WorkoutItem> items;
    private Map<Integer, List<Set>> prevWorkoutSets;
    private SetViewModel setViewModel;
    private ExerciseAbstractViewModel exerciseAbstractViewModel;
    private ExerciseViewModel exerciseViewModel;
    private Workout currentWorkout, prevWorkout;

//    private transient SparseBooleanArray expandState = new SparseBooleanArray();

    public WorkoutRecyclerAdapter(Context context, Workout currentWorkout, Workout prevWorkout,
                                  SetViewModel setViewModel, ExerciseViewModel exerciseViewModel,
                                  ExerciseAbstractViewModel exerciseAbstractViewModel) {
        this.context = context;
        this.currentWorkout = currentWorkout;
        this.prevWorkout = prevWorkout;
        this.setViewModel = setViewModel;
        this.exerciseViewModel = exerciseViewModel;
        this.exerciseAbstractViewModel = exerciseAbstractViewModel;

        buildWorkoutItems();
        setupPreviousWorkoutData();

        for (int i = 0; i < items.size(); i++) {
            Log.d("WorkoutAdapter", "Item " + i + ": " + items.get(i).type);
        }
    }

    // TODO: (NEW) understand how to expand all of the little craps
    public void expandAll() {
//        for (int i = 0; i < exercises.size(); i++) {
//            expandState.put(i, true);
//        }
        notifyDataSetChanged();
    }

    private void buildWorkoutItems() {
        items = new ArrayList<>();

        for (Exercise exercise : currentWorkout.getExercises()) {
            // Add exercise header
            items.add(new WorkoutItem(WorkoutItem.Type.EXERCISE_HEADER, exercise, null));

            // Add sets for this exercise
            List<Set> sets = setViewModel.getSetsForExercise(exercise.getId());
            if (sets != null) {
                for (Set set : sets) {
                    items.add(new WorkoutItem(WorkoutItem.Type.SET_ROW, exercise, set));
                }
            }
        }
    }

    private void setupPreviousWorkoutData() {
        prevWorkoutSets = new HashMap<>();

        if (prevWorkout == null) return;

        for (Exercise exercise : currentWorkout.getExercises()) {
            List<Set> prevSets = new ArrayList<>();

            // Find matching exercise in previous workout
            for (Exercise prevExercise : prevWorkout.getExercises()) {
                if (exercise.getId_exerciseabs() == prevExercise.getId_exerciseabs()) {
                    prevSets = prevExercise.getSets();
                    break;
                }
            }

            prevWorkoutSets.put(exercise.getId_exerciseabs(), prevSets);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).type == WorkoutItem.Type.EXERCISE_HEADER ?
                VIEW_TYPE_EXERCISE_HEADER : VIEW_TYPE_SET_ROW;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == VIEW_TYPE_EXERCISE_HEADER) {
            View view = inflater.inflate(R.layout.workoutnow_list_group_item, parent, false);
            return new ExerciseHeaderViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.workoutnow_list_set_item, parent, false);
            return new SetRowViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        WorkoutItem item = items.get(position);

        if (holder instanceof ExerciseHeaderViewHolder) {
            bindExerciseHeader((ExerciseHeaderViewHolder) holder, item);
        } else if (holder instanceof SetRowViewHolder) {
            bindSetRow((SetRowViewHolder) holder, item, position);
        } else {
            Log.e("WorkoutAdapter", "Unknown ViewHolder type at position " + position);
        }
    }

    private void setFieldVisibility(TextView titleView, TextView valueView, String value, String defaultTitle) {
        if (value == null || value.isEmpty() || value.equals("N/A")) {
            titleView.setVisibility(View.GONE);
            valueView.setVisibility(View.GONE);
        } else {
            titleView.setVisibility(View.VISIBLE);
            valueView.setVisibility(View.VISIBLE);
            titleView.setText(defaultTitle);
            valueView.setText(value);
        }
    }

    private void collapseEmptyDetailsSection(View parentView, int containerId) {
        LinearLayout container = parentView.findViewById(containerId);
        if (container == null) return;

        boolean hasVisibleChildren = false;
        int childCount = container.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = container.getChildAt(i);
            if (child instanceof LinearLayout) {
                LinearLayout fieldGroup = (LinearLayout) child;
                boolean hasAnyVisible = false;
                for (int j = 0; j < fieldGroup.getChildCount(); j++) {
                    View fieldChild = fieldGroup.getChildAt(j);
                    if (fieldChild.getVisibility() == View.VISIBLE) {
                        hasAnyVisible = true;
                        break;
                    }
                }
                if (!hasAnyVisible) {
                    child.setVisibility(View.GONE);
                } else {
                    child.setVisibility(View.VISIBLE);
                }
            }
        }

        // Also collapse the whole section if nothing is visible
        for (int i = 0; i < container.getChildCount(); i++) {
            if (container.getChildAt(i).getVisibility() == View.VISIBLE) {
                hasVisibleChildren = true;
                break;
            }
        }
        container.setVisibility(hasVisibleChildren ? View.VISIBLE : View.GONE);
    }

    private void bindExerciseHeader(ExerciseHeaderViewHolder holder, WorkoutItem item) {
        Exercise exercise = item.exercise;
        ExerciseAbstract ea = exerciseAbstractViewModel.getExerciseAbsFromId(exercise.getId_exerciseabs());

        if (ea == null) {
            Log.e("WorkoutAdapter", "ExerciseAbstract is null for id: " + exercise.getId_exerciseabs());
            return;
        }

        // Set exercise name
        holder.exerciseName.setText(ea.generateExerciseAbstractName());

        // Get current sets
        List<Set> currentSets = getCurrentSetsForExercise(exercise.getId_exerciseabs());
        List<Set> prevSets = prevWorkoutSets.get(exercise.getId_exerciseabs());

        int currentTotal = calculateTotalLoad(currentSets);
        int prevTotal = calculateTotalLoad(prevSets);

        // Color coding based on progress
        if (prevTotal > 0 && currentTotal > 0) {
            int color = currentTotal >= prevTotal ?
                    ContextCompat.getColor(context, R.color.fail_color) :
                    ContextCompat.getColor(context, R.color.fail_color);
            holder.completionIndicatorContainer.setCardBackgroundColor(color);
        } else {
            holder.exerciseName.setTextColor(ContextCompat.getColor(context, R.color.text_primary));
        }

        // Add set button
        holder.addSetButton.setOnClickListener(v -> addNewSet(exercise));

        // Set completion indicator
        boolean allComplete = areAllSetsFilled(currentSets);
        holder.completionIndicator.setVisibility(allComplete ? View.VISIBLE : View.GONE);

        // Load Type + Separate Sides
        setFieldVisibility(holder.loadTypeTitle, holder.loadTypeValue, ea.getLoad_type(), "Load Type");
        setFieldVisibility(holder.separateHandsTitle, holder.separateHandsValue, ea.getSeparate_sides(), "Separate Sides");

        // Position + Angle
        setFieldVisibility(holder.positionTitle, holder.positionValue, ea.getPosition(), "Position");
        setFieldVisibility(holder.angleTitle, holder.angleValue, ea.getAngle(), "Angle");

        // Grip Width + Thumbs Direction
        setFieldVisibility(holder.gripWidthTitle, holder.gripWidthValue, ea.getGrip_width(), "Grip Width");
        setFieldVisibility(holder.thumbsDirectionTitle, holder.thumbsDirectionValue, ea.getThumbs_direction(), "Thumbs Direction");

        // Now collapse entire rows if needed
        collapseEmptyRow(holder.rowLoadTypeAndSides,
                holder.containerLoadType,
                holder.containerSeparateSides);

//        collapseEmptyRow(holder.rowPositionAndAngle,
//                holder.containerPosition,
//                holder.containerAngle);
//
//        collapseEmptyRow(holder.rowGripAndThumbs,
//                holder.containerGripWidth,
//                holder.containerThumbsDirection);
    }

    private void bindSetRow(SetRowViewHolder holder, WorkoutItem item, int position) {
        Set currentSet = item.set;
        Exercise exercise = item.exercise;

        // Get corresponding previous set
        List<Set> prevSets = prevWorkoutSets.get(exercise.getId_exerciseabs());
        int setIndex = getSetIndexInExercise(position);
        Set prevSet = (prevSets != null && setIndex < prevSets.size()) ? prevSets.get(setIndex) : null;

        // Set number
//        holder.setNumber.setText(String.valueOf(setIndex + 1));

        // Previous workout data
        if (prevSet != null) {
            holder.prevWeight.setText(formatNumber(prevSet.getWeight()));
            holder.prevReps.setText(formatNumber(prevSet.getReps()));
        } else {
            holder.prevWeight.setText("N/A");
            holder.prevReps.setText("N/A");
        }

        // Current workout data - with focus management
        setupEditText(holder.currentWeight, currentSet.getWeight(), text -> updateSetWeight(currentSet, text), currentSet, holder, position, exercise);
        setupEditText(holder.currentReps, currentSet.getReps(), text -> updateSetReps(currentSet, text), currentSet, holder, position, exercise);
        // Copy from previous set button
        holder.copyButton.setOnClickListener(v -> copyFromPrevious(currentSet, prevSet, holder));
        holder.copyButton.setEnabled(prevSet != null);

        // Delete set button
        holder.deleteButton.setOnClickListener(v -> deleteSet(currentSet, position, holder));
    }

    private ExerciseHeaderViewHolder findExerciseHeaderView(RecyclerView recyclerView, int setPosition) {
        for (int i = setPosition - 1; i >= 0; i--) {
            if (items.get(i).type == WorkoutItem.Type.EXERCISE_HEADER) {
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(i);
                if (viewHolder instanceof ExerciseHeaderViewHolder) {
                    return (ExerciseHeaderViewHolder) viewHolder;
                }
            }
        }
        return null;
    }

    private int calculatePrevTotal(int exerciseAbsId) {
        List<Set> prevSets = prevWorkoutSets.get(exerciseAbsId);
        return calculateTotalLoad(prevSets);
    }

    private int findHeaderPositionForExercise(Exercise exercise) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).type == WorkoutItem.Type.EXERCISE_HEADER &&
                    items.get(i).exercise.getId_exerciseabs() == exercise.getId_exerciseabs()) {
                return i;
            }
        }
        return RecyclerView.NO_POSITION;
    }
    private RecyclerView findRootRecyclerView(View view) {
        ViewParent parent = view.getParent();
        while (parent != null && !(parent instanceof RecyclerView)) {
            if (parent instanceof View) {
                parent = parent.getParent();
            } else {
                break;
            }
        }
        return (RecyclerView) parent;
    }
    private void checkAndUpdateExerciseHeader(SetRowViewHolder holder, int position, Exercise exercise) {
        RecyclerView recyclerView = findRootRecyclerView(holder.itemView);
        if (recyclerView == null) {
            Log.e("WorkoutAdapter", "RecyclerView not found!");
            return;
        }

        List<Set> sets = getCurrentSetsForExercise(exercise.getId_exerciseabs());

        boolean allComplete = areAllSetsFilled(sets);
        // Now find the header and update it
        ExerciseHeaderViewHolder headerHolder = findExerciseHeaderView(recyclerView, position);

        if (headerHolder != null) {
            if (allComplete) {
                headerHolder.completionIndicator.setVisibility(View.VISIBLE);
            } else {
                headerHolder.completionIndicator.setVisibility(View.GONE);
            }

            int currentTotal = calculateTotalLoad(sets);
            int prevTotal = calculatePrevTotal(exercise.getId_exerciseabs());

            if (prevTotal > 0 && currentTotal > 0 && allComplete) {
                int color = currentTotal >= prevTotal ?
                        ContextCompat.getColor(context, R.color.success_color) :
                        ContextCompat.getColor(context, R.color.fail_color);
                headerHolder.completionIndicatorContainer.setCardBackgroundColor(color);
            } else {
                headerHolder.exerciseName.setTextColor(ContextCompat.getColor(context, R.color.text_primary));
            }
        }
    }

    private void setupEditText(EditText editText, double value, TextUpdateListener listener, Set set, SetRowViewHolder holder, int position, Exercise exercise) {
        Object tag = editText.getTag();
        if (tag instanceof TextWatcher) {
            editText.removeTextChangedListener((TextWatcher) tag);
        }

        editText.setText(formatNumber(value));

        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                listener.onTextUpdated(s.toString());
                checkAndUpdateExerciseHeader(holder, position, exercise); // Pass exercise here
            }
        };

        editText.addTextChangedListener(watcher);
        editText.setTag(watcher);
    }

    private void updateSetWeight(Set set, String text) {
        try {
            double weight = text.isEmpty() ? -1.0 : Double.parseDouble(text);
            set.setWeight(weight);
            setViewModel.update(set);
        } catch (NumberFormatException e) {
            // Invalid input, keep previous value
        }
    }

    private void updateSetReps(Set set, String text) {
        try {
            int reps = text.isEmpty() ? -1 : Integer.parseInt(text);
            set.setReps(reps);
            setViewModel.update(set);
        } catch (NumberFormatException e) {
            // Invalid input, keep previous value
        }
    }

    private void copyFromPrevious(Set currentSet, Set prevSet, SetRowViewHolder holder) {
        if (prevSet == null) return;

        currentSet.setWeight(prevSet.getWeight());
        currentSet.setReps(prevSet.getReps());
        setViewModel.update(currentSet);

        // Update UI
        holder.currentWeight.setText(formatNumber(prevSet.getWeight()));
        holder.currentReps.setText(formatNumber(prevSet.getReps()));

//        Toast.makeText(context, "Copied from previous set", Toast.LENGTH_SHORT).show();
    }

    private void addNewSet(Exercise exercise) {
        Set newSet = new Set(exercise.getId(), -1.0, -1);
        setViewModel.insert(newSet);

        buildWorkoutItems(); // Or use notifyItemInserted(...) for efficiency
        notifyDataSetChanged();

        int headerPos = findHeaderPositionForExercise(exercise);
        if (headerPos != RecyclerView.NO_POSITION) {
            notifyItemChanged(headerPos);
        }
    }

    private void updateHeaderWithProgress(ExerciseHeaderViewHolder holder, Exercise exercise, List<Set> sets) {
        boolean allComplete = areAllSetsFilled(sets);
        holder.completionIndicator.setVisibility(allComplete ? View.VISIBLE : View.GONE);

        int currentTotal = calculateTotalLoad(sets);
        int prevTotal = calculatePrevTotal(exercise.getId_exerciseabs());

        if (prevTotal > 0 && currentTotal > 0) {
            int color = currentTotal >= prevTotal ?
                    ContextCompat.getColor(context, R.color.success_color) :
                    ContextCompat.getColor(context, R.color.fail_color);
            holder.completionIndicatorContainer.setCardBackgroundColor(color);
        } else {
            holder.exerciseName.setTextColor(ContextCompat.getColor(context, R.color.text_primary));
        }
    }

    private void deleteSet(Set set, int position, SetRowViewHolder holder) {
        setViewModel.delete(set);
        items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, items.size() - position);

        RecyclerView recyclerView = findRootRecyclerView(holder.itemView);
        if (recyclerView != null) {
            ExerciseHeaderViewHolder headerHolder = findExerciseHeaderView(recyclerView, position);
            if (headerHolder != null) {
                Exercise exercise = items.get(position).exercise;
                List<Set> sets = getCurrentSetsForExercise(exercise.getId_exerciseabs());
                updateHeaderWithProgress(headerHolder, exercise, sets);
            }
        }
    }

    public void moveItem(int fromPosition, int toPosition) {
        // Replace Collections.swap with manual swap
        WorkoutItem temp = items.get(fromPosition);
        items.set(fromPosition, items.get(toPosition));
        items.set(toPosition, temp);

        notifyItemMoved(fromPosition, toPosition);
        updateSetOrder(items.get(fromPosition).exercise);
    }

    private void updateSetOrder(Exercise exercise) {
        // Update the order in database based on current item positions
        List<Set> orderedSets = getCurrentSetsForExercise(exercise.getId_exerciseabs());
        for (int i = 0; i < orderedSets.size(); i++) {
            // Update order field in database if you have one
            // setViewModel.updateOrder(orderedSets.get(i), i);
        }
    }

    // Utility methods
    private List<Set> getCurrentSetsForExercise(int exerciseAbsId) {
        List<Set> sets = new ArrayList<>();
        for (WorkoutItem item : items) {
            if (item.type == WorkoutItem.Type.SET_ROW &&
                item.exercise.getId_exerciseabs() == exerciseAbsId) {
                sets.add(item.set);
            }
        }
        return sets;
    }

    private int getSetIndexInExercise(int position) {
        WorkoutItem item = items.get(position);
        int index = 0;

        for (int i = 0; i < position; i++) {
            WorkoutItem prevItem = items.get(i);
            if (prevItem.type == WorkoutItem.Type.SET_ROW &&
                prevItem.exercise.getId() == item.exercise.getId()) {
                index++;
            }
        }
        return index;
    }

    private boolean areAllSetsFilled(List<Set> sets) {
        if (sets == null || sets.isEmpty()) return false;

        for (Set set : sets) {
            if (set.getReps() < 0 || set.getWeight() < 0) {
                return false;
            }
        }
        return true;
    }

    private int calculateTotalLoad(List<Set> sets) {
        if (sets == null || sets.isEmpty()) return -1;

        int total = 0;
        for (Set set : sets) {
            if (set.getReps() <= 0 || set.getWeight() <= 0) continue;
            total += set.getReps() * (int) set.getWeight();
        }
        return total;
    }

    private String formatNumber(double value) {
        if (value == -1.0) return "";
        if (value == -2.0) return "N/A";
        return value % 1 == 0 ? String.valueOf((int) value) : String.valueOf(value);
    }

    private String formatNumber(int value) {
        if (value == -1) return "";
        if (value == -2) return "N/A";
        return String.valueOf(value);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty() || str.equals("N/A");
    }

    private void collapseEmptyRow(LinearLayout rowContainer, LinearLayout... fieldContainers) {
        boolean anyVisible = false;
        for (LinearLayout container : fieldContainers) {
            if (container.getVisibility() == View.VISIBLE) {
                anyVisible = true;
                break;
            }
        }

        rowContainer.setVisibility(anyVisible ? View.VISIBLE : View.GONE);
    }

    // ViewHolder classes
    static class ExerciseHeaderViewHolder extends RecyclerView.ViewHolder {
        CardView completionIndicatorContainer;
        TextView exerciseName;
        ImageView completionIndicator;
        ImageButton addSetButton;

        // Field Titles and Values
        TextView loadTypeTitle, loadTypeValue;
        TextView separateHandsTitle, separateHandsValue;
        TextView positionTitle, positionValue;
        TextView angleTitle, angleValue;
        TextView gripWidthTitle, gripWidthValue;
        TextView thumbsDirectionTitle, thumbsDirectionValue;

        // Row Containers
        LinearLayout rowLoadTypeAndSides;
        LinearLayout rowPositionAndAngle;
        LinearLayout rowGripAndThumbs;

        // Individual Field Containers
        LinearLayout containerLoadType;
        LinearLayout containerSeparateSides;
        LinearLayout containerPosition;
        LinearLayout containerAngle;
        LinearLayout containerGripWidth;
        LinearLayout containerThumbsDirection;

        public ExerciseHeaderViewHolder(@NonNull View itemView) {
            super(itemView);

            exerciseName = itemView.findViewById(R.id.lblListHeader);
            completionIndicator = itemView.findViewById(R.id.completed_exercise_group_item);
            completionIndicatorContainer = itemView.findViewById(R.id.completed_exercise_group_item_container);
            addSetButton = itemView.findViewById(R.id.add_button_group_item);

            // Field Views
            loadTypeTitle = itemView.findViewById(R.id.text_view_exerciseabs_load_type_title);
            loadTypeValue = itemView.findViewById(R.id.text_view_exerciseabs_load_type_value);

            separateHandsTitle = itemView.findViewById(R.id.text_view_exerciseabs_separate_hands_title);
            separateHandsValue = itemView.findViewById(R.id.text_view_exerciseabs_separate_hands_value);

            positionTitle = itemView.findViewById(R.id.text_view_exerciseabs_position_title);
            positionValue = itemView.findViewById(R.id.text_view_exerciseabs_position_value);

            angleTitle = itemView.findViewById(R.id.text_view_exerciseabs_angle_title);
            angleValue = itemView.findViewById(R.id.text_view_exerciseabs_angle_value);

            gripWidthTitle = itemView.findViewById(R.id.text_view_exerciseabs_grip_width_title);
            gripWidthValue = itemView.findViewById(R.id.text_view_exerciseabs_grip_width_value);

            thumbsDirectionTitle = itemView.findViewById(R.id.text_view_exerciseabs_thumbs_direction_title);
            thumbsDirectionValue = itemView.findViewById(R.id.text_view_exerciseabs_thumbs_direction_value);

            // Row Containers
            rowLoadTypeAndSides = itemView.findViewById(R.id.row_load_type_and_separate_sides);
            rowPositionAndAngle = itemView.findViewById(R.id.row_position_and_angle);
            rowGripAndThumbs = itemView.findViewById(R.id.row_grip_and_thumbs);

            // Field Containers
            containerLoadType = itemView.findViewById(R.id.container_load_type);
            containerSeparateSides = itemView.findViewById(R.id.container_separate_hands);
            containerPosition = itemView.findViewById(R.id.container_position);
            containerAngle = itemView.findViewById(R.id.container_angle);
            containerGripWidth = itemView.findViewById(R.id.container_grip_width);
            containerThumbsDirection = itemView.findViewById(R.id.container_thumbs_direction);
        }
    }

    public static class SetRowViewHolder extends RecyclerView.ViewHolder {
        TextView setNumber, prevWeight, prevReps;
        TextInputEditText currentWeight, currentReps;
        ImageButton copyButton, deleteButton;

        public SetRowViewHolder(@NonNull View itemView) {
            super(itemView);
            prevWeight = itemView.findViewById(R.id.lblListItemEditextPrevWeight);
            prevReps = itemView.findViewById(R.id.lblListItemEditextPrevReps);
            currentWeight = itemView.findViewById(R.id.lblListItemEditextNowWeight);
            currentReps = itemView.findViewById(R.id.lblListItemEditextNowReps);
            copyButton = itemView.findViewById(R.id.arrow_forward_button_duplicate_set);
            deleteButton = itemView.findViewById(R.id.delete_button_list_item);
        }
    }

    // Data models
    static class WorkoutItem {
        enum Type { EXERCISE_HEADER, SET_ROW }

        Type type;
        Exercise exercise;
        Set set;

        public WorkoutItem(Type type, Exercise exercise, Set set) {
            this.type = type;
            this.exercise = exercise;
            this.set = set;
        }
    }

    interface TextUpdateListener {
        void onTextUpdated(String text);
    }
}