package com.example.tamirmishali.trainingmanager.ExerciseAbstract;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tamirmishali.trainingmanager.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class ExerciseAbstractAdapter extends RecyclerView.Adapter<ExerciseAbstractAdapter.ExerciseAbstractHolder> implements Filterable {

    private List<ExerciseAbstract> exerciseAbstracts = new ArrayList<>();
    private List<ExerciseAbstract> exerciseAbstractsFull;
    private OnItemLongClickListener longListener;
    private OnItemClickListener listener;

    @NonNull
    @Override
    public ExerciseAbstractHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exerciseabs_item, parent, false);
        return new ExerciseAbstractHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseAbstractHolder holder, final int position) {
        ExerciseAbstract currentExerciseAbstract = exerciseAbstracts.get(position);

        holder.text_view_exerciseabs_name.setText(currentExerciseAbstract.generateExerciseAbstractName());
        holder.text_view_exerciseabs_muscle_value.setText(currentExerciseAbstract.getMuscle());
        holder.text_view_exerciseabs_operation_value.setText(currentExerciseAbstract.getOperation());
        holder.text_view_exerciseabs_nickname_value.setText(currentExerciseAbstract.getNickname());
        holder.text_view_exerciseabs_load_type_value.setText(currentExerciseAbstract.getLoad_type());
        holder.text_view_exerciseabs_separate_hands_value.setText(currentExerciseAbstract.getSeparate_sides());
        holder.text_view_exerciseabs_position_value.setText(currentExerciseAbstract.getPosition());
        holder.text_view_exerciseabs_angle_value.setText(currentExerciseAbstract.getAngle());
        holder.text_view_exerciseabs_grip_width_value.setText(currentExerciseAbstract.getGrip_width());
        holder.text_view_exerciseabs_thumbs_direction_value.setText(currentExerciseAbstract.getThumbs_direction());

        holder.itemView.setTag(holder);
    }

    @Override
    public int getItemCount() {
        return exerciseAbstracts.size();
    }

    public void setExerciseAbstracts(List<ExerciseAbstract> exerciseAbstracts) {
        Collections.sort(exerciseAbstracts, ExerciseAbstract.MuscleGroupComparator);



        this.exerciseAbstracts = exerciseAbstracts;
        exerciseAbstractsFull = new ArrayList<>(exerciseAbstracts);
        notifyDataSetChanged(); //use itemchanged later
    }

/*    Collections.sort(list, new Comparator<String>() {
        @Override
        public int compare(String s1, String s2) {
            return s1.compareToIgnoreCase(s2);
        }
    });*/

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
                    if (item.generateExerciseAbstractName().toLowerCase().contains(filterPattern)) {
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
//        // old:
//        private TextView textViewExerciseAbstract;
//        private TextView textViewExerciseAbstractDescription;
//        private TextView textViewExerciseAbstractMuscleGroup;

        // new
        private final TextView text_view_exerciseabs_name;
        private final TextView text_view_exerciseabs_muscle_value;
        private final TextView text_view_exerciseabs_operation_value;
        private final TextView text_view_exerciseabs_nickname_value;
        private final TextView text_view_exerciseabs_load_type_value;
        private final TextView text_view_exerciseabs_separate_hands_value;
        private final TextView text_view_exerciseabs_position_value;
        private final TextView text_view_exerciseabs_angle_value;
        private final TextView text_view_exerciseabs_grip_width_value;
        private final TextView text_view_exerciseabs_thumbs_direction_value;

        public ExerciseAbstractHolder(View itemView) {
            super(itemView);
//           // old
//            textViewExerciseAbstract = itemView.findViewById(R.id.text_view_exerciseabs_text);
//            textViewExerciseAbstractDescription = itemView.findViewById(R.id.text_view_exerciseabs_description);
//            textViewExerciseAbstractMuscleGroup = itemView.findViewById(R.id.text_view_exerciseabs_musclegroup);

            // new
            text_view_exerciseabs_name = itemView.findViewById(R.id.text_view_exerciseabs_name);
            text_view_exerciseabs_muscle_value = itemView.findViewById(R.id.text_view_exerciseabs_musclegroup);
            text_view_exerciseabs_operation_value = itemView.findViewById(R.id.text_view_exerciseabs_operation_value);
            text_view_exerciseabs_nickname_value = itemView.findViewById(R.id.text_view_exerciseabs_nickname_value);
            text_view_exerciseabs_load_type_value = itemView.findViewById(R.id.text_view_exerciseabs_load_type_value);
            text_view_exerciseabs_separate_hands_value = itemView.findViewById(R.id.text_view_exerciseabs_separate_hands_value);
            text_view_exerciseabs_position_value = itemView.findViewById(R.id.text_view_exerciseabs_position_value);
            text_view_exerciseabs_angle_value = itemView.findViewById(R.id.text_view_exerciseabs_angle_value);
            text_view_exerciseabs_grip_width_value = itemView.findViewById(R.id.text_view_exerciseabs_grip_width_value);
            text_view_exerciseabs_thumbs_direction_value = itemView.findViewById(R.id.text_view_exerciseabs_thumbs_direction_value);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(exerciseAbstracts.get(position));
                    }
                }
            });

            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if(longListener != null && position != RecyclerView.NO_POSITION) {
                    longListener.onItemLongClick(exerciseAbstracts.get(position));
                }
                return true;
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
