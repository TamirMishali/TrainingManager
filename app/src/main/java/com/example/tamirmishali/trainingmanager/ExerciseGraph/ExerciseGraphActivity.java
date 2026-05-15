package com.example.tamirmishali.trainingmanager.ExerciseGraph;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.tamirmishali.trainingmanager.Database.TrainingManagerDatabase;
import com.example.tamirmishali.trainingmanager.Exercise.Exercise;
import com.example.tamirmishali.trainingmanager.R;
import com.example.tamirmishali.trainingmanager.Set.Set;
import com.example.tamirmishali.trainingmanager.Workout.Workout;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class ExerciseGraphActivity extends AppCompatActivity {

    public static final String EXTRA_EXERCISE_ABS_ID = "extra_exercise_abs_id";
    public static final String EXTRA_ROUTINE_ID = "extra_routine_id";
    public static final String EXTRA_EXERCISE_NAME = "extra_exercise_name";

    private CombinedChart chart;
    private LineChart weightChart, repsChart;
    private LinearLayout stackedChartsContainer;
    private TextView statSessions, statBest, statAvg;
    private TextView statBestLabel, statAvgLabel;
    private int exerciseAbsId, routineId;
    private String exerciseName;
    private boolean showAllTime = false;
    private boolean showRepsWeight = false;
    private TrainingManagerDatabase db;
    private final List<String> xLabels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_graph);

        exerciseAbsId = getIntent().getIntExtra(EXTRA_EXERCISE_ABS_ID, -1);
        routineId = getIntent().getIntExtra(EXTRA_ROUTINE_ID, -1);
        exerciseName = getIntent().getStringExtra(EXTRA_EXERCISE_NAME);

        db = TrainingManagerDatabase.getInstance(this);

        Toolbar toolbar = findViewById(R.id.graph_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(exerciseName != null ? exerciseName : "Overload History");
        }

        chart = findViewById(R.id.combined_chart);
        weightChart = findViewById(R.id.weight_chart);
        repsChart = findViewById(R.id.reps_chart);
        stackedChartsContainer = findViewById(R.id.stacked_charts_container);
        statSessions = findViewById(R.id.stat_sessions);
        statBest = findViewById(R.id.stat_best);
        statAvg = findViewById(R.id.stat_avg);
        statBestLabel = findViewById(R.id.stat_best_label);
        statAvgLabel = findViewById(R.id.stat_avg_label);

        setupChart();
        setupLineChartBase(weightChart);
        setupLineChartBase(repsChart);

        MaterialButtonToggleGroup toggleTimeRange = findViewById(R.id.toggle_time_range);
        toggleTimeRange.check(R.id.btn_this_routine);
        toggleTimeRange.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) return;
            showAllTime = (checkedId == R.id.btn_all_time);
            loadData();
        });

        MaterialButtonToggleGroup toggleGraphType = findViewById(R.id.toggle_graph_type);
        toggleGraphType.check(R.id.btn_overload);
        toggleGraphType.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) return;
            showRepsWeight = (checkedId == R.id.btn_reps_weight);
            chart.setVisibility(showRepsWeight ? View.GONE : View.VISIBLE);
            stackedChartsContainer.setVisibility(showRepsWeight ? View.VISIBLE : View.GONE);
            statBestLabel.setText(showRepsWeight ? "BEST WEIGHT" : "BEST SESSION");
            statAvgLabel.setText(showRepsWeight ? "AVG REPS" : "AVG SET");
            loadData();
        });

        loadData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupChart() {
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);
        chart.setHighlightFullBarEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setBackgroundColor(ContextCompat.getColor(this, R.color.card_background_primary));

        chart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.SCATTER,
                CombinedChart.DrawOrder.LINE
        });

        int textColor = ContextCompat.getColor(this, R.color.text_secondary);
        int gridColor = 0x22000000;

        chart.getLegend().setEnabled(true);
        chart.getLegend().setTextColor(ContextCompat.getColor(this, R.color.text_primary));
        chart.getLegend().setTextSize(11f);

        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setTextColor(textColor);
        chart.getAxisLeft().setGridColor(gridColor);
        chart.getAxisLeft().setAxisMinimum(0f);
        chart.getAxisLeft().setTextSize(11f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setTextColor(textColor);
        xAxis.setGridColor(gridColor);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(10f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int idx = Math.round(value);
                if (idx >= 0 && idx < xLabels.size()) return xLabels.get(idx);
                return "";
            }
        });
    }

    private void setupLineChartBase(LineChart lc) {
        lc.setDrawGridBackground(false);
        lc.getDescription().setEnabled(false);
        lc.setBackgroundColor(ContextCompat.getColor(this, R.color.card_background_primary));

        int textColor = ContextCompat.getColor(this, R.color.text_secondary);
        int gridColor = 0x22000000;

        lc.getLegend().setEnabled(false);
        lc.getAxisRight().setEnabled(false);
        lc.getAxisLeft().setTextColor(textColor);
        lc.getAxisLeft().setGridColor(gridColor);
        lc.getAxisLeft().setTextSize(11f);

        XAxis xAxis = lc.getXAxis();
        xAxis.setTextColor(textColor);
        xAxis.setGridColor(gridColor);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(10f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int idx = Math.round(value);
                if (idx >= 0 && idx < xLabels.size()) return xLabels.get(idx);
                return "";
            }
        });
    }

    private void loadData() {
        new Thread(() -> {
            List<SessionData> sessions = showAllTime ? loadAllTimeData() : loadRoutineData();
            runOnUiThread(() -> {
                if (showRepsWeight) updateRepsWeightCharts(sessions);
                else updateChart(sessions);
            });
        }).start();
    }

    private List<SessionData> loadRoutineData() {
        List<SessionData> result = new ArrayList<>();
        List<Workout> workouts = db.workoutDao().getPracticalWorkoutsForRoutine(routineId);
        for (Workout w : workouts) {
            if (w.getDate() == null) continue;
            Exercise ex = db.exerciseDao().getExerciseForWorkout(exerciseAbsId, w.getId());
            if (ex == null) continue;
            List<Set> sets = db.setDao().getSetsForExercise(ex.getId());
            List<Double> overloads = validOverloads(sets);
            if (overloads.isEmpty()) continue;
            result.add(new SessionData(w.getDate(), overloads, validWeights(sets), validReps(sets)));
        }
        Collections.sort(result, (a, b) -> a.date.compareTo(b.date));
        return result;
    }

    private List<SessionData> loadAllTimeData() {
        TreeMap<Date, List<Double>> overloadMap = new TreeMap<>();
        TreeMap<Date, List<Double>> weightMap = new TreeMap<>();
        TreeMap<Date, List<Double>> repsMap = new TreeMap<>();
        List<Exercise> exercises = db.exerciseDao().getExercisesForEA(exerciseAbsId);
        for (Exercise ex : exercises) {
            Workout w = db.workoutDao().getWorkout(ex.getId_workout());
            if (w == null || w.getDate() == null) continue;
            List<Set> sets = db.setDao().getSetsForExercise(ex.getId());
            List<Double> overloads = validOverloads(sets);
            if (overloads.isEmpty()) continue;
            Date date = w.getDate();
            overloadMap.computeIfAbsent(date, k -> new ArrayList<>()).addAll(overloads);
            weightMap.computeIfAbsent(date, k -> new ArrayList<>()).addAll(validWeights(sets));
            repsMap.computeIfAbsent(date, k -> new ArrayList<>()).addAll(validReps(sets));
        }
        List<SessionData> result = new ArrayList<>();
        for (Date date : overloadMap.keySet()) {
            result.add(new SessionData(
                    date,
                    overloadMap.get(date),
                    weightMap.getOrDefault(date, new ArrayList<>()),
                    repsMap.getOrDefault(date, new ArrayList<>())
            ));
        }
        return result;
    }

    private List<Double> validOverloads(List<Set> sets) {
        List<Double> result = new ArrayList<>();
        for (Set s : sets) {
            if (s.getWeight() > 0 && s.getReps() > 0) result.add(s.getWeight() * s.getReps());
        }
        return result;
    }

    private List<Double> validWeights(List<Set> sets) {
        List<Double> result = new ArrayList<>();
        for (Set s : sets) {
            if (s.getWeight() > 0 && s.getReps() > 0) result.add(s.getWeight());
        }
        return result;
    }

    private List<Double> validReps(List<Set> sets) {
        List<Double> result = new ArrayList<>();
        for (Set s : sets) {
            if (s.getWeight() > 0 && s.getReps() > 0) result.add((double) s.getReps());
        }
        return result;
    }

    private void updateChart(List<SessionData> sessions) {
        xLabels.clear();

        if (sessions.isEmpty()) {
            chart.clear();
            chart.invalidate();
            statSessions.setText("0");
            statBest.setText("—");
            statAvg.setText("—");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MMM d", Locale.getDefault());
        List<Entry> scatterEntries = new ArrayList<>();
        List<Entry> lineEntries = new ArrayList<>();
        double globalBestAvg = 0, globalSumAllSets = 0;
        int globalSetCount = 0;

        for (int i = 0; i < sessions.size(); i++) {
            SessionData session = sessions.get(i);
            xLabels.add(sdf.format(session.date));
            for (double overload : session.setOverloads) {
                scatterEntries.add(new Entry(i, (float) overload));
                globalSumAllSets += overload;
                globalSetCount++;
            }
            double sessionAvg = session.setOverloads.stream()
                    .mapToDouble(Double::doubleValue).average().orElse(0);
            lineEntries.add(new Entry(i, (float) sessionAvg));
            if (sessionAvg > globalBestAvg) globalBestAvg = sessionAvg;
        }

        ScatterDataSet scatterSet = new ScatterDataSet(scatterEntries, "Set overload");
        scatterSet.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        scatterSet.setScatterShapeSize(10f);
        scatterSet.setColor(ContextCompat.getColor(this, R.color.accent_color));
        scatterSet.setDrawValues(false);

        LineDataSet lineSet = new LineDataSet(lineEntries, "Session avg");
        lineSet.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
        lineSet.setLineWidth(2.5f);
        lineSet.setCircleColor(ContextCompat.getColor(this, R.color.colorPrimary));
        lineSet.setCircleRadius(4f);
        lineSet.setDrawValues(false);
        lineSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        CombinedData combinedData = new CombinedData();
        combinedData.setData(new ScatterData(scatterSet));
        combinedData.setData(new LineData(lineSet));

        chart.getXAxis().setLabelCount(Math.min(sessions.size(), 7), false);
        chart.setData(combinedData);
        chart.invalidate();

        statSessions.setText(String.valueOf(sessions.size()));
        statBest.setText(formatLoad(globalBestAvg));
        statAvg.setText(globalSetCount > 0 ? formatLoad(globalSumAllSets / globalSetCount) : "—");
    }

    private void updateRepsWeightCharts(List<SessionData> sessions) {
        xLabels.clear();

        if (sessions.isEmpty()) {
            weightChart.clear();
            weightChart.invalidate();
            repsChart.clear();
            repsChart.invalidate();
            statSessions.setText("0");
            statBest.setText("—");
            statAvg.setText("—");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MMM d", Locale.getDefault());
        List<Entry> weightEntries = new ArrayList<>();
        List<Entry> repsEntries = new ArrayList<>();
        double totalWeight = 0, totalReps = 0, bestAvgWeight = 0;
        int weightCount = 0, repsCount = 0;

        for (int i = 0; i < sessions.size(); i++) {
            SessionData session = sessions.get(i);
            xLabels.add(sdf.format(session.date));

            if (!session.weights.isEmpty()) {
                double avgW = session.weights.stream().mapToDouble(Double::doubleValue).average().orElse(0);
                weightEntries.add(new Entry(i, (float) avgW));
                if (avgW > bestAvgWeight) bestAvgWeight = avgW;
                for (double w : session.weights) { totalWeight += w; weightCount++; }
            }

            if (!session.reps.isEmpty()) {
                double avgR = session.reps.stream().mapToDouble(Double::doubleValue).average().orElse(0);
                repsEntries.add(new Entry(i, (float) avgR));
                for (double r : session.reps) { totalReps += r; repsCount++; }
            }
        }

        LineDataSet weightSet = new LineDataSet(weightEntries, "Avg Weight");
        weightSet.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
        weightSet.setLineWidth(2.5f);
        weightSet.setCircleColor(ContextCompat.getColor(this, R.color.colorPrimary));
        weightSet.setCircleRadius(4f);
        weightSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        weightSet.setDrawValues(true);
        weightSet.setValueTextSize(9f);
        weightSet.setValueTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        weightSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return value % 1 == 0
                        ? String.valueOf((int) value)
                        : String.format(Locale.getDefault(), "%.1f", value);
            }
        });

        weightChart.getXAxis().setLabelCount(Math.min(sessions.size(), 7), false);
        weightChart.setData(new LineData(weightSet));
        weightChart.invalidate();

        LineDataSet repsSet = new LineDataSet(repsEntries, "Avg Reps");
        repsSet.setColor(ContextCompat.getColor(this, R.color.accent_color));
        repsSet.setLineWidth(2.5f);
        repsSet.setCircleColor(ContextCompat.getColor(this, R.color.accent_color));
        repsSet.setCircleRadius(4f);
        repsSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        repsSet.setDrawValues(true);
        repsSet.setValueTextSize(9f);
        repsSet.setValueTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        repsSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf(Math.round(value));
            }
        });

        repsChart.getXAxis().setLabelCount(Math.min(sessions.size(), 7), false);
        repsChart.setData(new LineData(repsSet));
        repsChart.invalidate();

        statSessions.setText(String.valueOf(sessions.size()));
        statBest.setText(formatLoad(bestAvgWeight));
        statAvg.setText(repsCount > 0
                ? String.format(Locale.getDefault(), "%.1f", totalReps / repsCount)
                : "—");
    }

    private String formatLoad(double value) {
        if (value <= 0) return "—";
        return value % 1 == 0
                ? String.valueOf((int) value)
                : String.format(Locale.getDefault(), "%.1f", value);
    }

    static class SessionData {
        final Date date;
        final List<Double> setOverloads;
        final List<Double> weights;
        final List<Double> reps;

        SessionData(Date date, List<Double> setOverloads, List<Double> weights, List<Double> reps) {
            this.date = date;
            this.setOverloads = setOverloads;
            this.weights = weights;
            this.reps = reps;
        }
    }
}
