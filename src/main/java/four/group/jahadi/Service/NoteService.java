package four.group.jahadi.Service;

import four.group.jahadi.DTO.NoteData;
import four.group.jahadi.Models.Note;
import four.group.jahadi.Repository.NoteRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static four.group.jahadi.Utility.StaticValues.*;
import static four.group.jahadi.Utility.Utility.generateSuccessMsg;

@Service
public class NoteService extends AbstractService<Note, NoteData> {

    @Autowired
    NoteRepository noteRepository;

    @Override
    public String list(Object... filters) {
        return null;
    }

    @Override
    public String update(ObjectId id, NoteData dto, Object ... params) {



        return JSON_OK;
    }

    @Override
    public String store(NoteData dto, Object ... params) {

        ObjectId userId = (ObjectId) params[0];

        Note note = new Note();
        note.setDescription(dto.getDescription());
        note.setTitle(dto.getTitle());
        note.setUserId(userId);

        note = noteRepository.insert(note);

        return generateSuccessMsg("data", note.get_id().toString());
    }

    @Override
    Note findById(ObjectId id) {
        return null;
    }

    public String remove(ObjectId id, ObjectId userId) {

        Optional<Note> note = noteRepository.findById(id);

        if(note.isEmpty())
            return JSON_NOT_VALID_ID;

        if(!note.get().get_id().equals(userId))
            return JSON_NOT_ACCESS;

        noteRepository.delete(note.get());
        return JSON_OK;
    }
}
