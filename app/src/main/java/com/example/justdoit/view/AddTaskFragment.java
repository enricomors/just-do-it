package com.example.justdoit.view;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.justdoit.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTaskFragment extends Fragment {

    @BindView(R.id.fabDone)
    FloatingActionButton fabDone;

    @BindView(R.id.button_class)
    Button buttonClass;

    @BindView(R.id.button_date)
    Button buttonDate;

    @BindView(R.id.button_priority)
    Button buttonPriority;

    @BindView(R.id.button_time)
    Button buttonTime;

    @BindView(R.id.edit_text_description)
    EditText textDescription;

    @BindView(R.id.edit_text_title)
    EditText textTitle;

    public AddTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_task, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fabDone.setOnClickListener(v -> onTaskSave());
        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open date picker
            }
        });
        buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open time picker
            }
        });
    }

    private void onTaskSave() {
        NavDirections action = AddTaskFragmentDirections.actionSaveTask();
        Navigation.findNavController(fabDone).navigate(action);

        // Save newly created task to database
    }
}
