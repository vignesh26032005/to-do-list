package com.newapp.todolistapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private EditText taskInput;
    private LinearLayout taskListContainer;
    private ArrayList<String> taskList;
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREFS_KEY = "todo_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskInput = findViewById(R.id.task_input);
        Button addTaskBtn = findViewById(R.id.add_task_btn);
        taskListContainer = findViewById(R.id.task_list_container);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE);

        // Load tasks from SharedPreferences
        loadTasks();

        // Set up the Add Task button
        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = taskInput.getText().toString().trim();
                if (!task.isEmpty()) {
                    addTask(task);
                    saveTasks(); // Save tasks to SharedPreferences
                    taskInput.setText(""); // Clear input field after adding task
                }
            }
        });
    }

    private void addTask(final String task) {
        final TextView textView = new TextView(this);
        textView.setText(task);
        textView.setTextSize(20);
        textView.setPadding(0, 10, 0, 10);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markTaskCompleted(textView);
            }
        });
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deleteTask(textView);
                return true;
            }
        });
        taskListContainer.addView(textView);
        taskList.add(task);
    }

    private void markTaskCompleted(TextView textView) {
        textView.setPaintFlags(textView.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
    }

    private void deleteTask(TextView textView) {
        taskListContainer.removeView(textView);
        taskList.remove(textView.getText().toString());
        saveTasks(); // Save tasks to SharedPreferences after deletion
    }

    private void loadTasks() {
        Set<String> defaultSet = new HashSet<>();
        taskList = new ArrayList<>(sharedPreferences.getStringSet(SHARED_PREFS_KEY, defaultSet));
        for (String task : taskList) {
            addTask(task);
        }
    }

    private void saveTasks() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> taskSet = new HashSet<>(taskList);
        editor.putStringSet(SHARED_PREFS_KEY, taskSet);
        editor.apply();
    }
}
