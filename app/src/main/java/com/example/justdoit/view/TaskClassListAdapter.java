package com.example.justdoit.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.justdoit.R;
import com.example.justdoit.model.TaskClass;

import java.util.ArrayList;
import java.util.List;

public class TaskClassListAdapter extends RecyclerView.Adapter<TaskClassListAdapter.TaskClassViewHolder> {

    private List<TaskClass> classes = new ArrayList<>();

    private OnClassLongClickListener onClassLongClickListener;

    public TaskClassListAdapter(OnClassLongClickListener onClassLongClickListener) {
        this.onClassLongClickListener = onClassLongClickListener;
    }

    @NonNull
    @Override
    public TaskClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_class, parent, false);
        return new TaskClassViewHolder(view, onClassLongClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskClassViewHolder holder, int position) {
        holder.textViewName.setText(classes.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    public TaskClass getItem(int position) {
        return classes.get(position);
    }

    public void setClassesList(List<TaskClass> classes) {
        this.classes = classes;
        notifyDataSetChanged();
    }

    class TaskClassViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        private View itemView;

        private TextView textViewName;

        private OnClassLongClickListener onClassLongClickListener;

        public TaskClassViewHolder(@NonNull View itemView,
                                   OnClassLongClickListener onClassLongClickListener) {
            super(itemView);
            this.itemView = itemView;
            this.onClassLongClickListener = onClassLongClickListener;

            textViewName = itemView.findViewById(R.id.text_view_name);

            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            onClassLongClickListener.onItemLongClick(getAdapterPosition());
            return true;
        }
    }

    public interface OnClassLongClickListener {
        void onItemLongClick(int position);
    }

}
