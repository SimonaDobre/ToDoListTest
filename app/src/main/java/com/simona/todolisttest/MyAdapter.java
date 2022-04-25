package com.simona.todolisttest;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Task> arrayTasks;
    InterfaceEditTask interfaceEditTask;

    public MyAdapter(Context context, ArrayList<Task> arrayTasks, InterfaceEditTask interfaceEditTask) {
        this.context = context;
        this.arrayTasks = arrayTasks;
        this.interfaceEditTask = interfaceEditTask;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView taskTV, priorityTV, deadLineTV;
        ImageView editImageView;
        InterfaceEditTask iet;

        public MyViewHolder(@NonNull View itemView, InterfaceEditTask iet) {
            super(itemView);
            taskTV = itemView.findViewById(R.id.taskTextView);
            priorityTV = itemView.findViewById(R.id.priorityTextView);
            deadLineTV = itemView.findViewById(R.id.deadLineTextView);
            editImageView = itemView.findViewById(R.id.editImageView);
            this.iet = iet;

            itemView.setOnClickListener(this);
            editImageView.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View view) {
            if (iet != null) {
                int clickedPosition = getAdapterPosition();
                int clickedID = view.getId();
                switch (clickedID) {
                    case R.id.editImageView:
                        iet.editTask(clickedPosition);
                        break;
                    case R.id.wholeLineRL:
                        iet.solvedTask(clickedPosition);
                        break;
                    default:
                        return;
                }
            }
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_line, parent, false);
        MyViewHolder mvh = new MyViewHolder(v, interfaceEditTask);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Task currentTask = arrayTasks.get(position);
        holder.taskTV.setText(currentTask.getTask());
        holder.priorityTV.setText(String.valueOf(currentTask.getPriority()));
        if (currentTask.getPriority() == 0) {
            holder.priorityTV.setTextColor(Color.RED);
        }
        holder.deadLineTV.setText(currentTask.getDeadLine());

        if (currentTask.isAccomplished()){
            holder.taskTV.setPaintFlags(holder.taskTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.deadLineTV.setPaintFlags(holder.deadLineTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.priorityTV.setPaintFlags(holder.priorityTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.taskTV.setPaintFlags(0);
            holder.deadLineTV.setPaintFlags(0);
            holder.priorityTV.setPaintFlags(0);
        }

    }

    @Override
    public int getItemCount() {
        return arrayTasks.size();
    }


}
