package com.ChattingApp.ChattingAppBackend.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;


@Document(collection = "rooms")
@Getter
@Setter
@AllArgsConstructor
public class Room {
    @Id
    private String id;
    private String roomId;
    private List<Messages> messages = new ArrayList<>();

    public Room(){}

    public Room(String id, List<Messages> messages, String roomId) {
        this.id = id;
        this.messages = messages;
        this.roomId = roomId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Messages> getMessages() {
        return messages;
    }

    public void setMessages(List<Messages> messages) {
        this.messages = messages;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}