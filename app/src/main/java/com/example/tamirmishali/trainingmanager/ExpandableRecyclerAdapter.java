package com.example.tamirmishali.trainingmanager;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tamirmishali.trainingmanager.Exercise.Exercise;

import java.util.ArrayList;
import java.util.List;

import com.squareup.picasso.Picasso;

//https://stackoverflow.com/questions/38236948/how-to-make-an-expandable-list-of-cardviews
public class ExpandableRecyclerAdapter extends RecyclerView.Adapter<ExpandableRecyclerAdapter.ViewHolder> {

    private List<Exercise> exercises = new ArrayList<>();
    private SparseBooleanArray expandState = new SparseBooleanArray();
    private Context context;

    public ExpandableRecyclerAdapter(List<Exercise> exercises) {
        this.exercises = exercises;
        //set initial expanded state to false
        for (int i = 0; i < exercises.size(); i++) {
            expandState.append(i, false);
        }
    }

    public ExpandableRecyclerAdapter() {
    }


    @Override
    public ExpandableRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        this.context = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_workoutnow, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ExpandableRecyclerAdapter.ViewHolder viewHolder, final  int i) {

        viewHolder.setIsRecyclable(false);

        //viewHolder.tvName.setText(exercises.get(i).getName());
        viewHolder.tvName.setText(Integer.toString(exercises.get(i).getId()));

        viewHolder.tvOwnerLogin.setText("Owner: " /*+exercises.get(i).getOwner().getLogin()*/);
        //viewHolder.tvOwnerUrl.setText(exercises.get(i).getOwner().getUrl());

/*        Picasso.with(context)
                .load(exercises.get(i).getOwner().getImageUrl())
                .resize(500, 500)
                .centerCrop()
                .into(viewHolder.ivOwner);*/

        //check if view is expanded
        final boolean isExpanded = expandState.get(i);
        viewHolder.expandableLayout.setVisibility(isExpanded?View.VISIBLE:View.GONE);

        viewHolder.buttonLayout.setRotation(expandState.get(i) ? 180f : 0f);
        viewHolder.buttonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onClickButton(viewHolder.expandableLayout, viewHolder.buttonLayout,  i);
            }
        });
    }


    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
        for (int i = 0; i < exercises.size(); i++) {
            expandState.append(i, false);
        }
        notifyDataSetChanged(); //use itemchanged later
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvName,tvOwnerLogin, tvOwnerUrl;
        private ImageView ivOwner;
        public RelativeLayout buttonLayout;
        public LinearLayout expandableLayout;

        public ViewHolder(View view) {
            super(view);

            tvName = (TextView)view.findViewById(R.id.textView_name);
            //tvId = (TextView)view.findViewById(R.id.textView_id);
            //tvUrl = (TextView)view.findViewById(R.id.textView_url);
            tvOwnerLogin = (TextView)view.findViewById(R.id.textView_Owner);
            tvOwnerUrl = (TextView)view.findViewById(R.id.textView_OwnerUrl);
            ivOwner = (ImageView) view.findViewById(R.id.imageView_Owner);

            buttonLayout = (RelativeLayout) view.findViewById(R.id.button);
            expandableLayout = (LinearLayout) view.findViewById(R.id.expandableLayout);
        }
    }

    private void onClickButton(final LinearLayout expandableLayout, final RelativeLayout buttonLayout, final  int i) {

        //Simply set View to Gone if not expanded
        //Not necessary but I put simple rotation on button layout
        if (expandableLayout.getVisibility() == View.VISIBLE){
            createRotateAnimator(buttonLayout, 180f, 0f).start();
            expandableLayout.setVisibility(View.GONE);
            expandState.put(i, false);
        }else{
            createRotateAnimator(buttonLayout, 0f, 180f).start();
            expandableLayout.setVisibility(View.VISIBLE);
            expandState.put(i, true);
        }
    }

    //Code to rotate button
    private ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }
}