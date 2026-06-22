package com.example.tamirmishali.trainingmanager.Dashboard;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.tamirmishali.trainingmanager.Database.TrainingManagerDatabase;
import com.example.tamirmishali.trainingmanager.Exercise.Exercise;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstract;
import com.example.tamirmishali.trainingmanager.R;
import com.example.tamirmishali.trainingmanager.Routine.Routine;
import com.example.tamirmishali.trainingmanager.Set.Set;
import com.example.tamirmishali.trainingmanager.Workout.Workout;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class DashboardActivity extends AppCompatActivity {

    private static final int[] MUSCLE_ORDER = {1, 2, 3, 4, 5, 6, 7};
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

    private static final long DAY_MS = 24L * 60 * 60 * 1000;
    private final SimpleDateFormat dateFmt = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

    private TrainingManagerDatabase db;

    private Spinner routineSpinner;
    private Button startDateButton, endDateButton;
    private TextView daysBetweenView, summaryView, totalVolumeView;
    private BarChart workoutsMonthChart, muscleChart, hourChart;
    private LineChart volumeMonthChart;

    private List<Routine> routines = new ArrayList<>();
    private int selectedRoutineId = -1;
    private long startMillis;
    private long endMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        setTitle("Dashboard");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = TrainingManagerDatabase.getInstance(getApplicationContext());

        routineSpinner = findViewById(R.id.dashboard_routine_spinner);
        startDateButton = findViewById(R.id.dashboard_start_date);
        endDateButton = findViewById(R.id.dashboard_end_date);
        daysBetweenView = findViewById(R.id.dashboard_days_between);
        summaryView = findViewById(R.id.dashboard_summary);
        totalVolumeView = findViewById(R.id.dashboard_total_volume);
        workoutsMonthChart = findViewById(R.id.dashboard_chart_workouts_month);
        muscleChart = findViewById(R.id.dashboard_chart_muscle);
        volumeMonthChart = findViewById(R.id.dashboard_chart_volume_month);
        hourChart = findViewById(R.id.dashboard_chart_hour);

        startDateButton.setOnClickListener(v -> pickDate(true));
        endDateButton.setOnClickListener(v -> pickDate(false));

        loadRoutines();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    // ---- Setup ----

    private void loadRoutines() {
        new Thread(() -> {
            List<Routine> loaded = db.routineDao().getAllRoutinesList();
            if (loaded == null) loaded = new ArrayList<>();
            final List<Routine> finalLoaded = loaded;
            runOnUiThread(() -> {
                if (isFinishing() || isDestroyed()) return;
                routines = finalLoaded;
                if (routines.isEmpty()) {
                    Toast.makeText(this, "No routines yet", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<String> names = new ArrayList<>();
                for (Routine r : routines) names.add(r.getRoutineName());
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        this, android.R.layout.simple_spinner_dropdown_item, names);
                routineSpinner.setAdapter(adapter);
                routineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        onRoutineSelected(routines.get(position));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) { }
                });
                // Triggers onItemSelected for position 0 → initial render.
            });
        }).start();
    }

    private void onRoutineSelected(Routine routine) {
        selectedRoutineId = routine.getUid();
        // Defaults: start = routine start date, end = now.
        long routineStart = routine.getRoutineDate() != null
                ? routine.getRoutineDate().getTime() : System.currentTimeMillis();
        startMillis = startOfDay(routineStart);
        endMillis = endOfDay(System.currentTimeMillis());
        refreshDateButtons();
        recompute();
    }

    private void pickDate(boolean isStart) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(isStart ? startMillis : endMillis);
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, day) -> {
            Calendar picked = Calendar.getInstance();
            picked.set(year, month, day, 0, 0, 0);
            if (isStart) {
                startMillis = startOfDay(picked.getTimeInMillis());
            } else {
                endMillis = endOfDay(picked.getTimeInMillis());
            }
            refreshDateButtons();
            recompute();
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void refreshDateButtons() {
        startDateButton.setText("From: " + dateFmt.format(new Date(startMillis)));
        endDateButton.setText("To: " + dateFmt.format(new Date(endMillis)));
        long days = Math.round((endMillis - startMillis) / (double) DAY_MS);
        daysBetweenView.setText(days + " days");
    }

    // ---- Aggregation ----

    private void recompute() {
        final int routineId = selectedRoutineId;
        final long start = startMillis;
        final long end = endMillis;
        new Thread(() -> {
            Stats stats = computeStats(routineId, start, end);
            runOnUiThread(() -> {
                if (isFinishing() || isDestroyed()) return;
                render(stats);
            });
        }).start();
    }

    private Stats computeStats(int routineId, long start, long end) {
        Stats s = new Stats();
        List<Workout> all = db.workoutDao().getPracticalWorkoutsForRoutine(routineId);
        if (all == null) return s;

        for (Workout w : all) {
            if (w.getDate() == null) continue;
            long t = w.getDate().getTime();
            if (t < start || t > end) continue;

            s.totalWorkouts++;
            if (t < s.firstWorkoutMillis) s.firstWorkoutMillis = t;
            if (t > s.lastWorkoutMillis) s.lastWorkoutMillis = t;

            String monthKey = new SimpleDateFormat("yyyy-MM", Locale.US).format(new Date(t));
            s.workoutsPerMonth.merge(monthKey, 1, Integer::sum);

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(t);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
            int second = cal.get(Calendar.SECOND);
            // Exact midnight means the workout was added/edited via the date picker
            // (no real clock time recorded), so it is excluded from the time-of-day chart.
            if (!(hour == 0 && minute == 0 && second == 0)) {
                s.hourCount[hour]++;
                s.timedWorkouts++;
            }

            double workoutVolume = 0;
            List<Exercise> exercises = db.exerciseDao().getExercisesForWorkout(w.getId());
            if (exercises != null) {
                for (Exercise ex : exercises) {
                    ExerciseAbstract ea =
                            db.exerciseAbstractDao().getExerciseAbsFromId(ex.getId_exerciseabs());
                    int muscleId = ea != null ? ea.getId_muscle() : 0;
                    List<Set> sets = db.setDao().getSetsForExercise(ex.getId());
                    int doneSets = 0;
                    if (sets != null) {
                        for (Set set : sets) {
                            if (set.getReps() > 0) {           // reps>0 → a performed set (-1 unfilled, -2 N/A)
                                doneSets++;
                                if (set.getWeight() > 0) {
                                    workoutVolume += set.getWeight() * set.getReps();
                                }
                            }
                        }
                    }
                    if (muscleId > 0) {
                        s.setsPerMuscle.merge(muscleId, doneSets, Integer::sum);
                    }
                }
            }
            s.totalVolume += workoutVolume;
            s.volumePerMonth.merge(monthKey, workoutVolume, Double::sum);
        }
        return s;
    }

    // ---- Rendering ----

    private void render(Stats s) {
        // Average per week is measured over the ACTIVE window (first → last workout
        // in range), not the whole selected span, so empty stretches before you
        // started or after you moved on don't dilute it.
        double avgPerWeek = 0;
        if (s.totalWorkouts > 0) {
            long spanMs = s.lastWorkoutMillis - s.firstWorkoutMillis;
            // Inclusive day count; at least 1 day so a single week of training
            // doesn't blow up to a huge per-week figure.
            double activeDays = Math.max(spanMs / (double) DAY_MS + 1, 1.0);
            double activeWeeks = activeDays / 7.0;
            avgPerWeek = s.totalWorkouts / activeWeeks;
        }
        summaryView.setText(String.format(Locale.US,
                "Workouts: %d     •     Avg/week: %.1f", s.totalWorkouts, avgPerWeek));
        totalVolumeView.setText(String.format(Locale.US,
                "Total volume: %,.0f kg", s.totalVolume));

        renderMonthBar(workoutsMonthChart, s.workoutsPerMonth);
        renderMuscleBar(muscleChart, s.setsPerMuscle);
        renderVolumeLine(volumeMonthChart, s.volumePerMonth);
        renderHourBar(hourChart, s.hourCount, s.timedWorkouts);
    }

    private void renderMonthBar(BarChart chart, TreeMap<String, Integer> perMonth) {
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        int i = 0;
        for (Map.Entry<String, Integer> e : perMonth.entrySet()) {
            entries.add(new BarEntry(i, e.getValue()));
            labels.add(monthLabel(e.getKey()));
            i++;
        }
        BarDataSet ds = new BarDataSet(entries, "Workouts");
        ds.setColor(Color.parseColor("#4CAF50"));
        ds.setValueTextSize(10f);
        ds.setValueTextColor(ContextCompat.getColor(this, R.color.chart_value_text));
        styleBar(chart, labels);
        chart.setData(entries.isEmpty() ? null : new BarData(ds));
        chart.invalidate();
    }

    private void renderMuscleBar(BarChart chart, Map<Integer, Integer> setsPerMuscle) {
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        int i = 0;
        for (int muscleId : MUSCLE_ORDER) {
            Integer count = setsPerMuscle.get(muscleId);
            entries.add(new BarEntry(i, count != null ? count : 0));
            labels.add(MUSCLE_NAMES.get(muscleId));
            i++;
        }
        BarDataSet ds = new BarDataSet(entries, "Sets");
        ds.setColor(Color.parseColor("#2196F3"));
        ds.setValueTextSize(10f);
        ds.setValueTextColor(ContextCompat.getColor(this, R.color.chart_value_text));
        styleBar(chart, labels);
        chart.setData(new BarData(ds));
        chart.invalidate();
    }

    private void renderVolumeLine(LineChart chart, TreeMap<String, Double> volumePerMonth) {
        List<Entry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        int i = 0;
        for (Map.Entry<String, Double> e : volumePerMonth.entrySet()) {
            entries.add(new Entry(i, e.getValue().floatValue()));
            labels.add(monthLabel(e.getKey()));
            i++;
        }
        LineDataSet ds = new LineDataSet(entries, "Volume (kg)");
        ds.setColor(Color.parseColor("#FF7043"));
        ds.setCircleColor(Color.parseColor("#FF7043"));
        ds.setLineWidth(2f);
        ds.setValueTextSize(9f);
        ds.setValueTextColor(ContextCompat.getColor(this, R.color.chart_value_text));

        int axisText = ContextCompat.getColor(this, R.color.chart_axis_text);
        chart.setBackgroundColor(ContextCompat.getColor(this, R.color.chart_surface));
        chart.getDescription().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setScaleYEnabled(false);
        chart.getAxisLeft().setTextColor(axisText);
        XAxis x = chart.getXAxis();
        x.setTextColor(axisText);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setGranularity(1f);
        x.setDrawGridLines(false);
        x.setLabelRotationAngle(-45f);
        x.setValueFormatter(new IndexAxisValueFormatter(labels));
        chart.setData(entries.isEmpty() ? null : new LineData(ds));
        chart.invalidate();
    }

    private void renderHourBar(BarChart chart, int[] hourCount, int timedWorkouts) {
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        for (int h = 0; h < 24; h++) {
            entries.add(new BarEntry(h, hourCount[h]));
            labels.add(String.format(Locale.US, "%02d", h));
        }
        BarDataSet ds = new BarDataSet(entries, "Start hour");
        ds.setColor(Color.parseColor("#9C27B0"));
        ds.setValueTextSize(8f);
        ds.setValueTextColor(ContextCompat.getColor(this, R.color.chart_value_text));
        styleBar(chart, labels);
        chart.setNoDataTextColor(ContextCompat.getColor(this, R.color.chart_axis_text));
        chart.setData(timedWorkouts == 0 ? null : new BarData(ds));
        if (timedWorkouts == 0) {
            chart.setNoDataText("No recorded start times yet");
        }
        chart.invalidate();
    }

    private void styleBar(BarChart chart, List<String> labels) {
        int axisText = ContextCompat.getColor(this, R.color.chart_axis_text);
        chart.setBackgroundColor(ContextCompat.getColor(this, R.color.chart_surface));
        chart.getDescription().setEnabled(false);
        chart.setScaleYEnabled(false);
        chart.setFitBars(true);
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setTextColor(axisText);
        chart.getLegend().setEnabled(false);
        XAxis x = chart.getXAxis();
        x.setTextColor(axisText);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setGranularity(1f);
        x.setDrawGridLines(false);
        x.setLabelRotationAngle(-45f);
        x.setValueFormatter(new IndexAxisValueFormatter(labels));
    }

    private String monthLabel(String yyyyMM) {
        // "2026-06" → "06/26"
        String[] parts = yyyyMM.split("-");
        if (parts.length == 2 && parts[0].length() == 4) {
            return parts[1] + "/" + parts[0].substring(2);
        }
        return yyyyMM;
    }

    private long startOfDay(long millis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }

    private long endOfDay(long millis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTimeInMillis();
    }

    // ---- Holder ----

    private static class Stats {
        int totalWorkouts = 0;
        int timedWorkouts = 0;
        long firstWorkoutMillis = Long.MAX_VALUE;
        long lastWorkoutMillis = Long.MIN_VALUE;
        double totalVolume = 0;
        final TreeMap<String, Integer> workoutsPerMonth = new TreeMap<>();
        final TreeMap<String, Double> volumePerMonth = new TreeMap<>();
        final Map<Integer, Integer> setsPerMuscle = new HashMap<>();
        final int[] hourCount = new int[24];
    }
}
