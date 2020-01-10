package com.example.justdoit.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.justdoit.R;
import com.example.justdoit.model.TaskClass;
import com.example.justdoit.viewmodel.TaskClassViewModel;

public class NewClassFragment extends DialogFragment {

    private TaskClassViewModel viewModel;

    private EditText className;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.new_class_fragment, null);

        className = view.findViewById(R.id.edit_text_name);
        viewModel = ViewModelProviders.of(this).get(TaskClassViewModel.class);

        builder.setView(view)
                .setPositiveButton(R.string.new_class, (dialogInterface, i) -> {
                    String name = className.getText().toString();
                    if (name.trim().isEmpty()) {
                        className.setError("Inserisci un titolo!");
                    } else {
                        TaskClass newClass = new TaskClass(name);
                        viewModel.insertClass(newClass);
                        Toast.makeText(getContext(), "Class added", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.undo, (dialogInterface, i) -> NewClassFragment.this.getDialog().cancel());

        return builder.create();
    }
}
