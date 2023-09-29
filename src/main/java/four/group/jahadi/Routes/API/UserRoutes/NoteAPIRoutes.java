package four.group.jahadi.Routes.API.UserRoutes;

import four.group.jahadi.DTO.NoteData;
import four.group.jahadi.Exception.NotActivateAccountException;
import four.group.jahadi.Exception.UnAuthException;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.NoteService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "public/note")
@Validated
public class NoteAPIRoutes extends Router {

    @Autowired
    NoteService noteService;

    @PostMapping
    @ResponseBody
    public String store(HttpServletRequest request,
                        @RequestBody @Valid NoteData noteData
    ) throws UnAuthException, NotActivateAccountException {
        return noteService.store(noteData, getUser(request).get_id());
    }

    @DeleteMapping
    @ResponseBody
    public String remove(HttpServletRequest request,
                         @RequestBody @Valid ObjectId objectId
    ) throws UnAuthException, NotActivateAccountException {
        return noteService.remove(objectId, getUser(request).get_id());
    }

}
