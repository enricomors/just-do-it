package com.example.justdoit.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.justdoit.R;
import com.example.justdoit.model.TaskClass;
import com.example.justdoit.viewmodel.TaskClassViewModel;
import com.example.justdoit.viewmodel.TaskViewModel;

public class DeleteClassFragment extends DialogFragment {

    private TaskClass taskClass;
    private TaskClassViewModel viewModel;

    public DeleteClassFragment(TaskClass taskClass) {
        this.taskClass = taskClass;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        viewModel = ViewModelProviders.of(this).get(TaskClassViewModel.class);
        builder.setMessage(R.string.delete_class_req)
                .setPositiveButton(R.string.delete_task, (dialogInterface, i) -> {
                    // delete task
                    viewModel.deleteClass(taskClass);
                    Toast.makeText(getContext(), R.string.delete_class_conf, Toast.LENGTH_SHORT).show();
                    dismiss();
                })
                .setNegativeButton(R.string.undo, (dialogInterface, i) -> DeleteClassFragment.this.getDialog().cancel());
        return builder.create();
    }
}
