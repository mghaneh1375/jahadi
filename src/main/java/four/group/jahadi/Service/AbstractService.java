package four.group.jahadi.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import four.group.jahadi.DTO.Digest.DTO;
import four.group.jahadi.DTO.UserData;
import four.group.jahadi.Models.Model;
import four.group.jahadi.Models.ModelWithUser;
import four.group.jahadi.Models.PaginatedResponse;
import four.group.jahadi.Models.User;
import four.group.jahadi.Utility.Utility;
import org.bson.types.ObjectId;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;

public abstract class AbstractService <T, D> {

    @Autowired
    ModelMapper modelMapper;

    JSONArray convertObjectsToJSONList(List<T> list) {

        JSONArray jsonArray = new JSONArray();

        list.forEach(x -> {

            Model modelWithUser = (Model) x;

            JSONObject jsonObject = new JSONObject(x);
            jsonObject.remove("_id");
            jsonObject.put("id", modelWithUser.get_id().toString());

            if(jsonObject.has("createdAt"))
                jsonObject.put("createdAt", Utility.convertDateToJalali(modelWithUser.getCreatedAt()));

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
            jsonObject.put("id", modelWithUser.get_id().toString());

            if(jsonObject.has("createdAt"))
                jsonObject.put("createdAt", Utility.convertDateToJalali(modelWithUser.getCreatedAt()));

            User user = users.stream().filter(itr -> modelWithUser.getOwner().equals(itr.get_id())).findFirst().orElse(null);
            JSONObject userJSON = user == null ? null : new JSONObject(modelMapper.map(user, UserData.class));

            if(user != null)
                userJSON.put("pic", user.getPic());

            jsonObject.put("owner", userJSON);

            jsonArray.put(jsonObject);
        });

        return jsonArray;
    }

    public abstract String list(Object ... filters);

    abstract String update(ObjectId id, D dto, Object ... params);

    abstract String store(D dto, Object ... params);

    abstract T findById(ObjectId id);

    T populateEntity(T t, D d) {
        return (T) modelMapper.map(d, t.getClass());
    }

}
