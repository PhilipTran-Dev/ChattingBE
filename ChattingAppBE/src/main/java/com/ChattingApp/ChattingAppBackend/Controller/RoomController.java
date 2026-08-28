package com.ChattingApp.ChattingAppBackend.Controller;

import com.ChattingApp.ChattingAppBackend.Entities.Messages;
import com.ChattingApp.ChattingAppBackend.Entities.Room;
import com.ChattingApp.ChattingAppBackend.Repository.RoomRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v1/rooms")
@CrossOrigin("*")

public class RoomController {
    private RoomRepository roomRepository;

    public RoomController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }
    //create rooom
    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody String roomId){
        //room exists
        if(roomRepository.findByRoomId(roomId)!=null){
            return ResponseEntity.badRequest().body("Room is already exists!");
        }
        //create new room
        Room room = new Room();
        room.setRoomId(roomId);
        Room savedRoom = roomRepository.save(room);
        return ResponseEntity.status(HttpStatus.CREATED).body(room);
    }

    //get room: join
    @GetMapping("/{roomId}")
    public ResponseEntity<?> joinRoom(@PathVariable String roomId){
        Room room = roomRepository.findByRoomId(roomId);
        if(room == null){
            return ResponseEntity.badRequest().body("Room not found");
        }
        return ResponseEntity.ok(room);
    }

    //get message in room
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<Messages>> getMessages(
            @PathVariable String roomId,
            @RequestParam(value = "page",defaultValue = "0",required = false) int page,
            @RequestParam(value = "size",defaultValue = "10",required = false) int size
    ){
        Room room = roomRepository.findByRoomId(roomId);
        if(room == null){
            return ResponseEntity.badRequest().build();
        }

        //get message
        //pagination
        List<Messages> messages = room.getMessages();
        int start = Math.max(0,messages.size()-(page+1)*size);
        int end = Math.min(messages.size(),start+size);
        List<Messages> paginatedMessage = messages.subList(start,end);
        return ResponseEntity.ok(paginatedMessage);


    }

}
