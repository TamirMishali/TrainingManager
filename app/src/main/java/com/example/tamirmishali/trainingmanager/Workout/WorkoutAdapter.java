package com.example.tamirmishali.trainingmanager.Workout;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tamirmishali.trainingmanager.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutHolder> {

    private List<Workout> workouts = new ArrayList<>();
    private OnItemLongClickListener longListener;
    private OnItemClickListener listener;

    @NonNull
    @Override
    public WorkoutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.workout_item, parent, false);
        return new WorkoutHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutHolder holder, final int position) {
        Workout currentWorkout = workouts.get(position);
        holder.textViewWorkout.setText(currentWorkout.getWorkoutName());

        if(currentWorkout.getDate() != null){
            SimpleDateFormat formatter= new SimpleDateFormat("dd-MM-yyyy 'at' HH:mm");
            Date d = new Date(currentWorkout.getDate().getTime());
            holder.textViewWorkoutExercises.setText(formatter.format(d));
        }
        else {
            holder.textViewWorkoutExercises.setText("");

        }
        holder.itemView.setTag(holder);
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    public void setWorkouts(List<Workout> workouts) {
        this.workouts = workouts;
        notifyDataSetChanged(); //use itemchanged later
    }

/*    public void setMussles(List<String> mussles){
        this.mussles = mussles;
        notifyDataSetChanged();
    }*/

    public Workout getWorkoutAt(int position) {
        return workouts.get(position);
    }


    class WorkoutHolder extends RecyclerView.ViewHolder {
        private TextView textViewWorkout;
        private TextView textViewWorkoutExercises;


        public WorkoutHolder(View itemView) {
            super(itemView);
            textViewWorkout = itemView.findViewById(R.id.text_view_workout_text);
            textViewWorkoutExercises = itemView.findViewById(R.id.text_view_workout_exercises);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(workouts.get(position));
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v){
                    int position = getAdapterPosition();
                    if(longListener != null && position != RecyclerView.NO_POSITION) {
                        longListener.onItemLongClick(workouts.get(position));
                    }
                    return true;
                }
            });
        }
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Workout workout);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longListener = listener;
    }


    public interface OnItemClickListener {
        void onItemClick(Workout workout);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;

    }
}
