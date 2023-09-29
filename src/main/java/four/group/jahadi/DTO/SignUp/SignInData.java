package four.group.jahadi.DTO.SignUp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInData {

    private String nid;
    private String password;

    @Override
    public boolean equals(Object o) {

        if(o instanceof SignInData sign) {
            return sign.getNid().equals(nid) &&
                    sign.getPassword().equals(password);
        }

        return false;
    }

}
