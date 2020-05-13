package com.example.spring2020_final;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> implements iOnItemListener {

    ArrayList<Schedule> mData;
    iOnItemListener iOnItemListener;

    public ScheduleAdapter(ArrayList<Schedule> mData, iOnItemListener iOnItemListener) {
        this.mData = mData;
        this.iOnItemListener = iOnItemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item,parent, false);

        ViewHolder viewHolder = new ViewHolder(view, iOnItemListener);
        return viewHolder;    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Schedule schedule = mData.get(position);
        holder.textViewName.setText(schedule.name);
        holder.textViewPlace.setText(schedule.place);
        holder.textViewDateTIme.setText(schedule.date + " " + schedule.time);
        holder.schedule = schedule;

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onItemLongPress(int pos) {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textViewName, textViewPlace, textViewDateTIme;
        Schedule schedule;
        iOnItemListener iOnItemListener;

        public ViewHolder(@NonNull View itemView, iOnItemListener iOnItemListener) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.tv_name);
            textViewPlace = itemView.findViewById(R.id.tv_place);
            textViewDateTIme = itemView.findViewById(R.id.tv_date_time);
            this.iOnItemListener = iOnItemListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iOnItemListener.onItemLongPress(getAdapterPosition());
        }
    }

}
