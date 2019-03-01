package com.example.tamirmishali.trainingmanager;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tamirmishali.trainingmanager.Database.RoutineRepository;

import java.util.ArrayList;
import java.util.List;

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.RoutineHolder> {

    private List<Routine> routines = new ArrayList<>();
    //private RoutineRepository repository = new RoutineRepository();
    private List<Workout> workouts = new ArrayList<>();
    private Context context;


/*    //declare interface
    private OnItemClicked onClick;

    //make interface like this
    public interface OnItemClicked {
        void onItemClick(int position);
    }

    public RoutineAdapter(Context context,List<Routine> r) {
        this.context = context;
        this.routines = r;
    }
    public void setOnClick(OnItemClicked onClick) {
        this.onClick=onClick;
    }*/

    @NonNull
    @Override
    public RoutineHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.routine_item,parent,false);
        //itemView.setOnClickListener(mOnClickListener);
        return new RoutineHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RoutineHolder holder, final int position) {

/*        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onItemClick(position);
                getWorkoutsForRoutine
            }
        });*/
        Routine curretRoutine = routines.get(position);
        //workouts.getWorkoutsForRoutine(curretRoutine.getUid());
        //holder.textViewRoutine.setText(curretRoutine.getRoutineDate().toString());
        holder.textViewroutinedate.setText(curretRoutine.getRoutineDate().toString());
        holder.textViewDescription.setText(curretRoutine.getExercise());
        //holder.textViewDescription.setText(String.valueof(currentRoutine.getpriority())));
        holder.itemView.setOnClickListener(mClickListener);
        holder.itemView.setTag(holder);
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RoutineHolder holder = (RoutineHolder) view.getTag();
            int position = holder.getAdapterPosition();

            Toast.makeText(view.getContext(), "worked?",Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public int getItemCount() {
        return routines.size();
    }

    public void setRoutines(List<Routine> routines){
        this.routines=routines;
        notifyDataSetChanged(); //use itemchanged later
    }
    public void setWorkouts(List<Workout> workouts){
        this.workouts=workouts;
        notifyDataSetChanged(); //use itemchanged later
    }

 /*   @Override
    public void onClick(final View view) {
        int itemPosition = mRecyclerView.getChildLayoutPosition(view);
        String item = mList.get(itemPosition);
        Toast.makeText(mContext, item, Toast.LENGTH_LONG).show();
    }*/

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
