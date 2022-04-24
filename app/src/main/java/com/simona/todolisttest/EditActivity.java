package com.simona.todolisttest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTaskED, editPriorityED, editDeadlineED;
    Button saveModificationsBtn;
    String newDeadline;

    public static final String NEW_TASK = "nt";
    public static final String NEW_DEADLINE = "nd";
    public static final int NEW_PRIORITY = 18;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        initViews();
        receivedTaskForEdit();
    }

    void receivedTaskForEdit() {
        Intent datasToBeEdited = getIntent();
        String taskForEdit = datasToBeEdited.getStringExtra(MainActivity.TASK_FOR_EDIT);
        String deadlineForEdit = datasToBeEdited.getStringExtra(MainActivity.DEADLINE_FOR_EDIT);
        int priorityForEdit = datasToBeEdited.getIntExtra(String.valueOf(MainActivity.PRIORITY_FOR_EDIT), -1);
        editTaskED.setText(taskForEdit);
        editDeadlineED.setText(deadlineForEdit);
        editPriorityED.setText(String.valueOf(priorityForEdit));
        editTaskED.requestFocus();
    }

    void sendBackModifiedTask() {
        String newTask = editTaskED.getText().toString().trim();
        newDeadline = editDeadlineED.getText().toString().trim();
        int newPriority = Integer.parseInt(editPriorityED.getText().toString().trim());
        if (newPriority < 0 || newPriority > 2) {
            Toast.makeText(EditActivity.this, "valoarea trebuie sa fie 0 sau 1 sau 2", Toast.LENGTH_SHORT).show();
            editPriorityED.setText(null);
            editPriorityED.requestFocus();
            return;
        }
        Intent j = new Intent(EditActivity.this, MainActivity.class);
        j.putExtra(NEW_TASK, newTask);
        j.putExtra(NEW_DEADLINE, newDeadline);
        j.putExtra(String.valueOf(NEW_PRIORITY), newPriority);
        setResult(RESULT_OK, j);
//        finish();
        super.onBackPressed();
    }

    void initViews() {
        editTaskED = findViewById(R.id.editTaskEditText);
        editPriorityED = findViewById(R.id.editPriorityEditText);
        editDeadlineED = findViewById(R.id.editDeadLineEditText);
        editDeadlineED.setOnClickListener(this::onClick);
        saveModificationsBtn = findViewById(R.id.saveModificationsButton);
        saveModificationsBtn.setOnClickListener(this);
    }

    void pickDeadlineDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(EditActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        i1++;
                        newDeadline = i2 + "-" + i1 + "-" + i;
                        editDeadlineED.setText(newDeadline);
                    }
                }, year, month, day);
        dpd.getDatePicker().setMinDate(cal.getTimeInMillis());
        dpd.show();
    }

    @Override
    public void onClick(View view) {
        int clickedID = view.getId();
        switch (clickedID) {
            case R.id.saveModificationsButton:
                sendBackModifiedTask();
                break;
            case R.id.editDeadLineEditText:
                pickDeadlineDate();
                break;
            default:
                return;
        }

    }
}