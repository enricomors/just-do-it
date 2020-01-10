package com.example.justdoit.view;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.justdoit.R;
import com.example.justdoit.model.TaskClass;
import com.example.justdoit.viewmodel.TaskClassViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddClassFragment extends Fragment
        implements TaskClassListAdapter.OnClassLongClickListener {

    @BindView(R.id.fab_add_class)
    FloatingActionButton fabbAdd;

    @BindView(R.id.classes_list)
    RecyclerView classesList;

    private TaskClassViewModel viewModel;

    private TaskClassListAdapter adapter = new TaskClassListAdapter(this);

    public AddClassFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_class, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(TaskClassViewModel.class);
        viewModel.getAllClasses().observe(this, taskClasses -> adapter.setClassesList(taskClasses));

        fabbAdd.setOnClickListener(v -> showDialog());

        classesList.setLayoutManager(new LinearLayoutManager(getContext()));
        classesList.setAdapter(adapter);

    }

    private void showDialog() {
        NewClassFragment fragment = new NewClassFragment();
        fragment.show(getFragmentManager(), "Add Class");
    }

    private void navigateBack() {
        AddClassFragmentDirections.ActionClassAdded action =
                AddClassFragmentDirections.actionClassAdded();
        action.setFilerType(0);
        Navigation.findNavController(fabbAdd).navigate(action);
    }

    @Override
    public void onItemLongClick(int position) {
        TaskClass taskClass = adapter.getItem(position);
        if (taskClass.getName().equals("No class")) {
            // non puoi eliminarla
        } else {
            DeleteClassFragment fragment = new DeleteClassFragment(taskClass);
            fragment.show(getFragmentManager(), "Delete Class");
        }
    }
}
