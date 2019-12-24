package com.example.justdoit.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.justdoit.R;
import com.example.justdoit.model.ClassWithTask;
import com.example.justdoit.model.Task;
import com.example.justdoit.viewmodel.TaskViewModel;

import java.util.ArrayList;
import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {

    private List<Task> tasksList = new ArrayList<>();

    private OnTaskClickListener onTaskClickListener;

    public TaskListAdapter(OnTaskClickListener onTaskClickListener) {
        this.onTaskClickListener = onTaskClickListener;
    }

    // non so se mi serve
    // public void updateTasksList(List<Task> newTasksList)

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view, onTaskClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {

        holder.title.setText(tasksList.get(position).getTitle());
        holder.description.setText(tasksList.get(position).getDescription());
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

    class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public View itemView;

        private OnTaskClickListener onTaskClickListener;

        private CheckBox completed;
        private TextView title;
        private TextView description;
        private TextView date;
        private TextView time;
        private TextView taskClass;
        private TextView priority;

        public TaskViewHolder(@NonNull View itemView, OnTaskClickListener onTaskClickListener) {
            super(itemView);
            this.itemView = itemView;

            completed = itemView.findViewById(R.id.check_complete);
            title = itemView.findViewById(R.id.text_view_title);
            description = itemView.findViewById(R.id.text_view_descritption);
            date = itemView.findViewById(R.id.text_view_date);
            time = itemView.findViewById(R.id.text_view_time);
            taskClass = itemView.findViewById(R.id.text_view_class);
            priority = itemView.findViewById(R.id.text_view_priority);

            itemView.setOnClickListener(this);

            this.onTaskClickListener = onTaskClickListener;
        }

        @Override
        public void onClick(View view) {
            // get position of item clicked
            onTaskClickListener.onItemClick(getAdapterPosition());
        }
    }

    /**
     * Interface for listening to click on recycler view
     */
    public interface OnTaskClickListener {
        void onItemClick(int position);
    }
}
