package com.simona.todolisttest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText inputTaskED, inputPriorityED;
    Button addBtn;
    public ArrayList<Task> taskList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        addTaskToList();
    }


    void addTaskToList(){
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String task = inputTaskED.getText().toString().trim();
                int priority = Integer.parseInt(inputPriorityED.getText().toString().trim());
                Task taskObject = new Task(task, priority);
                taskList.add(taskObject);
            }
        });
    }

    void initViews(){
        inputTaskED = findViewById(R.id.inputTaskEditText);
        inputPriorityED = findViewById(R.id.inputPriorityEditText);
        addBtn = findViewById(R.id.button);
    }

}