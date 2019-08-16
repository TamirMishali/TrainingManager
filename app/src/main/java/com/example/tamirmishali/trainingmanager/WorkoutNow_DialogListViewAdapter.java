package com.example.tamirmishali.trainingmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.tamirmishali.trainingmanager.Workout.Workout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//Reference: https://www.youtube.com/watch?v=Z7oekIFb7fA
public class WorkoutNow_DialogListViewAdapter extends BaseAdapter {

    Context context;
    List<Workout> workouts;
    SimpleDateFormat formatter= new SimpleDateFormat("dd-MM-yyyy 'at' HH:mm");
    Date d;

    WorkoutNow_DialogListViewAdapter(Context context, List<Workout> workouts){
        this.context = context;
        this.workouts = workouts;
    }

    @Override
    public int getCount() {
        return workouts.size();
    }

    @Override
    public Object getItem(int position) {
        return workouts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i , View view, ViewGroup viewGroup){

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.workoutnow_listview_item,viewGroup,false);
        TextView textViewWorkoutName = (TextView)row.findViewById(R.id.workoutnow_listview_item_workout_name);
        TextView textViewWorkoutDate = (TextView)row.findViewById(R.id.workoutnow_listview_item_workout_date);
        Workout workout = workouts.get(i);

        textViewWorkoutName.setText(workout.getName());
        if(workout.getDate() != null){
            d = new Date(workout.getDate().getTime());
            textViewWorkoutDate.setText(formatter.format(d));
        }
        else
            textViewWorkoutDate.setText("First time - No date");

        return row;
    }

}
