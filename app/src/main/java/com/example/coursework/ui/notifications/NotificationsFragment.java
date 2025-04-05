package com.example.coursework.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.coursework.R;
import com.example.coursework.YogaClassData;
import com.example.coursework.ui.DataBaseHelper;
import com.example.coursework.YogaClassAdapter;
import java.util.ArrayList;
import java.util.List;

import com.example.coursework.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {
    private RecyclerView recyclerView;
    private YogaClassAdapter yogaClassAdapter;
    private DataBaseHelper dbHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);


        recyclerView = view.findViewById(R.id.recyclerViewYogaClasses);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        dbHelper = new DataBaseHelper(view.getContext());
        List<YogaClassData> classList = dbHelper.getAllYogaClasses();

        yogaClassAdapter = new YogaClassAdapter(view.getContext(), classList);
        recyclerView.setAdapter(yogaClassAdapter);

        return view;
    }
}