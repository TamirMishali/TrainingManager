package com.example.tamirmishali.trainingmanager.Routine;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tamirmishali.trainingmanager.R;

import java.util.Calendar;

public class AddEditRoutineActivity extends AppCompatActivity {
    public static final String EXTRA_ROUTINE_ID =
            "com.example.tamirmishali.trainingmanager.EXTRA_ROUTINE_ID";
    public static final String EXTRA_ROUTINE_NAME =
            "com.example.tamirmishali.trainingmanager.EXTRA_ROUTINE_NAME";
    public static final String EXTRA_ROUTINE_DATE =
            "com.example.tamirmishali.trainingmanager.EXTRA_ROUTINE_DATE";

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private EditText editTextRoutineName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_routine);

        editTextRoutineName = findViewById(R.id.edit_text_routine_name);
        mDisplayDate = findViewById(R.id.text_view_routine_date);
        //mDisplayDate.setPaintFlags(mDisplayDate.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);//for underline

        //check why we got here: Edit/Add
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ROUTINE_ID)) {
            setTitle("Edit routine");
            editTextRoutineName.setText(intent.getStringExtra(EXTRA_ROUTINE_NAME));
            mDisplayDate.setText(intent.getStringExtra(EXTRA_ROUTINE_DATE));
        }else{
            setTitle("Add routine");
            Calendar calendar = Calendar.getInstance();
            mDisplayDate.setText(new java.sql.Date(calendar.getTime().getTime()).toString());
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
                        AddEditRoutineActivity.this,
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
                mDisplayDate.setText(dateString);
            }
        };
    }

    private void saveRoutine(){
        String routineName = editTextRoutineName.getText().toString();
        String routineDate = mDisplayDate.getText().toString();

        if (routineName.trim().isEmpty() || routineDate.trim().equals("Select Date")){
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_ROUTINE_NAME,routineName);
        data.putExtra(EXTRA_ROUTINE_DATE,routineDate);

        int id = getIntent().getIntExtra(EXTRA_ROUTINE_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ROUTINE_ID,Integer.toString(id));
        }
        setResult(RESULT_OK,data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_routine_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_routine:
                saveRoutine();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

