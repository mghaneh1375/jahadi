package four.group.jahadi.Service;

import four.group.jahadi.DTO.NoteData;
import four.group.jahadi.Exception.InvalidIdException;
import four.group.jahadi.Exception.NotAccessException;
import four.group.jahadi.Models.Note;
import four.group.jahadi.Repository.NoteRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static four.group.jahadi.Utility.StaticValues.JSON_NOT_ACCESS;
import static four.group.jahadi.Utility.StaticValues.JSON_OK;

@Service
public class NoteService extends AbstractService<Note, NoteData> {

    @Autowired
    NoteRepository noteRepository;

    @Override
    public ResponseEntity<List<Note>> list(Object... filters) {
        return new ResponseEntity<>(
                noteRepository.findByUserId((ObjectId) filters[0]),
                HttpStatus.OK
        );
    }

    @Override
    public void update(ObjectId id, NoteData dto, Object ... params) {

        Note note = noteRepository.findById(id).orElseThrow(InvalidIdException::new);

        if(!note.getUserId().equals(params[0]))
            throw new NotAccessException();

        note.setUpdatedAt(LocalDateTime.now());
        note.setTitle(dto.getTitle());
        note.setDescription(dto.getDescription());
        noteRepository.save(note);
    }

    @Override
    public ResponseEntity<Note> store(NoteData dto, Object ... params) {

        ObjectId userId = (ObjectId) params[0];

        Note note = new Note();
        note.setDescription(dto.getDescription());
        note.setTitle(dto.getTitle());
        note.setUserId(userId);
        note.setUpdatedAt(LocalDateTime.now());

        note = noteRepository.insert(note);

        return new ResponseEntity<>(note, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Note> findById(ObjectId id, Object ... params) {

        Note note = noteRepository.findById(id).orElseThrow(InvalidIdException::new);

        if(!note.getUserId().equals(params[0]))
            throw new NotAccessException();

        return new ResponseEntity<>(note, HttpStatus.OK);
    }

    public String remove(ObjectId id, ObjectId userId) {

        Note note = noteRepository.findById(id).orElseThrow(InvalidIdException::new);

        if(!note.getUserId().equals(userId))
            return JSON_NOT_ACCESS;

        noteRepository.delete(note);
        return JSON_OK;
    }
}
