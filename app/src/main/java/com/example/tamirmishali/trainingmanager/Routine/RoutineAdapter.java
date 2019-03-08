package com.example.tamirmishali.trainingmanager.Routine;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tamirmishali.trainingmanager.R;
import com.example.tamirmishali.trainingmanager.Workout.Workout;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void onBindViewHolder(@NonNull RoutineHolder holder, final int position) {

        Routine curretRoutine = routines.get(position);
        //workouts.getWorkoutsForRoutine(curretRoutine.getUid());
        holder.textViewRoutine.setText(curretRoutine.getRoutineName());
        holder.textViewroutinedate.setText(curretRoutine.getRoutineDate().toString());
        holder.textViewDescription.setText("empty for now");
        //holder.textViewDescription.setText(String.valueof(currentRoutine.getpriority())));
        //holder.itemView.setOnClickListener(mClickListener);
        holder.itemView.setTag(holder);
    }

    /*View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RoutineHolder holder = (RoutineHolder) view.getTag();
            int position = holder.getAdapterPosition();

            Toast.makeText(view.getContext(), "worked?", Toast.LENGTH_SHORT).show();
        }
    };*/

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


        public RoutineHolder(View itemView) {
            super(itemView);
            textViewRoutine = itemView.findViewById(R.id.text_view_routinetext);
            textViewroutinedate = itemView.findViewById(R.id.text_view_routinedate);
            textViewDescription = itemView.findViewById(R.id.text_view_description);

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
