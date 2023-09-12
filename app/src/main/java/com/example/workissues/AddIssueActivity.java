package com.example.workissues;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.workissues.utilites.Issue;
import com.example.workissues.utilites.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddIssueActivity extends AppCompatActivity {

    LinearLayout usersView;
    String currentUser = null, chosenUser = null;
    Date chosenDate = null;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference usersCollection = db.collection("users");
    CollectionReference tasksCollection = db.collection("tasks");

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_issue);
        usersView = findViewById(R.id.all_workers_list);
        context = getApplicationContext();
        currentUser = getIntent().getStringExtra("id");
        DatePicker picker = findViewById(R.id.issue_add_date_end);
        picker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    chosenDate = sdf.parse(selectedDate);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        UpdateList();
    }
    void UpdateList(){
        usersCollection
                .whereEqualTo("userRole", "Worker")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            List<User> users = new ArrayList<>();
                            for (DocumentSnapshot document:
                                 task.getResult()) {
                                User newUser = document.toObject(User.class);
                                newUser.setId(document.getId());
                                users.add(newUser);
                                Button userChooseButton =  (Button) LayoutInflater.from(context).inflate(R.layout.sample_button, null);
                                userChooseButton.setText(newUser.getName());
                                userChooseButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        chosenUser = newUser.getId();
                                        Toast.makeText(context, "Вы выбрали "+newUser.getName(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                usersView.addView(userChooseButton);
                            }

                        }
                    }
                });
    }

    public void AddNewIssue(View view) {
        EditText titleView = findViewById(R.id.issue_add_title), descriptionView = findViewById(R.id.issue_add_description);
        String title = titleView.getText().toString(),
                description = descriptionView.getText().toString();
        if(!title.matches("") && !description.matches("") &&
            chosenDate != null && chosenUser != null){
            tasksCollection.add(new Issue(
                    "",
                    title,
                    description,
                    new Date(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) * 1000),
                    chosenDate,
                    0,
                    currentUser,
                    chosenUser,
                    new ArrayList<>()

            ));
            Toast.makeText(context, "Задача добавлена", Toast.LENGTH_SHORT).show();
            finish();
        }
        else Toast.makeText(context, "Проверьте правильность заполнения значений", Toast.LENGTH_SHORT).show();
    }
}