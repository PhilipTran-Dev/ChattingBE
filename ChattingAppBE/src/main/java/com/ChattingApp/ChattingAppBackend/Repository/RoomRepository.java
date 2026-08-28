package com.ChattingApp.ChattingAppBackend.Repository;

import com.ChattingApp.ChattingAppBackend.Entities.Room;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoomRepository extends MongoRepository<Room,String> {
    Room findByRoomId(String roomId);
}
