package com.example.workissues;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.workissues.utilites.Role;
import com.example.workissues.utilites.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference usersCollection = db.collection("users");
    CollectionReference tasksCollection = db.collection("tasks");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void LoginClick(View view) {

        EditText loginText = findViewById(R.id.login_text),
                passwordText = findViewById(R.id.password_text);
        Context context = getApplicationContext();
        usersCollection
                .whereEqualTo("login",loginText.getText().toString())
                .whereEqualTo("password",passwordText.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                            List<User> users = task.getResult().toObjects(User.class);
                            if(users.size() > 0){
                                Intent intent = new Intent(context, WorkerActivity.class);

                                intent.putExtra("id", task.getResult().getDocuments().get(0).getId());
                                intent.putExtra("role",users.get(0).getUserRole().toString());
                                startActivity(intent);
                            }
                            else Toast.makeText(context, "Пользователь не найден", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}