package com.example.coursework;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class YogaClassAdapter extends RecyclerView.Adapter<YogaClassAdapter.YogaClassViewHolder> {

    private List<YogaClassData> yogaClassList;
    private Context context;

    public class MyviewHolder extends RecyclerView.ViewHolder {
        public TextView classType;
        public TextView details;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            classType = itemView.findViewById(R.id.textViewClassType);
            details = itemView.findViewById(R.id.textViewDetails);
        }
    }

    public YogaClassAdapter(Context context, List<YogaClassData> classList) {
        this.context = context;
        this.yogaClassList = classList;

    }

    @NonNull
    @Override
    public YogaClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_yoga_class, parent, false);
        return new YogaClassViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull YogaClassViewHolder holder, int position) {
        final YogaClassData yogaClassData = yogaClassList.get(position);
        holder.classType.setText(yogaClassData.getClassType());
        holder.details.setText(yogaClassData.getDescription());
        YogaClassData yogaClass = yogaClassList.get(position);

        holder.classType.setText(yogaClass.getClassType());

        String details = "Day: " + yogaClass.getDay()
                + " | Duration: " + yogaClass.getDuration()
                + " | People: " + yogaClass.getNumberOfPeople()
                + " | Price: " + yogaClass.getPrice() + " Pound\n"
                + "Note: " + yogaClass.getDescription();
        holder.details.setText(details);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.editOrDeleteYogaClass(true, yogaClass, position);
            }
    });
        }

    @Override
    public int getItemCount() {
        return yogaClassList.size();
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