package kaem0n.meetoo.controllers;

import kaem0n.meetoo.entities.User;
import kaem0n.meetoo.payloads.search.SearchResultsDTO;
import kaem0n.meetoo.repositories.UserDAO;
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

    @GetMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public SearchResultsDTO search(@RequestParam String query) {
        List<User> foundUsers = us.findBySearchQuery(query);
        System.out.println(foundUsers);

        return new SearchResultsDTO(foundUsers, null);
    }
}
