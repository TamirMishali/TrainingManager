package com.example.tamirmishali.trainingmanager.Routine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tamirmishali.trainingmanager.PreferenceUtils;
import com.example.tamirmishali.trainingmanager.R;
import com.example.tamirmishali.trainingmanager.Workout.Workout;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// ToDo (MEDIUM) add a checkbox in each routine row to choose the default routine. only one can be marked at all times.

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.RoutineHolder> {

    private List<Routine> routines = new ArrayList<>();
    private OnItemLongClickListener longListener;
    private OnItemClickListener listener;
    //private RoutineRepository repository = new RoutineRepository();
    private List<Workout> workouts = new ArrayList<>();
    private Context context;

    @NonNull
    @Override
    public RoutineHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.routine_item, parent, false);
        return new RoutineHolder(itemView);
    }


    @NonNull
    @Override
    public void onBindViewHolder(@NonNull RoutineHolder holder, final int position) {
        Routine currentRoutine = routines.get(position);
        holder.textViewRoutine.setText(currentRoutine.getRoutineName());

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date d = new Date(currentRoutine.getRoutineDate().getTime());
        holder.textViewroutinedate.setText(formatter.format(d));

        holder.textViewDescription.setText(""); // Optional: add "# Workouts" here

        // Get the main routine ID from SharedPreferences
        int mainRoutineId = PreferenceUtils.getMainRoutineId(holder.itemView.getContext());

        // Change stroke color based on whether it's the main routine
        if (currentRoutine.getUid() == mainRoutineId) {
            holder.cardView.setStrokeColor(android.graphics.Color.RED);
            holder.cardView.setStrokeWidth(6);  // you can adjust thickness
        } else {
            holder.cardView.setStrokeColor(android.graphics.Color.LTGRAY);
            holder.cardView.setStrokeWidth(2);  // default
        }
    }


    @Override
    public int getItemCount() {
        return routines.size();
    }

    public void setRoutines(List<Routine> routines) {
        this.routines = routines;
        notifyDataSetChanged(); //use itemchanged later
    }

    public Routine getRoutineAt(int position) {
        return routines.get(position);
    }

    public void setWorkouts(List<Workout> workouts) {
        this.workouts = workouts;
        notifyDataSetChanged(); //use itemchanged later
    }

 /*   @Override
    public void onClick(final View view) {
        int itemPosition = mRecyclerView.getChildLayoutPosition(view);
        String item = mList.get(itemPosition);
        Toast.makeText(mContext, item, Toast.LENGTH_LONG).show();
    }*/

    class RoutineHolder extends RecyclerView.ViewHolder {
        private TextView textViewRoutine;
        private TextView textViewroutinedate;
        private TextView textViewDescription;
        private MaterialCardView cardView;


        public RoutineHolder(View itemView) {
            super(itemView);
            textViewRoutine = itemView.findViewById(R.id.text_view_routinetext);
            textViewroutinedate = itemView.findViewById(R.id.text_view_routinedate);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            cardView = itemView.findViewById(R.id.card_view);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(routines.get(position));
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v){
                    int position = getAdapterPosition();
                    if(longListener != null && position != RecyclerView.NO_POSITION) {
                        longListener.onItemLongClick(routines.get(position));
                    }
                    return true;
                }
            });
        }
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Routine routine);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longListener = listener;
    }


    public interface OnItemClickListener {
        void onItemClick(Routine routine);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;

    }
}
