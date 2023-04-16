package com.example.lostie.activity;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lostie.database.Object;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lostie.R;
import com.example.lostie.database.DatabaseHelper;
import com.example.lostie.database.MyListAdapter;
import com.example.lostie.database.ViewModel;
import com.example.lostie.map.MapActivity;

public class DatabaseCheck extends AppCompatActivity {

    private ViewModel mViewModel;

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        final MyListAdapter adapter = new MyListAdapter(new MyListAdapter.MyDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mViewModel = new ViewModelProvider(this).get(ViewModel.class);

        // Update the cached copy of the words in the adapter.
        mViewModel.getAll().observe(this, adapter::submitList);

        Button button = (Button) findViewById(R.id.addButton);
        button.setOnClickListener( view -> {
            Intent intent = new Intent(DatabaseCheck.this, AddNewObjectActivity.class);
            startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Object object = new Object();
            object.name = data.getStringExtra(AddNewObjectActivity.EXTRA_REPLY);
            object.id = object.name.length();
            mViewModel.insert(object);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "empty_not_saved",
                    Toast.LENGTH_LONG).show();
        }
    }
}
