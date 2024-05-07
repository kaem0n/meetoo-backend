package kaem0n.meetoo.payloads.search;

import kaem0n.meetoo.entities.Group;
import kaem0n.meetoo.entities.Post;
import kaem0n.meetoo.entities.User;

import java.util.List;

public record SearchResultsDTO(List<User> users, List<Group> groups, List<Post> posts) {
}
