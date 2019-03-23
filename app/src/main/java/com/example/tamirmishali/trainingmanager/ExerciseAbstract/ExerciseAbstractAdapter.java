package com.example.tamirmishali.trainingmanager.ExerciseAbstract;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tamirmishali.trainingmanager.R;

import java.util.ArrayList;
import java.util.List;

public class ExerciseAbstractAdapter extends RecyclerView.Adapter<ExerciseAbstractAdapter.ExerciseAbstractHolder> {

    private List<ExerciseAbstract> exerciseAbstracts = new ArrayList<>();
    private OnItemLongClickListener longListener;
    private OnItemClickListener listener;
    //private Context context;

    @NonNull
    @Override
    public ExerciseAbstractHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exerciseabs_item, parent, false);
        return new ExerciseAbstractHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseAbstractHolder holder, final int position) {
        ExerciseAbstract curretExerciseAbstract = exerciseAbstracts.get(position);
        holder.textViewExerciseAbstract.setText(curretExerciseAbstract.getName());
        holder.textViewExerciseAbstractDescription.setText(curretExerciseAbstract.getDescription());
        holder.textViewExerciseAbstractMussleGroup.setText(curretExerciseAbstract.getMuscleGroup());
        holder.itemView.setTag(holder);
    }

    @Override
    public int getItemCount() {
        return exerciseAbstracts.size();
    }

    public void setExerciseAbstracts(List<ExerciseAbstract> exerciseAbstracts) {
        this.exerciseAbstracts = exerciseAbstracts;
        notifyDataSetChanged(); //use itemchanged later
    }

    public ExerciseAbstract getExerciseAbstractAt(int position) {
        return exerciseAbstracts.get(position);
    }


    class ExerciseAbstractHolder extends RecyclerView.ViewHolder {
        private TextView textViewExerciseAbstract;
        private TextView textViewExerciseAbstractDescription;
        private TextView textViewExerciseAbstractMussleGroup;


        public ExerciseAbstractHolder(View itemView) {
            super(itemView);
            textViewExerciseAbstract = itemView.findViewById(R.id.text_view_exerciseabs_text);
            textViewExerciseAbstractDescription = itemView.findViewById(R.id.text_view_exerciseabs_description);
            textViewExerciseAbstractMussleGroup = itemView.findViewById(R.id.text_view_exerciseabs_musclegroup);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(exerciseAbstracts.get(position));
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v){
                    int position = getAdapterPosition();
                    if(longListener != null && position != RecyclerView.NO_POSITION) {
                        longListener.onItemLongClick(exerciseAbstracts.get(position));
                    }
                    return true;
                }
            });
        }
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(ExerciseAbstract exerciseAbstract);
    }
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ExerciseAbstract exerciseAbstract);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;

    }
}
