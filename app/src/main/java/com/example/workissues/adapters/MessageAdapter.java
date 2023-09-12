package com.example.workissues.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workissues.R;
import com.example.workissues.utilites.Comment;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Comment> comments;
    private String currentUser;

    public MessageAdapter(List<Comment> comments, String currentUser) {
        this.comments = comments;
        this.currentUser = currentUser;
    }

    private Resources resources;

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        resources = parent.getContext().getResources();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.message_box, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        holder.Bind(comments.get(position));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        public void Bind(Comment comment){
            ConstraintLayout background = itemView.findViewById(R.id.message_card_background);
            if(comment.getFrom().contains(currentUser)){
                background.setBackgroundColor(resources.getColor(R.color.self_message));
            }else{
                background.setBackgroundColor(resources.getColor(R.color.out_message));
            }
            TextView messageText = itemView.findViewById(R.id.message_card_text);
            messageText.setText(comment.getMessage());
        }
    }
}
