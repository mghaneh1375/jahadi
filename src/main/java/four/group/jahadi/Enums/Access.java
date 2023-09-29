package four.group.jahadi.Enums;

import org.springframework.security.core.GrantedAuthority;

public enum Access implements GrantedAuthority {

    ADMIN, GROUP, REPORTER, TRIP_REPORTER, WAREHOUSE_KEEPER, JAHADI;

    public String getName() {
        return name().toLowerCase();
    }

    public String getAuthority() {
        return name();
    }

}
