package com.ChattingApp.ChattingAppBackend.Payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class MessageRequest {
    private String content;
    private String sender;
    private String roomId;
    private LocalDateTime messageTime;

    public MessageRequest(){}
    public MessageRequest(String content, LocalDateTime messageTime, String roomId, String sender) {
        this.content = content;
        this.messageTime = messageTime;
        this.roomId = roomId;
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(LocalDateTime messageTime) {
        this.messageTime = messageTime;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}