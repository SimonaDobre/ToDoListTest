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
import java.util.Collections;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements InterfaceEditTask, View.OnClickListener {

    EditText inputTaskED, inputPriorityED, deadLineED;
    Button addBtn, showEmergenciesBtn, orderByDeadLineBtn, showAllBtn;
    RecyclerView tasksListRV;
    ArrayList<Task> arrayTasksAll, arrayTaskSelected;
    MyAdapter myAdapter;
    String deadlineDate = "";

    public static final String TASK_FOR_EDIT = "tfe";
    public static final String DEADLINE_FOR_EDIT = "dlfe";
    public static final int PRIORITY_FOR_EDIT = 5;
    // public static final int RESULT_AFTER_EDIT = 30;
    int positionForEdit;
    boolean emergencyTasksListActiveShowing;

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
//        populareArrayPentruTest();
    }

    void populareArrayPentruTest() {
        arrayTasksAll.add(new Task("task 0", 0, "26-04-2022", false));
        arrayTasksAll.add(new Task("task 1", 1, "05-05-2022", false));
        arrayTasksAll.add(new Task("task 2", 2, "25-04-2022", false));
        arrayTasksAll.add(new Task("task 3", 0, "29-04-2022", false));
        arrayTasksAll.add(new Task("task 4", 0, "25-06-2022", false));
        arrayTasksAll.add(new Task("task 5", 2, "30-04-2022", false));
        arrayTasksAll.add(new Task("task 6", 0, "28-04-2022", false));
        arrayTasksAll.add(new Task("task 7", 1, "25-05-2022", false));
    }

    void resultReceivedFromEdit(ActivityResult resultReceived) {
        if (resultReceived.getResultCode() == RESULT_OK) {
            Intent receivedIntentAfterEdit = resultReceived.getData();
            if (receivedIntentAfterEdit != null) {
                String modifiedTask = receivedIntentAfterEdit.getStringExtra(EditActivity.NEW_TASK);
                String modifiedDeadline = receivedIntentAfterEdit.getStringExtra(EditActivity.NEW_DEADLINE);
                int modifiedPriority = receivedIntentAfterEdit.getIntExtra(String.valueOf(EditActivity.NEW_PRIORITY), 0);
                Task editedTask;
                if (emergencyTasksListActiveShowing) {
                    editedTask = arrayTaskSelected.get(positionForEdit);
                } else {
                    editedTask = arrayTasksAll.get(positionForEdit);
                }
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
        if (task.equals("") || task.equals(null)) {
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
        if (deadlineDate.equals("") || deadlineDate.equals(null)) {
            Calendar cal = Calendar.getInstance();
            Date today = cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH, 1);
            Date tommorow = cal.getTime();
            deadlineDate = tommorow.toString();
        }

        Task taskObject = new Task(task, priority, deadlineDate, false);
        arrayTasksAll.add(taskObject);
        myAdapter.notifyItemInserted(arrayTasksAll.size());
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
                if (emergencyTasksListActiveShowing) {
                    Task taskToBeDeleted = arrayTaskSelected.get(lineToBeDeleted);
                    arrayTaskSelected.remove(lineToBeDeleted);
                    arrayTasksAll.remove(taskToBeDeleted);
                } else {
                    arrayTasksAll.remove(lineToBeDeleted);
                }
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
        showEmergenciesBtn = findViewById(R.id.showEmergenciesButton);
        showEmergenciesBtn.setOnClickListener(this::onClick);
        orderByDeadLineBtn = findViewById(R.id.orderTasksButton);
        orderByDeadLineBtn.setOnClickListener(this::onClick);
        showAllBtn = findViewById(R.id.showAllButton);
        showAllBtn.setOnClickListener(this::onClick);

        arrayTaskSelected = new ArrayList<>();

        tasksListRV = findViewById(R.id.tasksListRecyclerView);
        arrayTasksAll = new ArrayList<>();
        myAdapter = new MyAdapter(this, arrayTasksAll, this);
        tasksListRV.setLayoutManager(new LinearLayoutManager(this));
        tasksListRV.setAdapter(myAdapter);

        tasksListRV.setHasFixedSize(true);
        tasksListRV.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public void editTask(int position) {
        Intent intentToEditActivity = new Intent(MainActivity.this, EditActivity.class);
        if (emergencyTasksListActiveShowing) {
            Task taskToBeEdited = arrayTaskSelected.get(position);
            positionForEdit = position;
            intentToEditActivity.putExtra(TASK_FOR_EDIT, taskToBeEdited.getTask());
            intentToEditActivity.putExtra(String.valueOf(PRIORITY_FOR_EDIT), taskToBeEdited.getPriority());
            intentToEditActivity.putExtra(DEADLINE_FOR_EDIT, taskToBeEdited.getDeadLine());
        } else {
            Task taskToBeEdited = arrayTasksAll.get(position);
            positionForEdit = position;
            intentToEditActivity.putExtra(TASK_FOR_EDIT, taskToBeEdited.getTask());
            intentToEditActivity.putExtra(String.valueOf(PRIORITY_FOR_EDIT), taskToBeEdited.getPriority());
            intentToEditActivity.putExtra(DEADLINE_FOR_EDIT, taskToBeEdited.getDeadLine());
        }
        launcherToEditare.launch(intentToEditActivity);
    }

    @Override
    public void solvedTask(int position) {
        Task accomplishedTask;
        if (emergencyTasksListActiveShowing) {
            accomplishedTask = arrayTaskSelected.get(position);
        } else {
            accomplishedTask = arrayTasksAll.get(position);
        }
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

    void showOnlyEmergencyTasks() {
        emergencyTasksListActiveShowing = true;
        arrayTaskSelected.clear();
        for (Task t : arrayTasksAll) {
            if (t.getPriority() == 0) {
                arrayTaskSelected.add(t);
            }
        }
        myAdapter = new MyAdapter(this, arrayTaskSelected, this);
        tasksListRV.setAdapter(myAdapter);
    }

    void showAllTasks() {
        emergencyTasksListActiveShowing = false;
        myAdapter = new MyAdapter(this, arrayTasksAll, this);
        tasksListRV.setAdapter(myAdapter);
    }

    void sortBy() {
        if (emergencyTasksListActiveShowing) {
            Collections.sort(arrayTaskSelected);
        } else {
            Collections.sort(arrayTasksAll);
        }
        myAdapter.notifyDataSetChanged();
        tasksListRV.setAdapter(myAdapter);
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
            case R.id.showEmergenciesButton:
                showOnlyEmergencyTasks();
                break;
            case R.id.orderTasksButton:
                sortBy();
                break;
            case R.id.showAllButton:
                showAllTasks();
                break;
            default:
                return;
        }

    }
}