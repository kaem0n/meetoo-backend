package kaem0n.meetoo.controllers;

import kaem0n.meetoo.entities.Comment;
import kaem0n.meetoo.entities.Group;
import kaem0n.meetoo.entities.Post;
import kaem0n.meetoo.entities.User;
import kaem0n.meetoo.payloads.search.SearchResultsDTO;
import kaem0n.meetoo.services.CommentService;
import kaem0n.meetoo.services.GroupService;
import kaem0n.meetoo.services.PostService;
import kaem0n.meetoo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {
    @Autowired
    private UserService us;
    @Autowired
    private GroupService gs;
    @Autowired
    private PostService ps;
    @Autowired
    private CommentService cs;

    @GetMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public SearchResultsDTO search(@RequestParam String query) {
        List<User> foundUsers = us.findBySearchQuery(query);
        List<Group> foundGroups = gs.findBySearchQuery(query);
        List<Post> foundPosts = ps.findBySearchQuery(query);
        List<Comment> foundComments = cs.findBySearchQuery(query);

        return new SearchResultsDTO(foundUsers, foundGroups, foundPosts, foundComments);
    }
}
