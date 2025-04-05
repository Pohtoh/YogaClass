package com.example.coursework.ui.notifications;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.coursework.R;
import com.example.coursework.YogaClassData;
import com.example.coursework.ui.DataBaseHelper;
import com.example.coursework.YogaClassAdapter;
import java.util.ArrayList;


public class NotificationsFragment extends Fragment {
    private RecyclerView recyclerView;
    private YogaClassAdapter yogaClassAdapter;
    private DataBaseHelper dbHelper;
    private ArrayList<YogaClassData> yogaClassArrayList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView = view.findViewById(R.id.recyclerViewYogaClasses);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        dbHelper = new DataBaseHelper(view.getContext());

        yogaClassArrayList = dbHelper.getAllYogaClasses();
        yogaClassAdapter = new YogaClassAdapter(getContext(), yogaClassArrayList, NotificationsFragment.this);

        recyclerView.setAdapter(yogaClassAdapter);
        return view;
    }
        public void EditYogaClass(final boolean isUpdated, final YogaClassData yogaClass, final int position) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View view = layoutInflater.inflate(R.layout.item_update_yoga_class, null);

            AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(getContext());
            alerDialogBuilder.setView(view);

            final EditText editTextDay = view.findViewById(R.id.editTextDate);
            final EditText editTextDuration = view.findViewById(R.id.editTextTime);
            final EditText editTextNumberOfPeople = view.findViewById(R.id.editTextNumber);
            final EditText editTextPrice = view.findViewById(R.id.editTextNumber2);
            final EditText editTextClassType = view.findViewById(R.id.autoCompleteTextView);
            final EditText editTextDescription = view.findViewById(R.id.editTextTextMultiLine);


            if (isUpdated && yogaClass != null) {
                editTextDay.setText(String.valueOf(yogaClass.getDay()));
                editTextDuration.setText(String.valueOf(yogaClass.getDuration()));
                editTextNumberOfPeople.setText(String.valueOf(yogaClass.getNumberOfPeople()));
                editTextPrice.setText(String.valueOf(yogaClass.getPrice()));
                editTextClassType.setText(yogaClass.getClassType());
                editTextDescription.setText(yogaClass.getDescription());
            }
            alerDialogBuilder.setCancelable(false)
                    .setPositiveButton(isUpdated ? "Update" : "Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (TextUtils.isEmpty(editTextDay.getText().toString())) {
                                Toast.makeText(getContext(), "Day cannot be empty", Toast.LENGTH_SHORT).show();
                            }
                            if (TextUtils.isEmpty(editTextDuration.getText().toString())) {
                                Toast.makeText(getContext(), "Duration cannot be empty", Toast.LENGTH_SHORT).show();
                            }
                            if (TextUtils.isEmpty(editTextNumberOfPeople.getText().toString())) {
                                Toast.makeText(getContext(), "Number of People cannot be empty", Toast.LENGTH_SHORT).show();
                            }
                            if (TextUtils.isEmpty(editTextPrice.getText().toString())) {
                                Toast.makeText(getContext(), "Price cannot be empty", Toast.LENGTH_SHORT).show();
                            }
                            if (TextUtils.isEmpty(editTextClassType.getText().toString())) {
                                Toast.makeText(getContext(), "Class Type cannot be empty", Toast.LENGTH_SHORT).show();
                            }


                            if (isUpdated && yogaClass != null) {
                               // YogaClassData yogaClass = yogaClassArrayList.get(position);
                                yogaClass.setDay(Integer.parseInt(editTextDay.getText().toString()));
                                yogaClass.setDuration(Integer.parseInt(editTextDuration.getText().toString()));
                                yogaClass.setNumberOfPeople(Integer.parseInt(editTextNumberOfPeople.getText().toString()));
                                yogaClass.setPrice(Integer.parseInt(editTextPrice.getText().toString()));
                                yogaClass.setClassType(editTextClassType.getText().toString());
                                yogaClass.setDescription(editTextDescription.getText().toString());

                                dbHelper.updateYogaClass(yogaClass);
                                yogaClassArrayList.set(position, yogaClass);
                                yogaClassAdapter.notifyItemChanged(position);;
                            }

                        }

            })
                    .setNegativeButton(isUpdated ? "Delete" : "Cancel",
                            new DialogInterface.OnClickListener() {
                        @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if(isUpdated){
                                        dbHelper.deleteYogaClass(yogaClass);
                                        yogaClassArrayList.remove(position);
                                        yogaClassAdapter.notifyItemRemoved(position);

                                    }else{
                                        dialogInterface.cancel();
                                    }
                                }
                                }
                            );
            final AlertDialog alertDialog = alerDialogBuilder.create();
            alertDialog.show();
        }
}