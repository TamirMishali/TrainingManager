package com.example.tamirmishali.trainingmanager.Workout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tamirmishali.trainingmanager.R;

import java.util.Calendar;

public class AddEditWorkoutActivity extends AppCompatActivity {
    public static final  String EXTRA_WORKOUT_ID =
            "com.example.tamirmishali.trainingmanager.EXTRA_WORKOUT_ID";
    public static final  String EXTRA_WORKOUT_NAME =
            "com.example.tamirmishali.trainingmanager.EXTRA_WORKOUT_NAME";
    public static final  String EXTRA_WORKOUT_DATE =
            "com.example.tamirmishali.trainingmanager.EXTRA_WORKOUT_DATE";

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private EditText editTextWorkoutName;
    private int routineId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);

        editTextWorkoutName = findViewById(R.id.edit_text_workout_name);
        mDisplayDate = findViewById(R.id.text_view_workout_date);
        //mDisplayDate.setPaintFlags(mDisplayDate.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);//for underline

        //check why we got here: Edit/Add
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_WORKOUT_ID)) {
            setTitle("Edit workout");
            editTextWorkoutName.setText(intent.getStringExtra(EXTRA_WORKOUT_NAME));
            mDisplayDate.setText(intent.getStringExtra(EXTRA_WORKOUT_DATE));
            findViewById(R.id.text_view_workout_date_text).setVisibility(View.GONE);

        }else{
            setTitle("Add workout");
            Calendar calendar = Calendar.getInstance();
            mDisplayDate.setText(new java.sql.Date(calendar.getTime().getTime()).toString());
            mDisplayDate.setVisibility(View.GONE);
            findViewById(R.id.text_view_workout_date_text).setVisibility(View.GONE);

        }

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);


        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddEditWorkoutActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String dateString = year + "-" + month + "-" + day;
                //Log.d(TAG, "onDateSet: dd/mm/yyyy: " + dateString);

                mDisplayDate.setText(dateString);


            }
        };
    }

    private void saveWorkout(){
        String workoutName = editTextWorkoutName.getText().toString();
        String workoutDate = mDisplayDate.getText().toString();

        if (workoutName.trim().isEmpty() || workoutDate.trim().equals("Select Date")){
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_WORKOUT_NAME,workoutName);
        data.putExtra(EXTRA_WORKOUT_DATE,workoutDate);

        int id = getIntent().getIntExtra(EXTRA_WORKOUT_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_WORKOUT_ID, id);
        }
        setResult(RESULT_OK,data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_workout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_workout:
                saveWorkout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

