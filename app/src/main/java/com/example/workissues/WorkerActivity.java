package com.example.workissues;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.workissues.adapters.IssuesAdapters;
import com.example.workissues.utilites.Comment;
import com.example.workissues.utilites.Issue;
import com.example.workissues.utilites.Role;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.DateTime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WorkerActivity extends AppCompatActivity {

    private String currentUserID = "111";
    private int mode = 0;
    private String field = "to";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference tasksCollection = db.collection("tasks");
    private ArrayList<Issue> allIssues = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker);
        Intent intent = getIntent();
        currentUserID = intent.getStringExtra("id");
        String userRole = "";
        userRole = intent.getStringExtra("role");
        if(userRole.contains(Role.Worker.toString())){
            field = "to";
            Button b = findViewById(R.id.button), b1 = findViewById(R.id.button4);
            b.setVisibility(View.GONE);
            b1.setVisibility(View.GONE);
        }
        else field = "from";
        GetListOfIssues();
        Toast.makeText(this, "Ваш айди - " + currentUserID, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void ReloadRecycle(){

        RecyclerView rec = findViewById(R.id.works_recycler);
        rec.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //rec.setHasFixedSize(true);
        IssuesAdapters adapter = new IssuesAdapters(allIssues, field, currentUserID);
        rec.setAdapter(adapter);
    }
    public void GetListOfIssues(){
        allIssues.clear();
        if(mode == 0){
            tasksCollection
                    .whereEqualTo(field, currentUserID)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for (DocumentSnapshot document:
                                        task.getResult()) {
                                    Issue newIssue = document.toObject(Issue.class);
                                    newIssue.setId(document.getId());
                                    allIssues.add(newIssue);
                                }
                                ReloadRecycle();
                            }
                        }
                    });
        }
        else if(mode == 1){
            tasksCollection
                    .whereEqualTo(field, currentUserID)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for (DocumentSnapshot document:
                                        task.getResult()) {
                                    Issue newIssue = document.toObject(Issue.class);
                                    newIssue.setId(document.getId());
                                    if(newIssue.getProgress() <= 30)
                                        allIssues.add(newIssue);
                                }
                                ReloadRecycle();
                            }
                        }
                    });
        }
        else {
            tasksCollection
                    .whereEqualTo(field, currentUserID)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for (DocumentSnapshot document:
                                        task.getResult()) {
                                    Issue newIssue = document.toObject(Issue.class);
                                    newIssue.setId(document.getId());
                                    if(newIssue.getProgress() > 30)
                                        allIssues.add(newIssue);
                                }
                                ReloadRecycle();
                            }
                        }
                    });
        }
    }

    public void ChangeMode(View view) {
        if(mode < 2) mode++;
        else mode = 0;
        GetListOfIssues();
    }

    public void AddWorker(View view) {
        Intent intent = new Intent(WorkerActivity.this, UserAddMenu.class);
        startActivity(intent);
    }

    public void addIssue(View view) {
        Intent intent = new Intent(WorkerActivity.this, AddIssueActivity.class);
        intent.putExtra("id",currentUserID);
        startActivity(intent);
    }
}