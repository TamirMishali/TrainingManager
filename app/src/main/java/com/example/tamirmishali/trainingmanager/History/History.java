package com.example.tamirmishali.trainingmanager.History;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import com.example.tamirmishali.trainingmanager.R;
import com.example.tamirmishali.trainingmanager.Routine.AddEditRoutineActivity;
import com.example.tamirmishali.trainingmanager.Routine.Routine;
import com.example.tamirmishali.trainingmanager.Routine.RoutineAdapter;
import com.example.tamirmishali.trainingmanager.Routine.RoutineViewModel;

import java.util.List;

public class History extends AppCompatActivity {
    public static final int ADD_ROUTINE_REQUEST = 1;
    public static final int EDIT_ROUTINE_REQUEST = 2;
    public static final int VIEW_WORKOUTS_REQUEST = 3;
    private RoutineViewModel routineViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setTitle("Routines");


        //Init
        RecyclerView recyclerView = findViewById(R.id.recycler_view_routine);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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

/*        //delete routine
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                AlertDialog.Builder alert = new AlertDialog.Builder(History.this);
                alert.setTitle(R.string.delete_entry_dialog_title);
                alert.setMessage(R.string.delete_routine_dialog);
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        routineViewModel.delete(adapter.getRoutineAt(viewHolder.getAdapterPosition()));
                        Toast.makeText(EditRoutines.this , "Routine deleted" , Toast.LENGTH_SHORT).show();

                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // close dialog
                        dialog.cancel();
                        adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    }
                });
                alert.show();
            }
        }).attachToRecyclerView(recyclerView);*/

/*        //Edit Routine name and date
        adapter.setOnItemLongClickListener(new RoutineAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(Routine routine) {
                Intent intent = new Intent(EditRoutines.this, AddEditRoutineActivity.class);
                intent.putExtra(AddEditRoutineActivity.EXTRA_ROUTINE_ID, routine.getUid());
                intent.putExtra(AddEditRoutineActivity.EXTRA_ROUTINE_NAME, routine.getRoutineName());
                intent.putExtra(AddEditRoutineActivity.EXTRA_ROUTINE_DATE, routine.getRoutineDate().toString());
                startActivityForResult(intent, EDIT_ROUTINE_REQUEST);
            }
        });*/

        //Edit Workouts in that routine
        adapter.setOnItemClickListener(new RoutineAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Routine routine) {
                Intent intent = new Intent(History.this, ViewPracticalWorkouts.class); //EditWorkouts
                intent.putExtra(AddEditRoutineActivity.EXTRA_ROUTINE_ID,routine.getUid());
                intent.putExtra(AddEditRoutineActivity.EXTRA_ROUTINE_NAME,routine.getRoutineName());
                startActivityForResult(intent, VIEW_WORKOUTS_REQUEST);
            }
        });
    }






    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == ADD_ROUTINE_REQUEST && resultCode == RESULT_OK){
/*            String routineName = data.getStringExtra(AddEditRoutineActivity.EXTRA_ROUTINE_NAME);
            String routineDate = data.getStringExtra(AddEditRoutineActivity.EXTRA_ROUTINE_DATE);
            Routine routine = new Routine(routineName, routineDate);//, routineDate);
            routineViewModel.insert(routine);
            Toast.makeText(this,"Routine saved", Toast.LENGTH_SHORT).show();
        }
        else if(requestCode == EDIT_ROUTINE_REQUEST && resultCode == RESULT_OK){
            //check if valid id
            int routineId = data.getIntExtra(AddEditRoutineActivity.EXTRA_ROUTINE_ID, -1);
            if (routineId == -1){
                Toast.makeText(this, "Routine can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String routineName = data.getStringExtra(AddEditRoutineActivity.EXTRA_ROUTINE_NAME);
            String routineDate = data.getStringExtra(AddEditRoutineActivity.EXTRA_ROUTINE_DATE);
            Routine routine = new Routine(routineName, routineDate);//, routineDate);
            routine.setUid(routineId);
            routineViewModel.update(routine);
            Toast.makeText(this,"Routine updated", Toast.LENGTH_SHORT).show();*/
        }
        else if (requestCode == VIEW_WORKOUTS_REQUEST && resultCode == RESULT_OK){
            Toast.makeText(this,"Workouts added", Toast.LENGTH_SHORT).show();
        }
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.editroutine_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_routines:
                //routineViewModel.deleteAllRoutines();
                Toast.makeText(this, "All routines deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }*/



}
