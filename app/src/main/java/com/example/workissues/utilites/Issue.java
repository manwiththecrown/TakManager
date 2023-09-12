package com.example.workissues.utilites;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Issue {
    private String id;
    private String title;
    private String description;
    private Date startDate;
    private Date deadline;
    private float progress;

    private String from;
    private String to;
    private List<Comment> allComments = new ArrayList<>();

    public Issue(){}

    public Issue(String id, String title, String description, Date startDate, Date deadline, float progress, String from, String to, List<Comment> allComments) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.deadline = deadline;
        this.progress = progress;
        this.from = from;
        this.to = to;
        this.allComments = allComments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public List<Comment> getAllComments() {
        return allComments;
    }

    public void setAllComments(List<Comment> allComments) {
        this.allComments = allComments;
    }
}
