package com.example.tamirmishali.trainingmanager;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.tamirmishali.trainingmanager.Database.DAOs.RoutineDao;

import java.util.List;

public class EditWorkout extends AppCompatActivity {
    public static final int ADD_ROUTINE_REQUEST = 1;
    private RoutineViewModel routineViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editworkout_layout);

        FloatingActionButton buttonAddRoutine = findViewById(R.id.button_add_routine);
        buttonAddRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditWorkout.this,AddRoutineActivity.class);
                startActivityForResult(intent,ADD_ROUTINE_REQUEST);
            }
        });

        //Init
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.setHasFixedSize(true);

        //RoutineAdapter Decleration
        final RoutineAdapter adapter = new RoutineAdapter();
        recyclerView.setAdapter(adapter);

        //RoutineViewModel Decleration
        routineViewModel = ViewModelProviders.of(this).get(RoutineViewModel.class);
        routineViewModel.getAllRoutines().observe(this, new Observer<List<Routine>>() {
            @Override
            public void onChanged(@Nullable List<Routine> routines) {
                adapter.setRoutines(routines);
            }
        });


        //routineViewModel.insert(new Routine("11.22.33","B C K"));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == ADD_ROUTINE_REQUEST && resultCode == RESULT_OK){
            String routineName = data.getStringExtra(AddRoutineActivity.EXTRA_ROUTINE_NAME);
            String routineDate = data.getStringExtra(AddRoutineActivity.EXTRA_ROUTINE_DATE);

            Routine routine = new Routine(routineName, routineDate);//, routineDate);
            routineViewModel.insert(routine);

            Toast.makeText(this,"Routine saved", Toast.LENGTH_SHORT).show();
        }

        else {
            Toast.makeText(this,"Routine NOT saved", Toast.LENGTH_SHORT).show();
        }


    }

}
