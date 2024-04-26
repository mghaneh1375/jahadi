package four.group.jahadi.Service;

import four.group.jahadi.DTO.UpdatePresenceList;
import four.group.jahadi.Exception.InvalidFieldsException;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.PresenceList;
import four.group.jahadi.Models.User;
import four.group.jahadi.Models.UserPresenceList;
import four.group.jahadi.Repository.PresenceListRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AreaPresenceService {

    @Autowired
    private PresenceListRepository presenceListRepository;

    public void submitEntrance(ObjectId areaId, ObjectId userId) {
        presenceListRepository.insert(PresenceList
                .builder()
                .areaId(areaId)
                .userId(userId)
                .entrance(new Date())
                .build()
        );
    }

    public void updateEntrance(
            ObjectId areaId, ObjectId userId,
            ObjectId presenceListId, UpdatePresenceList data
    ) {
        PresenceList presenceList = presenceListRepository.findById(presenceListId).orElseThrow(InvalidIdException::new);
        if (!presenceList.getAreaId().equals(areaId) ||
                !presenceList.getUserId().equals(userId)
        )
            throw new NotAccessException();

        if(data.getJustSetExit() != null && data.getJustSetExit())
            presenceList.setExit(new Date());
        else {
            if(data.getEntrance() != null)
                presenceList.setEntrance(data.getEntrance());
            if(data.getExit() != null)
                presenceList.setExit(data.getExit());

            if(presenceList.getEntrance() != null && presenceList.getExit() != null &&
                    presenceList.getExit().before(presenceList.getEntrance())
            )
                throw new InvalidFieldsException("زمان ورود باید قبل از خروج باشد");
        }

        presenceListRepository.save(presenceList);
    }

    public List<UserPresenceList> getAreaPresenceList(ObjectId areaId, List<User> members) {
        List<PresenceList> presenceLists = presenceListRepository.findByAreaId(areaId);

        List<UserPresenceList> outputList = new ArrayList<>();
        members.forEach(member -> {
            outputList.add(
                    UserPresenceList
                            .builder()
                            .user(member)
                            .dates(
                                    presenceLists.stream()
                                            .filter(presenceList -> presenceList.getUserId().equals(member.getId()))
                                            .map(presenceList -> UserPresenceList.Dates
                                                    .builder()
                                                    .entrance(presenceList.getEntrance())
                                                    .exit(presenceList.getExit())
                                                    .build()
                                            )
                                            .collect(Collectors.toList())
                            ).build()
            );
        });

        return outputList;
    }

}
