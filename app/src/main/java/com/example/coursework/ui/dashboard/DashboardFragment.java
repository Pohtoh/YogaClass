package com.example.coursework.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.coursework.R;
import com.example.coursework.YogaClassData;
import com.example.coursework.ui.DataBaseHelper;

import com.example.coursework.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {
    private DataBaseHelper dbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DataBaseHelper(getContext()); // Initialize database helper

        // Get references to UI elements using `view.findViewById()`
        EditText editTextDate = view.findViewById(R.id.editTextDate);
        EditText editTextTime = view.findViewById(R.id.editTextTime);
        EditText editTextNumber = view.findViewById(R.id.editTextNumber);
        EditText editTextNumber2 = view.findViewById(R.id.editTextNumber2);
        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);
        EditText editTextTextMultiLine = view.findViewById(R.id.editTextTextMultiLine);
        Button buttonAdd = view.findViewById(R.id.buttonAdd); // Use `view.findViewById()`

        String[] options = {"Option 1", "Option 2", "Option 3"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, options);
        autoCompleteTextView.setAdapter(adapter);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int day = Integer.parseInt(editTextDate.getText().toString());
                    int duration = Integer.parseInt(editTextTime.getText().toString());
                    int numberOfPeople = Integer.parseInt(editTextNumber.getText().toString());
                    int price = Integer.parseInt(editTextNumber2.getText().toString());
                    String classType = autoCompleteTextView.getText().toString();
                    String description = editTextTextMultiLine.getText().toString();

                    long newRowId = dbHelper.insertYogaClass(day, duration, numberOfPeople, price, classType, description);
                    if (newRowId != -1) {
                        Toast.makeText(getContext(), "Class Inserted! ID: " + newRowId, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Insertion Failed!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    //Toast.makeText(getContext(), "Invalid input! Please enter valid data.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(),e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}