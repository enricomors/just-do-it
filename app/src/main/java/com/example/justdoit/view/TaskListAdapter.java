package com.example.justdoit.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.justdoit.R;
import com.example.justdoit.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {

    private List<Task> tasksList = new ArrayList<>();

    private OnTaskClickListener onTaskClickListener;
    private OnTaskCompleteListener onTaskCompleteListener;

    public TaskListAdapter(OnTaskClickListener onTaskClickListener,
                           OnTaskCompleteListener onTaskCompleteListener) {
        this.onTaskClickListener = onTaskClickListener;
        this.onTaskCompleteListener = onTaskCompleteListener;
    }

    // non so se mi serve
    // public void updateTasksList(List<Task> newTasksList)

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view, onTaskClickListener, onTaskCompleteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {

        holder.title.setText(tasksList.get(position).getTitle());
        holder.date.setText(tasksList.get(position).getDate());
        holder.time.setText(tasksList.get(position).getTime());
        holder.priority.setText(String.valueOf(tasksList.get(position).getPriority()));
        holder.taskClass.setText(tasksList.get(position).getTaskClass());

        holder.completed.setChecked(false);
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }

    /**
     * Returns task at the specified position
     * @param position
     * @return
     */
    public Task getItem(int position) {
        return tasksList.get(position);
    }


    public void setTasksList(List<Task> tasksList) {
        this.tasksList = tasksList;
        notifyDataSetChanged();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            CompoundButton.OnCheckedChangeListener {

        public View itemView;

        private OnTaskClickListener onTaskClickListener;
        private OnTaskCompleteListener onTaskCompleteListener;

        private CheckBox completed;
        private TextView title;
        private TextView date;
        private TextView time;
        private TextView taskClass;
        private TextView priority;

        public TaskViewHolder(@NonNull View itemView,
                              OnTaskClickListener onTaskClickListener,
                              OnTaskCompleteListener onTaskCompleteListener) {
            super(itemView);
            this.onTaskClickListener = onTaskClickListener;
            this.onTaskCompleteListener = onTaskCompleteListener;
            this.itemView = itemView;

            completed = itemView.findViewById(R.id.check_complete);
            title = itemView.findViewById(R.id.text_view_title);
            date = itemView.findViewById(R.id.text_view_date);
            time = itemView.findViewById(R.id.text_view_time);
            taskClass = itemView.findViewById(R.id.label_class);
            priority = itemView.findViewById(R.id.text_view_priority);

            itemView.setOnClickListener(this);
            completed.setOnCheckedChangeListener(this);
        }

        @Override
        public void onClick(View view) {
            // get position of item clicked
            onTaskClickListener.onItemClick(getAdapterPosition());
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            onTaskCompleteListener.onTaskComplete(getAdapterPosition(), b);
        }
    }

    /**
     * Interface for listening to click on recycler view
     */
    public interface OnTaskClickListener {
        void onItemClick(int position);
    }

    public interface OnTaskCompleteListener {
        void onTaskComplete(int position, boolean complete);
    }
}
