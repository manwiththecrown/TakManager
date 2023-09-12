package com.example.workissues;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.workissues.utilites.Role;
import com.example.workissues.utilites.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class UserAddMenu extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference usersCollection = db.collection("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add_menu);
    }

    public void AddNewWorker(View view) {
        EditText fioText,loginText,passwordText;
        fioText = findViewById(R.id.editText2);
        loginText = findViewById(R.id.editText);
        passwordText = findViewById(R.id.editText3);

        String fio = fioText.getText().toString();
        String login = loginText.getText().toString();
        String password = passwordText.getText().toString();
        if (fio.matches("")){
            Toast.makeText(this, "Введите ФИО", Toast.LENGTH_SHORT).show();
            return;
        }
        if (login.matches("")){
            Toast.makeText(this, "Введите логин", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.matches("")){
            Toast.makeText(this, "Введите пароль", Toast.LENGTH_SHORT).show();
            return;
        }
        usersCollection.add(new User(
                "",
                fio,
                login,
                password,
                Role.Worker
        ));
        Toast.makeText(this, "Пользователь добавлен", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void DeleteUser(View view) {

        EditText fioText;
        fioText = findViewById(R.id.editText2);
        String fio = fioText.getText().toString();

        if (fio.matches("")){
            Toast.makeText(this, "Введите ФИО", Toast.LENGTH_SHORT).show();
            return;
        }
        usersCollection.whereEqualTo("name", fio)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (DocumentSnapshot document:
                                 task.getResult()) {
                                usersCollection.document(document.getId())
                                        .delete();
                                db.collection("tasks")
                                        .whereEqualTo("from", document.getId())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful()){
                                                    for (DocumentSnapshot document:
                                                            task.getResult()){
                                                        db.collection("tasks").document(document.getId()).delete();
                                                    }
                                                }
                                            }
                                        });
                                db.collection("tasks")
                                        .whereEqualTo("to", document.getId())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful()){
                                                    for (DocumentSnapshot document:
                                                            task.getResult()){
                                                        db.collection("tasks").document(document.getId()).delete();
                                                    }
                                                }
                                            }
                                        });
                            }
                            Toast.makeText(UserAddMenu.this, "Сотрудник удалён", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}