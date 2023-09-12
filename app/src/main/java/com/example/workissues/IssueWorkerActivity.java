package com.example.workissues;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workissues.adapters.MessageAdapter;
import com.example.workissues.utilites.Comment;
import com.example.workissues.utilites.Issue;
import com.example.workissues.utilites.Role;
import com.example.workissues.utilites.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class IssueWorkerActivity extends AppCompatActivity {

    private String taskID;
    private String userID;
    private Issue currentTask;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference tasksCollection = db.collection("tasks");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_worker);

        Intent intent = getIntent();
        taskID = intent.getStringExtra("taskID");
        userID = intent.getStringExtra("userID");

        NumberPicker numberPicker = findViewById(R.id.issue_progress);
        int maxNumber = 100;
        int minNumber = 0;
        numberPicker.setMaxValue(maxNumber);
        numberPicker.setMinValue(minNumber);


        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        tasksCollection.document(taskID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            currentTask = task.getResult().toObject(Issue.class);
                            UpdateFields();
                        }
                    }
                });
        db.collection("users").document(userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult().toObject(User.class).getUserRole() == Role.Prarab){
                                Button sendAndDeleteButton = findViewById(R.id.SendButton);
                                sendAndDeleteButton.setText("Удалить");
                                sendAndDeleteButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        tasksCollection.document(taskID)
                                                .delete();
                                        finish();
                                    }
                                });
                            }
                            else{
                                Button sendAndDeleteButton = findViewById(R.id.SendButton);
                                sendAndDeleteButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ChangeCompletionClick(v);
                                    }
                                });
                            }
                        }
                    }
                });
    }
    private void UpdateFields(){
        TextView titleText = findViewById(R.id.issue_title);
        TextView descriptionText = findViewById(R.id.issue_description);
        titleText.setText(currentTask.getTitle());
        descriptionText.setText(currentTask.getDescription());
        ReloadRecycler();
    }
    private void ReloadRecycler(){
        tasksCollection.document(taskID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            currentTask = task.getResult().toObject(Issue.class);
                        }
                    }
                });
        RecyclerView rec = findViewById(R.id.message_recycler);
        rec.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rec.setHasFixedSize(true);
        MessageAdapter adapter = new MessageAdapter(currentTask.getAllComments(), userID);
        rec.setAdapter(adapter);
    }

    public void SendMessageClick(View view) {
        EditText message = findViewById(R.id.issue_message);
        String text = message.getText().toString();
        if(text == "" || text == null) return;
        Comment newComment = new Comment(userID,text);
        List<Comment> allComments = currentTask.getAllComments();
        allComments.add(newComment);
        currentTask.setAllComments(allComments);
        message.setText("");
        tasksCollection.document(taskID)
                .set(currentTask)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        ReloadRecycler();
                    }
                });
    }

    public void ChangeCompletionClick(View view) {
        NumberPicker numberPicker = findViewById(R.id.issue_progress);
        int chosenNumber = numberPicker.getValue();
        currentTask.setProgress(chosenNumber);
        tasksCollection.document(taskID)
                .set(currentTask)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            Toast.makeText(IssueWorkerActivity.this, "Changed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}