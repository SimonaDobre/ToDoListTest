package com.simona.todolisttest;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements InterfaceEditTask, View.OnClickListener {

    EditText inputTaskED, inputPriorityED, deadLineED;
    Button addBtn;
    RecyclerView tasksListRV;
    ArrayList<Task> arrayTasks;
    MyAdapter myAdapter;
    String deadlineDate = "";

    public static final String TASK_FOR_EDIT = "tfe";
    public static final String DEADLINE_FOR_EDIT = "dlfe";
    public static final int PRIORITY_FOR_EDIT = 5;
    public static final int RESULT_AFTER_EDIT = 30;
    int positionForEdit;

    ActivityResultLauncher<Intent> launcherToEditare = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    resultReceivedFromEdit(result);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        swipeToDelete();

    }

    void resultReceivedFromEdit(ActivityResult resultReceived) {
        if (resultReceived.getResultCode() == RESULT_OK) {
            Intent receivedIntentAfterEdit = resultReceived.getData();
            if (receivedIntentAfterEdit != null) {
                String modifiedTask = receivedIntentAfterEdit.getStringExtra(EditActivity.NEW_TASK);
                String modifiedDeadline = receivedIntentAfterEdit.getStringExtra(EditActivity.NEW_DEADLINE);
                int modifiedPriority = receivedIntentAfterEdit.getIntExtra(String.valueOf(EditActivity.NEW_PRIORITY), 0);
                Task editedTask = arrayTasks.get(positionForEdit);
                editedTask.setTask(modifiedTask);
                editedTask.setDeadLine(modifiedDeadline);
                editedTask.setPriority(modifiedPriority);
                myAdapter.notifyDataSetChanged();
                tasksListRV.setAdapter(myAdapter);
            }
        }
    }

    void addTaskToList() {
        String task = inputTaskED.getText().toString().trim();
        if (task.equals("") || task.equals(null)){
            Toast.makeText(MainActivity.this, "campul task nu poate fi null", Toast.LENGTH_SHORT).show();
            return;
        }
        int priority = 0;
        try {
            priority = Integer.parseInt(inputPriorityED.getText().toString().trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "campul priority nu poate fi null", Toast.LENGTH_SHORT).show();
            return;
        }
        if (priority < 0 || priority > 2) {
            inputPriorityED.setText(null);
            inputPriorityED.requestFocus();
            Toast.makeText(MainActivity.this, "se accepta doar 0 sau 1 sau 2", Toast.LENGTH_SHORT).show();
            return;
        }
        if (deadlineDate.equals("") || deadlineDate.equals(null)){
            Calendar cal = Calendar.getInstance();
            Date today = cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH, 1);
            Date tommorow = cal.getTime();
            deadlineDate = tommorow.toString();
         }

        Task taskObject = new Task(task, priority, deadlineDate, false);
        arrayTasks.add(taskObject);
        myAdapter.notifyItemInserted(arrayTasks.size());
        inputTaskED.setText(null);
        inputTaskED.requestFocus();
        inputPriorityED.setText(null);
        deadLineED.setText(null);
    }

    void swipeToDelete() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int lineToBeDeleted = viewHolder.getAdapterPosition();
                // int pozitia = arrayTasks.indexOf(lineToBeDeleted);
                arrayTasks.remove(lineToBeDeleted);
                // myAdapter.notifyItemRemoved(arrayTasks.indexOf(pozitia));
                myAdapter.notifyDataSetChanged();
                tasksListRV.setAdapter(myAdapter);
            }
        }).attachToRecyclerView(tasksListRV);

    }

    void initViews() {
        inputTaskED = findViewById(R.id.inputTaskEditText);
        inputPriorityED = findViewById(R.id.inputPriorityEditText);
        addBtn = findViewById(R.id.addButton);
        addBtn.setOnClickListener(this);
        deadLineED = findViewById(R.id.deadLineEditText);
        deadLineED.setOnClickListener(this);

        tasksListRV = findViewById(R.id.tasksListRecyclerView);
        arrayTasks = new ArrayList<>();
        myAdapter = new MyAdapter(this, arrayTasks, this);
        tasksListRV.setLayoutManager(new LinearLayoutManager(this));
        tasksListRV.setAdapter(myAdapter);

        tasksListRV.setHasFixedSize(true);
        tasksListRV.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public void editTask(int position) {
        Task taskToBeEdited = arrayTasks.get(position);
        positionForEdit = position;
        Intent intentToEditActivity = new Intent(MainActivity.this, EditActivity.class);
        intentToEditActivity.putExtra(TASK_FOR_EDIT, taskToBeEdited.getTask());
        intentToEditActivity.putExtra(String.valueOf(PRIORITY_FOR_EDIT), taskToBeEdited.getPriority());
        intentToEditActivity.putExtra(DEADLINE_FOR_EDIT, taskToBeEdited.getDeadLine());
        launcherToEditare.launch(intentToEditActivity);
    }

    @Override
    public void solvedTask(int position) {
        Task accomplishedTask = arrayTasks.get(position);
        accomplishedTask.changeAccomplishment();
        myAdapter.notifyDataSetChanged();
        tasksListRV.setAdapter(myAdapter);
    }

    void pickDeadlineDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(MainActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        i1++;
                        deadlineDate = i2 + "-" + i1 + "-" + i;
                        deadLineED.setText(deadlineDate);
                    }
                }, year, month, day);
        dpd.getDatePicker().setMinDate(cal.getTimeInMillis());
        dpd.show();
    }

    @Override
    public void onClick(View view) {
        int clickedID = view.getId();
        switch (clickedID) {
            case R.id.addButton:
                addTaskToList();
                break;
            case R.id.deadLineEditText:
                pickDeadlineDate();
                break;
            default:
                return;
        }

    }
}