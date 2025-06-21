package com.example.tamirmishali.trainingmanager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


@SuppressWarnings("serial")
public class WorkoutItemTouchCallback extends ItemTouchHelper.Callback {

    private final WorkoutRecyclerAdapter adapter;

    public WorkoutItemTouchCallback(WorkoutRecyclerAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        // Only allow dragging for set rows, not exercise headers
        if (viewHolder instanceof WorkoutRecyclerAdapter.SetRowViewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            return makeMovementFlags(dragFlags, 0);
        }
        return 0;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {

        // Don't allow moving between different view types
        if (viewHolder.getClass() != target.getClass()) {
            return false;
        }

        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();

        adapter.moveItem(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        // Not used since swipe is disabled
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false; // We'll use the drag handle instead
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false; // Disable swipe to avoid accidental deletions
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof WorkoutRecyclerAdapter.SetRowViewHolder) {
                viewHolder.itemView.setAlpha(0.7f); // Highlight dragged item
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setAlpha(1.0f); // Reset appearance
    }
}