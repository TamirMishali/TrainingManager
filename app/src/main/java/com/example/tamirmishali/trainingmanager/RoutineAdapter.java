package com.example.tamirmishali.trainingmanager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.RoutineHolder> {

    private List<Routine> routines = new ArrayList<>();

    @NonNull
    @Override
    public RoutineHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.routine_item,parent,false);
        return new RoutineHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RoutineHolder holder, int position) {
        Routine curretRoutine = routines.get(position);
        holder.textViewRoutine.setText(curretRoutine.getRoutineDate());
        holder.textViewroutinedate.setText(curretRoutine.getRoutineDate());
        holder.textViewDescription.setText(curretRoutine.getExercise());
        //holder.textViewDescription.setText(String.valueof(currentRoutine.getpriority())));
    }

    @Override
    public int getItemCount() {
        return routines.size();
    }

    public void setRoutines(List<Routine> routines){
        this.routines=routines;
        notifyDataSetChanged(); //use itemchanged later
    }

    class RoutineHolder extends RecyclerView.ViewHolder{
        private TextView textViewRoutine;
        private TextView textViewroutinedate;
        private TextView textViewDescription;


        public RoutineHolder(View itemView) {
            super(itemView);
            textViewRoutine = itemView.findViewById(R.id.text_view_routinetext);
            textViewroutinedate = itemView.findViewById(R.id.text_view_routinedate);
            textViewDescription = itemView.findViewById(R.id.text_view_description);

        }
    }
}
