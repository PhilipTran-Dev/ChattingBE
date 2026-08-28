package com.ChattingApp.ChattingAppBackend.Controller;

import com.ChattingApp.ChattingAppBackend.Entities.Messages;
import com.ChattingApp.ChattingAppBackend.Entities.Room;
import com.ChattingApp.ChattingAppBackend.Payload.MessageRequest;
import com.ChattingApp.ChattingAppBackend.Repository.RoomRepository;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Controller
@CrossOrigin("*")
public class ChatController {
    private RoomRepository roomRepository;

    public ChatController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    //sending and reciving message
    @MessageMapping("sendMessage/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public Messages sendMessage(
            @DestinationVariable String roomId,
            @RequestBody MessageRequest request
    ){
        Room room  = roomRepository.findByRoomId(request.getRoomId());
        Messages messages = new Messages();
        messages.setContent(request.getContent());
        messages.setSender(request.getSender());
        messages.setTimeStamp(LocalDateTime.now());

        if(room != null){
            room.getMessages().add(messages);
            roomRepository.save(room);
        }else {
            throw new RuntimeException("Room not found!");
        }
        return messages;
    }

}