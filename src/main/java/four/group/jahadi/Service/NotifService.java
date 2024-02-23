package four.group.jahadi.Service;

import four.group.jahadi.Models.Area.Area;
import four.group.jahadi.Models.Notif;
import four.group.jahadi.Repository.NotifRepository;
import four.group.jahadi.Repository.TripRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotifService {

    @Autowired
    NotifRepository notifRepository;

    @Autowired
    TripRepository tripRepository;

    public void sendTripNotifToAllMembers(Area area, String msg) {
        area.getMembers().forEach(objectId -> {
            Notif notif = new Notif();
            notif.setMsg(msg);
            notif.setOwner(objectId);
            notifRepository.save(notif);
        });
    }

    public ResponseEntity<List<Notif>> getMyUnSeenNotifs(ObjectId userId) {
        return new ResponseEntity<>(notifRepository.findByUserIdAndUnseen(userId), HttpStatus.OK);
    }

    public ResponseEntity<List<Notif>> getMyAllNotifs(ObjectId userId) {
        return new ResponseEntity<>(notifRepository.findByUserId(userId), HttpStatus.OK);
    }
}
