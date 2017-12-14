package com.android.jianchen.rentme.model.rentme;

/**
 * Created by emerald on 6/12/2017.
 */
public class MessageModel {
    private int projectId;
    private String message;
    private int sender;
    private int receiver;
    private String timestamp;
    private int checked;

    public MessageModel() {
    }

    public MessageModel(int pId, String message, int sender, int receiver, String timestamp, int checked) {
        this.projectId = pId;
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.timestamp = timestamp;
        this.checked = checked;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }
}
