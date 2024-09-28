package four.group.jahadi.Routes.API.UserRoutes;

import four.group.jahadi.DTO.NoteData;
import four.group.jahadi.Models.Note;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.NoteService;
import four.group.jahadi.Validator.ObjectIdConstraint;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/public/note")
@Validated
public class NoteAPIRoutes extends Router {

    @Autowired
    NoteService noteService;

    @PostMapping(value = "store")
    @ResponseBody
    public ResponseEntity<Note> store(
            HttpServletRequest request,
            @RequestBody @Valid NoteData noteData
    ) {
        return noteService.store(noteData, getId(request));
    }

    @PutMapping(value = "update/{id}")
    public void update(HttpServletRequest request,
                       @PathVariable @ObjectIdConstraint ObjectId id,
                       @RequestBody @Valid NoteData noteData
    ) {
        noteService.update(id, noteData, getId(request));
    }

    @GetMapping(value = "list")
    @ResponseBody
    public ResponseEntity<List<Note>> list(HttpServletRequest request) {
        return noteService.list(getId(request));
    }

    @GetMapping(value = "get/{id}")
    @ResponseBody
    public ResponseEntity<Note> get(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId id
    ) {
        return noteService.findById(id, getId(request));
    }

    @DeleteMapping(value = "remove/{id}")
    @ResponseBody
    public String remove(
            HttpServletRequest request,
            @PathVariable @ObjectIdConstraint ObjectId id
    ) {
        return noteService.remove(id, getId(request));
    }

}
