package com.example.tamirmishali.trainingmanager.Routine;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tamirmishali.trainingmanager.Database.TrainingManagerDatabase;
import com.example.tamirmishali.trainingmanager.Exercise.Exercise;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.AddEditExerciseAbsActivity;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstract;
import com.example.tamirmishali.trainingmanager.R;
import com.example.tamirmishali.trainingmanager.Set.Set;
import com.example.tamirmishali.trainingmanager.Workout.Workout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoutineTableActivity extends AppCompatActivity {

    public static final String EXTRA_ROUTINE_ID =
            "com.example.tamirmishali.trainingmanager.EXTRA_ROUTINE_ID";
    public static final String EXTRA_ROUTINE_NAME =
            "com.example.tamirmishali.trainingmanager.EXTRA_ROUTINE_NAME";

    private static final int LABEL_WIDTH_DP = 100;
    private static final int COL_WIDTH_DP = 140;
    private static final int TOTAL_COL_WIDTH_DP = 60;
    private static final int ADD_BTN_WIDTH_DP = 40;
    private static final int EDIT_EXERCISE_REQUEST = 100;

    private static final int[] DEFAULT_MUSCLE_ORDER = {1, 2, 3, 4, 5, 6, 7};
    private static final String PREF_NAME = "routine_table";
    private static final String PREF_MUSCLE_ORDER_PREFIX = "muscle_order_";
    private static final Map<Integer, String> MUSCLE_NAMES = new HashMap<>();

    static {
        MUSCLE_NAMES.put(1, "Chest");
        MUSCLE_NAMES.put(2, "Back");
        MUSCLE_NAMES.put(3, "Legs");
        MUSCLE_NAMES.put(4, "Shoulders");
        MUSCLE_NAMES.put(5, "Biceps");
        MUSCLE_NAMES.put(6, "Triceps");
        MUSCLE_NAMES.put(7, "Abs");
    }

    private int routineId;
    private String routineName;
    private List<Integer> muscleOrder;
    private final List<HorizontalScrollView> scrollViews = new ArrayList<>();
    private boolean isSyncing = false;
    private boolean isReorderMode = false;
    private LinearLayout tableContainer;
    private TrainingManagerDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_table);

        db = TrainingManagerDatabase.getInstance(getApplicationContext());

        routineId = getIntent().getIntExtra(EXTRA_ROUTINE_ID, -1);
        routineName = getIntent().getStringExtra(EXTRA_ROUTINE_NAME);
        if (routineId == -1) {
            finish();
            return;
        }

        setTitle(routineName + " - Table");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tableContainer = findViewById(R.id.table_container);
        muscleOrder = loadMuscleOrder();
        buildTable();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.routine_table_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.reorder_rows) {
            isReorderMode = !isReorderMode;
            item.setTitle(isReorderMode ? "Done Reordering" : "Reorder Rows");
            buildTable();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_EXERCISE_REQUEST && resultCode == RESULT_OK) {
            buildTable();
        }
    }

    // ---- Muscle order persistence ----

    private List<Integer> loadMuscleOrder() {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String saved = prefs.getString(PREF_MUSCLE_ORDER_PREFIX + routineId, "");
        List<Integer> order = new ArrayList<>();
        if (!saved.isEmpty()) {
            for (String s : saved.split(",")) {
                try {
                    int id = Integer.parseInt(s.trim());
                    if (MUSCLE_NAMES.containsKey(id)) order.add(id);
                } catch (NumberFormatException ignored) {}
            }
        }
        // Add any muscles missing from saved order
        for (int id : DEFAULT_MUSCLE_ORDER) {
            if (!order.contains(id)) order.add(id);
        }
        return order;
    }

    private void saveMuscleOrder() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < muscleOrder.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(muscleOrder.get(i));
        }
        getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit()
                .putString(PREF_MUSCLE_ORDER_PREFIX + routineId, sb.toString())
                .apply();
    }

    // ---- Table building ----

    private void buildTable() {
        new Thread(() -> {
            List<Workout> workouts = db.workoutDao().getAbstractWorkoutsForRoutine(routineId);
            if (workouts == null) workouts = new ArrayList<>();

            // Build data: index i = workout i, map: muscleId -> List<ExerciseDisplay>
            List<Map<Integer, List<ExerciseDisplay>>> data = new ArrayList<>();
            for (Workout workout : workouts) {
                Map<Integer, List<ExerciseDisplay>> cellMap = new HashMap<>();
                for (int muscleId : muscleOrder) {
                    List<Exercise> exercises =
                            db.exerciseDao().getExercisesForWorkoutAndMuscle(workout.getId(), muscleId);
                    List<ExerciseDisplay> displays = new ArrayList<>();
                    if (exercises != null) {
                        for (Exercise ex : exercises) {
                            int setCount = db.setDao().getSetsForExercise(ex.getId()).size();
                            ExerciseAbstract ea =
                                    db.exerciseAbstractDao().getExerciseAbsFromId(ex.getId_exerciseabs());
                            String opName = db.exerciseAbstractDao()
                                    .getExerciseAbstractOperationOperation(ea.getId_operation());
                            String displayName;
                            if (ea.getId_nickname() != null && ea.getId_nickname() > 0) {
                                String nick = db.exerciseAbstractDao()
                                        .getExerciseAbstractNicknameNickname(ea.getId_nickname());
                                displayName = (nick != null ? nick : "") + " " + (opName != null ? opName : "");
                            } else {
                                displayName = opName != null ? opName : "?";
                            }
                            displays.add(new ExerciseDisplay(
                                    displayName.trim(), setCount,
                                    ex.getId(), workout.getId(), ex.getId_exerciseabs()));
                        }
                    }
                    cellMap.put(muscleId, displays);
                }
                data.add(cellMap);
            }

            final List<Workout> finalWorkouts = workouts;
            final List<Map<Integer, List<ExerciseDisplay>>> finalData = data;
            runOnUiThread(() -> {
                if (!isFinishing() && !isDestroyed()) {
                    buildTableViews(finalWorkouts, finalData);
                }
            });
        }).start();
    }

    private void buildTableViews(List<Workout> workouts,
                                 List<Map<Integer, List<ExerciseDisplay>>> data) {
        // Preserve horizontal scroll position across rebuilds
        final int savedScrollX = scrollViews.isEmpty() ? 0 : scrollViews.get(0).getScrollX();

        tableContainer.removeAllViews();
        scrollViews.clear();

        int labelW = dpToPx(LABEL_WIDTH_DP);
        int colW   = dpToPx(COL_WIDTH_DP);
        int totalW = dpToPx(TOTAL_COL_WIDTH_DP);
        int addW   = dpToPx(ADD_BTN_WIDTH_DP);

        // Header row
        tableContainer.addView(buildHeaderRow(workouts, labelW, colW, totalW, addW));

        // Muscle rows
        for (int rowIndex = 0; rowIndex < muscleOrder.size(); rowIndex++) {
            int muscleId = muscleOrder.get(rowIndex);
            String muscleName = MUSCLE_NAMES.get(muscleId);
            if (muscleName == null) continue;

            List<List<ExerciseDisplay>> rowData = new ArrayList<>();
            for (int wIdx = 0; wIdx < workouts.size(); wIdx++) {
                List<ExerciseDisplay> cell = data.get(wIdx).get(muscleId);
                rowData.add(cell != null ? cell : new ArrayList<>());
            }
            tableContainer.addView(
                    buildMuscleRow(muscleId, muscleName, workouts, rowData, rowIndex, labelW, colW, totalW));
        }

        // Totals row
        tableContainer.addView(buildTotalsRow(workouts, data, labelW, colW, totalW, addW));

        // Sync all HorizontalScrollViews
        for (HorizontalScrollView hsv : scrollViews) {
            hsv.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                if (!isSyncing) {
                    isSyncing = true;
                    for (HorizontalScrollView other : scrollViews) {
                        if (other != v) other.scrollTo(scrollX, 0);
                    }
                    isSyncing = false;
                }
            });
        }

        // Restore scroll position after layout pass
        if (savedScrollX > 0) {
            for (HorizontalScrollView hsv : scrollViews) {
                hsv.post(() -> hsv.scrollTo(savedScrollX, 0));
            }
        }
    }

    private LinearLayout buildHeaderRow(List<Workout> workouts,
                                        int labelW, int colW, int totalW, int addW) {
        LinearLayout row = newHRow();

        row.addView(makeLabelCell("Muscle", labelW, true, Color.parseColor("#37474F")));

        HorizontalScrollView hsv = newHSV();
        LinearLayout inner = newHInner();

        for (Workout w : workouts) {
            TextView header = makeHeaderCell(w.getName(), colW);
            header.setOnLongClickListener(v -> { showWorkoutOptionsDialog(w); return true; });
            inner.addView(header);
        }

        // "+" add column button
        TextView addColBtn = new TextView(this);
        addColBtn.setLayoutParams(new LinearLayout.LayoutParams(addW, LinearLayout.LayoutParams.MATCH_PARENT));
        addColBtn.setText("+");
        addColBtn.setTextSize(18);
        addColBtn.setGravity(Gravity.CENTER);
        addColBtn.setBackgroundColor(Color.parseColor("#4CAF50"));
        addColBtn.setTextColor(Color.WHITE);
        addColBtn.setOnClickListener(v -> showAddColumnDialog());
        inner.addView(addColBtn);

        // "Total" header
        inner.addView(makeHeaderCell("Total", totalW));

        hsv.addView(inner);
        row.addView(hsv);
        scrollViews.add(hsv);
        return row;
    }

    private LinearLayout buildMuscleRow(int muscleId, String muscleName,
                                        List<Workout> workouts, List<List<ExerciseDisplay>> rowData,
                                        int rowIndex, int labelW, int colW, int totalW) {
        LinearLayout row = newHRow();
        row.setBackgroundColor(rowIndex % 2 == 0 ? Color.parseColor("#FAFAFA") : Color.parseColor("#F0F0F0"));
        row.setPadding(0, 1, 0, 1);

        // Left label area
        LinearLayout labelArea = new LinearLayout(this);
        labelArea.setLayoutParams(new LinearLayout.LayoutParams(labelW, LinearLayout.LayoutParams.MATCH_PARENT));
        labelArea.setOrientation(LinearLayout.HORIZONTAL);
        labelArea.setGravity(Gravity.CENTER_VERTICAL);
        labelArea.setBackgroundColor(Color.parseColor("#546E7A"));

        if (isReorderMode) {
            LinearLayout btnCol = new LinearLayout(this);
            btnCol.setOrientation(LinearLayout.VERTICAL);
            btnCol.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
            btnCol.setGravity(Gravity.CENTER);

            final int ri = rowIndex;

            Button upBtn = new Button(this);
            upBtn.setText("↑");
            upBtn.setTextSize(10);
            upBtn.setPadding(4, 0, 4, 0);
            upBtn.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, 0, 1f));
            upBtn.setOnClickListener(v -> {
                if (ri > 0) {
                    int tmp = muscleOrder.get(ri - 1);
                    muscleOrder.set(ri - 1, muscleOrder.get(ri));
                    muscleOrder.set(ri, tmp);
                    saveMuscleOrder();
                    buildTable();
                }
            });

            Button downBtn = new Button(this);
            downBtn.setText("↓");
            downBtn.setTextSize(10);
            downBtn.setPadding(4, 0, 4, 0);
            downBtn.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, 0, 1f));
            downBtn.setOnClickListener(v -> {
                if (ri < muscleOrder.size() - 1) {
                    int tmp = muscleOrder.get(ri + 1);
                    muscleOrder.set(ri + 1, muscleOrder.get(ri));
                    muscleOrder.set(ri, tmp);
                    saveMuscleOrder();
                    buildTable();
                }
            });

            btnCol.addView(upBtn);
            btnCol.addView(downBtn);
            labelArea.addView(btnCol);
        }

        TextView nameLabel = new TextView(this);
        nameLabel.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        nameLabel.setText(muscleName);
        nameLabel.setTextSize(12);
        nameLabel.setTextColor(Color.WHITE);
        nameLabel.setTypeface(null, Typeface.BOLD);
        nameLabel.setGravity(Gravity.CENTER_VERTICAL);
        nameLabel.setPadding(6, 8, 4, 8);
        labelArea.addView(nameLabel);
        row.addView(labelArea);

        // Scrollable cells
        HorizontalScrollView hsv = newHSV();
        LinearLayout inner = newHInner();

        int totalSets = 0;
        for (int i = 0; i < workouts.size(); i++) {
            List<ExerciseDisplay> cellExercises = rowData.get(i);
            int workoutSets = 0;
            for (ExerciseDisplay ed : cellExercises) workoutSets += ed.setCount;
            totalSets += workoutSets;
            inner.addView(buildCell(workouts.get(i).getId(), muscleId, cellExercises, colW));
        }

        // Row total cell
        TextView rowTotal = new TextView(this);
        rowTotal.setLayoutParams(new LinearLayout.LayoutParams(totalW, LinearLayout.LayoutParams.MATCH_PARENT));
        rowTotal.setText(String.valueOf(totalSets));
        rowTotal.setTextSize(13);
        rowTotal.setGravity(Gravity.CENTER);
        rowTotal.setTypeface(null, Typeface.BOLD);
        rowTotal.setBackgroundColor(Color.parseColor("#CFD8DC"));
        inner.addView(rowTotal);

        hsv.addView(inner);
        row.addView(hsv);
        scrollViews.add(hsv);
        return row;
    }

    private LinearLayout buildCell(int workoutId, int muscleId,
                                   List<ExerciseDisplay> exercises, int colW) {
        LinearLayout cell = new LinearLayout(this);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(colW, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.setMargins(1, 0, 1, 0);
        cell.setLayoutParams(p);
        cell.setOrientation(LinearLayout.VERTICAL);
        cell.setPadding(4, 4, 4, 4);
        cell.setBackgroundColor(Color.WHITE);

        for (ExerciseDisplay ed : exercises) {
            cell.addView(makeExerciseChip(ed));
        }

        TextView addBtn = new TextView(this);
        addBtn.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        addBtn.setText("+");
        addBtn.setTextSize(16);
        addBtn.setTextColor(Color.parseColor("#2196F3"));
        addBtn.setGravity(Gravity.CENTER);
        addBtn.setPadding(4, 2, 4, 2);
        addBtn.setOnClickListener(v -> promptAddExercise(workoutId, muscleId));
        cell.addView(addBtn);

        return cell;
    }

    private LinearLayout makeExerciseChip(ExerciseDisplay ed) {
        LinearLayout row = new LinearLayout(this);
        LinearLayout.LayoutParams rowP = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        rowP.setMargins(0, 1, 0, 1);
        row.setLayoutParams(rowP);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setBackgroundColor(Color.parseColor("#607D8B"));
        row.setPadding(0, 2, 0, 2);

        // Name label — tap to edit exercise
        TextView nameView = new TextView(this);
        nameView.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        nameView.setText(ed.name);
        nameView.setTextSize(11);
        nameView.setTextColor(Color.WHITE);
        nameView.setPadding(6, 4, 2, 4);
        nameView.setMaxLines(2);
        nameView.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditExerciseAbsActivity.class);
            intent.putExtra(AddEditExerciseAbsActivity.EXTRA_EXERCISEABS_ID, ed.exerciseAbsId);
            intent.putExtra(AddEditExerciseAbsActivity.EXTRA_WORKOUT_ID, ed.workoutId);
            startActivityForResult(intent, EDIT_EXERCISE_REQUEST);
        });
        row.addView(nameView);

        // Minus button
        TextView minusBtn = new TextView(this);
        minusBtn.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        minusBtn.setText("−");
        minusBtn.setTextSize(13);
        minusBtn.setTextColor(Color.parseColor("#FFCDD2"));
        minusBtn.setGravity(Gravity.CENTER);
        minusBtn.setPadding(6, 2, 4, 2);
        minusBtn.setOnClickListener(v -> {
            if (ed.setCount > 0) changeSetCount(ed.exerciseId, ed.setCount, ed.setCount - 1);
        });
        row.addView(minusBtn);

        // Set count display
        TextView countView = new TextView(this);
        countView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        countView.setText(String.valueOf(ed.setCount));
        countView.setTextSize(12);
        countView.setTextColor(Color.WHITE);
        countView.setTypeface(null, Typeface.BOLD);
        countView.setGravity(Gravity.CENTER);
        countView.setPadding(4, 2, 4, 2);
        row.addView(countView);

        // Plus button
        TextView plusBtn = new TextView(this);
        plusBtn.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        plusBtn.setText("+");
        plusBtn.setTextSize(13);
        plusBtn.setTextColor(Color.parseColor("#C8E6C9"));
        plusBtn.setGravity(Gravity.CENTER);
        plusBtn.setPadding(4, 2, 6, 2);
        plusBtn.setOnClickListener(v -> changeSetCount(ed.exerciseId, ed.setCount, ed.setCount + 1));
        row.addView(plusBtn);

        // Long press for exact-count dialog
        row.setOnLongClickListener(v -> {
            showChangeSetCountDialog(ed);
            return true;
        });

        return row;
    }

    private LinearLayout buildTotalsRow(List<Workout> workouts,
                                        List<Map<Integer, List<ExerciseDisplay>>> data,
                                        int labelW, int colW, int totalW, int addW) {
        LinearLayout row = newHRow();
        row.setBackgroundColor(Color.parseColor("#ECEFF1"));

        row.addView(makeLabelCell("Total", labelW, true, Color.parseColor("#37474F")));

        HorizontalScrollView hsv = newHSV();
        LinearLayout inner = newHInner();

        int grandTotal = 0;
        for (int i = 0; i < workouts.size(); i++) {
            Map<Integer, List<ExerciseDisplay>> wData = data.get(i);
            int wTotal = 0;
            for (int muscleId : muscleOrder) {
                List<ExerciseDisplay> exList = wData.get(muscleId);
                if (exList != null) {
                    for (ExerciseDisplay ed : exList) wTotal += ed.setCount;
                }
            }
            grandTotal += wTotal;

            TextView totalCell = new TextView(this);
            totalCell.setLayoutParams(new LinearLayout.LayoutParams(colW, LinearLayout.LayoutParams.WRAP_CONTENT));
            totalCell.setText(String.valueOf(wTotal));
            totalCell.setTextSize(13);
            totalCell.setGravity(Gravity.CENTER);
            totalCell.setTypeface(null, Typeface.BOLD);
            totalCell.setBackgroundColor(Color.parseColor("#CFD8DC"));
            totalCell.setPadding(4, 10, 4, 10);
            inner.addView(totalCell);
        }

        // Spacer for the "+" add-column button
        android.view.View spacer = new android.view.View(this);
        spacer.setLayoutParams(new LinearLayout.LayoutParams(addW, LinearLayout.LayoutParams.WRAP_CONTENT));
        inner.addView(spacer);

        // Grand total
        TextView grandTotalCell = new TextView(this);
        grandTotalCell.setLayoutParams(new LinearLayout.LayoutParams(totalW, LinearLayout.LayoutParams.WRAP_CONTENT));
        grandTotalCell.setText(String.valueOf(grandTotal));
        grandTotalCell.setTextSize(13);
        grandTotalCell.setGravity(Gravity.CENTER);
        grandTotalCell.setTypeface(null, Typeface.BOLD);
        grandTotalCell.setBackgroundColor(Color.parseColor("#B0BEC5"));
        grandTotalCell.setPadding(4, 10, 4, 10);
        inner.addView(grandTotalCell);

        hsv.addView(inner);
        row.addView(hsv);
        scrollViews.add(hsv);
        return row;
    }

    // ---- Dialogs ----

    private void promptAddExercise(int workoutId, int muscleId) {
        new Thread(() -> {
            List<String> ops =
                    db.exerciseAbstractDao().getExerciseAbstractOperationByMuscleId(muscleId);
            String muscleName = MUSCLE_NAMES.get(muscleId);
            runOnUiThread(() -> {
                if (!isFinishing()) showQuickAddDialog(workoutId, muscleId, muscleName, ops);
            });
        }).start();
    }

    private void showQuickAddDialog(int workoutId, int muscleId, String muscleName,
                                    List<String> operations) {
        android.view.View dialogView =
                getLayoutInflater().inflate(R.layout.dialog_quick_add_exercise, null);

        TextView muscleLabel = dialogView.findViewById(R.id.dialog_muscle_name);
        AutoCompleteTextView operationView = dialogView.findViewById(R.id.dialog_operation);
        NumberPicker setCountPicker = dialogView.findViewById(R.id.dialog_set_count);

        muscleLabel.setText(muscleName);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, operations);
        operationView.setAdapter(adapter);
        operationView.setThreshold(1);
        operationView.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) operationView.showDropDown();
        });

        setCountPicker.setMinValue(1);
        setCountPicker.setMaxValue(10);
        setCountPicker.setValue(3);

        new AlertDialog.Builder(this)
                .setTitle("Add Exercise — " + muscleName)
                .setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    String op = operationView.getText().toString().trim();
                    int setCount = setCountPicker.getValue();
                    if (!op.isEmpty()) {
                        addExerciseToCell(workoutId, muscleId, op, setCount);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void addExerciseToCell(int workoutId, int muscleId, String operation, int setCount) {
        new Thread(() -> {
            int opId = db.exerciseAbstractDao()
                    .getExerciseAbstractOperationId(String.valueOf(muscleId), operation);
            if (opId == 0) {
                runOnUiThread(() -> Toast.makeText(this,
                        "Exercise not found. Select from the dropdown.", Toast.LENGTH_SHORT).show());
                return;
            }

            // Check if exercise abstract exists (no nickname, all extras = 0)
            int eaId = db.exerciseAbstractDao()
                    .getExerciseAbstractId_inner(muscleId, opId, 0, 0, 0, 0, 0, 0);

            // If not found, insert it
            if (eaId == 0) {
                ExerciseAbstract ea = new ExerciseAbstract();
                ea.setId_muscle(muscleId);
                ea.setId_operation(opId);
                ea.setId_nickname(null);
                ea.setId_load_type(0);
                ea.setId_position(0);
                ea.setId_angle(0);
                ea.setId_grip_width(0);
                ea.setId_thumbs_direction(0);
                ea.setId_separate_sides(0);
                eaId = (int) db.exerciseAbstractDao().insert(ea);
            }

            // Insert exercise
            long exerciseId = db.exerciseDao().insert(new Exercise(eaId, workoutId, ""));

            // Insert sets
            for (int i = 0; i < setCount; i++) {
                db.setDao().insert(new Set((int) exerciseId, -1.0, -1));
            }

            runOnUiThread(this::buildTable);
        }).start();
    }

    private void showChangeSetCountDialog(ExerciseDisplay ed) {
        NumberPicker picker = new NumberPicker(this);
        picker.setMinValue(0);
        picker.setMaxValue(20);
        picker.setValue(ed.setCount);

        new AlertDialog.Builder(this)
                .setTitle("Sets for: " + ed.name)
                .setView(picker)
                .setPositiveButton("OK", (dialog, which) -> {
                    int newCount = picker.getValue();
                    if (newCount != ed.setCount) {
                        changeSetCount(ed.exerciseId, ed.setCount, newCount);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void changeSetCount(int exerciseId, int currentCount, int newCount) {
        new Thread(() -> {
            List<Set> sets = db.setDao().getSetsForExercise(exerciseId);
            if (newCount > currentCount) {
                for (int i = 0; i < newCount - currentCount; i++) {
                    db.setDao().insert(new Set(exerciseId, -1.0, -1));
                }
            } else {
                int toDelete = currentCount - newCount;
                for (int i = sets.size() - 1; i >= 0 && toDelete > 0; i--, toDelete--) {
                    db.setDao().delete(sets.get(i));
                }
            }
            runOnUiThread(this::buildTable);
        }).start();
    }

    private void showAddColumnDialog() {
        EditText nameInput = new EditText(this);
        nameInput.setHint("Workout name");
        nameInput.setSingleLine(true);

        new AlertDialog.Builder(this)
                .setTitle("Add Workout")
                .setView(nameInput)
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = nameInput.getText().toString().trim();
                    if (!name.isEmpty()) {
                        new Thread(() -> {
                            db.workoutDao().insert(new Workout(routineId, name, true));
                            runOnUiThread(this::buildTable);
                        }).start();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showWorkoutOptionsDialog(Workout workout) {
        new AlertDialog.Builder(this)
                .setTitle(workout.getName())
                .setItems(new String[]{"Rename", "Delete"}, (dialog, which) -> {
                    if (which == 0) showRenameWorkoutDialog(workout);
                    else showDeleteWorkoutConfirmDialog(workout);
                })
                .show();
    }

    private void showRenameWorkoutDialog(Workout workout) {
        EditText nameInput = new EditText(this);
        nameInput.setText(workout.getName());
        nameInput.setSingleLine(true);

        new AlertDialog.Builder(this)
                .setTitle("Rename Workout")
                .setView(nameInput)
                .setPositiveButton("Rename", (dialog, which) -> {
                    String newName = nameInput.getText().toString().trim();
                    if (!newName.isEmpty()) {
                        new Thread(() -> {
                            workout.setName(newName);
                            db.workoutDao().update(workout);
                            runOnUiThread(this::buildTable);
                        }).start();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showDeleteWorkoutConfirmDialog(Workout workout) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Workout")
                .setMessage("Delete \"" + workout.getName() + "\" and all its exercises?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    new Thread(() -> {
                        db.workoutDao().delete(workout);
                        runOnUiThread(this::buildTable);
                    }).start();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // ---- View helpers ----

    private LinearLayout newHRow() {
        LinearLayout row = new LinearLayout(this);
        row.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        row.setOrientation(LinearLayout.HORIZONTAL);
        return row;
    }

    private HorizontalScrollView newHSV() {
        HorizontalScrollView hsv = new HorizontalScrollView(this);
        hsv.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        hsv.setHorizontalScrollBarEnabled(false);
        return hsv;
    }

    private LinearLayout newHInner() {
        LinearLayout inner = new LinearLayout(this);
        inner.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        inner.setOrientation(LinearLayout.HORIZONTAL);
        return inner;
    }

    private TextView makeLabelCell(String text, int widthPx, boolean bold, int bgColor) {
        TextView tv = new TextView(this);
        tv.setLayoutParams(new LinearLayout.LayoutParams(widthPx, LinearLayout.LayoutParams.MATCH_PARENT));
        tv.setText(text);
        tv.setTextSize(13);
        tv.setTextColor(Color.WHITE);
        tv.setBackgroundColor(bgColor);
        tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
        tv.setPadding(8, 10, 8, 10);
        if (bold) tv.setTypeface(null, Typeface.BOLD);
        return tv;
    }

    private TextView makeHeaderCell(String text, int widthPx) {
        TextView tv = new TextView(this);
        tv.setLayoutParams(new LinearLayout.LayoutParams(widthPx, LinearLayout.LayoutParams.WRAP_CONTENT));
        tv.setText(text);
        tv.setTextSize(13);
        tv.setTextColor(Color.WHITE);
        tv.setBackgroundColor(Color.parseColor("#546E7A"));
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(4, 12, 4, 12);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setSingleLine(true);
        return tv;
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    // ---- Inner class ----

    static class ExerciseDisplay {
        final String name;
        final int setCount;
        final int exerciseId;
        final int workoutId;
        final int exerciseAbsId;

        ExerciseDisplay(String name, int setCount, int exerciseId, int workoutId, int exerciseAbsId) {
            this.name = name;
            this.setCount = setCount;
            this.exerciseId = exerciseId;
            this.workoutId = workoutId;
            this.exerciseAbsId = exerciseAbsId;
        }
    }
}
