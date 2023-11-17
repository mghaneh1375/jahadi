package four.group.jahadi.Routes.API.GroupAPIRoutes;

import four.group.jahadi.Enums.Access;
import four.group.jahadi.Models.Project;
import four.group.jahadi.Routes.Router;
import four.group.jahadi.Service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/api/public/project")
@Validated
public class ManageProjectAPIRoutes extends Router {

    @Autowired
    private ProjectService projectService;

    @PutMapping(value = "/myProjects")
    public ResponseEntity<List<Project>> myProjects(HttpServletRequest request) {
        boolean groupAccess = ((UsernamePasswordAuthenticationToken)request.getUserPrincipal()).getAuthorities().contains(Access.GROUP);
        return projectService.myProjects(
                groupAccess ? getGroup(request) : null,
                groupAccess ? null : getId(request)
        );
    }

}
