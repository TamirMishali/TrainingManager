package com.example.tamirmishali.trainingmanager.ExerciseAbstract;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.tamirmishali.trainingmanager.Exercise.Exercise;
import com.example.tamirmishali.trainingmanager.R;

import java.util.ArrayList;
import java.util.List;

public class ExerciseAbstractAdapter extends RecyclerView.Adapter<ExerciseAbstractAdapter.ExerciseAbstractHolder> implements Filterable {

    private List<ExerciseAbstract> exerciseAbstracts = new ArrayList<>();
    private List<ExerciseAbstract> exerciseAbstractsFull;// = new ArrayList<>();
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
        exerciseAbstractsFull = new ArrayList<>(exerciseAbstracts);
        notifyDataSetChanged(); //use itemchanged later
    }

    public ExerciseAbstract getExerciseAbstractAt(int position) {
        return exerciseAbstracts.get(position);
    }

    //-------------------------Filter-------------------------
    @Override
    public Filter getFilter() {
        return ExerciseAbstractFilter;
    }

    private Filter ExerciseAbstractFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ExerciseAbstract> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exerciseAbstractsFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ExerciseAbstract item : exerciseAbstractsFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            exerciseAbstracts.clear();
            exerciseAbstracts.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };




    //--------------------------------------------------------
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
