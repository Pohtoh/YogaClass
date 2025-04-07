package com.example.coursework;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursework.ui.DataBaseHelper;
import com.example.coursework.ui.home.HomeFragment;
import com.example.coursework.ui.notifications.NotificationsFragment;

import java.util.ArrayList;
import java.util.List;

public class YogaClassAdapter2 extends RecyclerView.Adapter<YogaClassAdapter2.YogaClassViewHolder> {

    private List<YogaClassScheduleData> yogaClassScheduleList;
    private Context context;
    private HomeFragment homeFragment;
    private DataBaseHelper dbHelper;

    public YogaClassAdapter2(Context context, ArrayList<YogaClassScheduleData> classList, HomeFragment homeFragment, DataBaseHelper dbHelper) {
        this.context = context;
        this.yogaClassScheduleList = classList;
        this.homeFragment = homeFragment;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public YogaClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_yoga_class, parent, false);
        return new YogaClassViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull YogaClassViewHolder holder, int position) {
        final YogaClassScheduleData yogaClassScheduleData = yogaClassScheduleList.get(position);
        YogaClassScheduleData scheduleData = yogaClassScheduleList.get(position);

        YogaClassData yogaClass = dbHelper.getYogaClassById(scheduleData.getYogaClassID());
        String classType = (yogaClass != null) ? yogaClass.getClassType() : "Unknown Class";

        holder.classType.setText(classType);
        String details = "Teacher: " + scheduleData.getTeacher()
                + "\nDate: " + scheduleData.getDate()
                + "\nNote: " + scheduleData.getDescription();
        holder.details.setText(details);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(homeFragment != null){
                    homeFragment.EditYogaClass(true, scheduleData);
                }
            }
    });
        }

    @Override
    public int getItemCount() {
        return yogaClassScheduleList.size();
    }

    public static class YogaClassViewHolder extends RecyclerView.ViewHolder {
        TextView classType;
        TextView details;

        public YogaClassViewHolder(@NonNull View itemView) {
            super(itemView);
            classType = itemView.findViewById(R.id.textViewClassType);
            details = itemView.findViewById(R.id.textViewDetails);
        }
    }
}