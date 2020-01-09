package com.example.justdoit.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.justdoit.R;
import com.example.justdoit.model.Task;
import com.example.justdoit.viewmodel.TaskViewModel;

public class DeleteTaskFragment extends DialogFragment {

    private Task task;

    private TaskViewModel taskViewModel;

    public DeleteTaskFragment(Task taskToDelete) {
        task = taskToDelete;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        builder.setMessage(R.string.delete_task_req)
                .setPositiveButton(R.string.delete_task, (dialogInterface, i) -> {
                    // delete task
                    taskViewModel.deleteTask(task);
                    Toast.makeText(getContext(), R.string.delete_task_conf, Toast.LENGTH_SHORT).show();
                    dismiss();
                })
                .setNegativeButton(R.string.undo, (dialogInterface, i) -> {
                    // user cancels dialog
                    dismiss();
                });
        return builder.create();
    }


}
