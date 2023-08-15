package four.group.jahadi.Models;

import org.springframework.security.core.GrantedAuthority;

public enum Section implements GrantedAuthority {

    MEDICAL, DENTAL;

    public String getAuthority() {
        return name();
    }

}
