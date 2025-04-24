package four.group.jahadi.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import four.group.jahadi.DTO.Digest.DTO;
import four.group.jahadi.DTO.UserData;
import four.group.jahadi.Exception.InvalidFieldsException;
import four.group.jahadi.Models.Model;
import four.group.jahadi.Models.ModelWithUser;
import four.group.jahadi.Models.User;
import four.group.jahadi.Utility.Utility;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public abstract class AbstractService <T> {

    @Autowired
    ModelMapper modelMapper;

    JSONArray convertObjectsToJSONList(List<T> list) {

        JSONArray jsonArray = new JSONArray();

        list.forEach(x -> {

            Model modelWithUser = (Model) x;

            JSONObject jsonObject = new JSONObject(x);
//            jsonObject.remove("_id");
//            jsonObject.put("id", modelWithUser.get_id().toString());

//            if(jsonObject.has("createdAt"))
//                jsonObject.put("createdAt", Utility.convertDateToJalali(modelWithUser.getCreatedAt()));

            jsonArray.put(jsonObject);
        });

        return jsonArray;

    }

    static JSONObject getDTO(Object object) {
        try {
            JSONObject jsonObject = new JSONObject(new ObjectMapper().writeValueAsString(object));
            jsonObject.put("id", ((DTO)object).get_id().toString());
            jsonObject.remove("_id");
            return jsonObject;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    JSONArray convertObjectsToJSONList(List<T> list, List<User> users) {

        JSONArray jsonArray = new JSONArray();

        list.forEach(x -> {

            ModelWithUser modelWithUser = (ModelWithUser) x;

            JSONObject jsonObject = new JSONObject(x);
            jsonObject.remove("_id");
            jsonObject.put("id", modelWithUser.getId().toString());

            if(jsonObject.has("createdAt"))
                jsonObject.put("createdAt", Utility.convertDateToJalali(modelWithUser.getCreatedAt()));

            User user = users.stream().filter(itr -> modelWithUser.getOwner().equals(itr.getId())).findFirst().orElse(null);
            JSONObject userJSON = user == null ? null : new JSONObject(modelMapper.map(user, UserData.class));

            if(user != null)
                userJSON.put("pic", user.getPic());

            jsonObject.put("owner", userJSON);

            jsonArray.put(jsonObject);
        });

        return jsonArray;
    }

    public abstract ResponseEntity<List<T>> list(Object ... filters);

    public abstract ResponseEntity<T> findById(ObjectId id, Object ...params);

    void validateString(String val, String key, int min, int max) {
        if (val == null || val.length() < min || val.length() > max)
            throw new InvalidFieldsException(key + " باید حداقل 2 کاراکتر و حداکثر 100 کاراکتر باشد");
    }
}
