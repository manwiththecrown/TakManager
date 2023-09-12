package com.example.workissues.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workissues.IssueWorkerActivity;
import com.example.workissues.R;
import com.example.workissues.utilites.Issue;
import com.example.workissues.utilites.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class IssuesAdapters extends RecyclerView.Adapter<IssuesAdapters.ViewHolder> {

    private ArrayList<Issue> issues;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersCollection = db.collection("users");

    private Context context;
    private String field;
    private String currentUser;
    public IssuesAdapters(ArrayList<Issue> issues, String field, String currentUser) {
        this.issues = issues;
        this.field = field;
        this.currentUser = currentUser;
    }

    @NonNull
    @Override
    public IssuesAdapters.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.issue_card, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IssuesAdapters.ViewHolder holder, int position) {
        holder.Bind(issues.get(position));
    }

    @Override
    public int getItemCount() {
        return issues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        private Issue issue;
        public void Bind(Issue currentIssue){
            issue = currentIssue;
            TextView titleView = itemView.findViewById(R.id.card_title),
                     nameView = itemView.findViewById(R.id.card_from),
                     progressView = itemView.findViewById(R.id.card_progress),
                     dateView = itemView.findViewById(R.id.card_date);

            titleView.setText(issue.getTitle());
            progressView.setText((issue.getProgress())+"");
            if((issue.getProgress()) < 25) progressView.setTextColor(Color.RED);
            else if ((issue.getProgress()) < 70) progressView.setTextColor(Color.YELLOW);
            else progressView.setTextColor(Color.GREEN);
            dateView.setText(issue.getDeadline().toString());
            if(field.contains("to"))
                usersCollection
                        .document(issue.getFrom())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    nameView.setText(task.getResult().toObject(User.class).getName());
                                }
                            }
                        });
            else
                usersCollection
                        .document(issue.getTo())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    nameView.setText(task.getResult().toObject(User.class).getName());
                                }
                            }
                        });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, IssueWorkerActivity.class);
                    intent.putExtra("taskID", issue.getId());
                    intent.putExtra("userID", currentUser);
                    context.startActivity(intent);
                }
            });
        }
    }
}
