package kaem0n.meetoo.entities;

import java.util.List;
import java.util.UUID;

public class Post {
    private UUID id;
    private String content;
    private List<String> mediaUrls;
    private User user;
    private Board board;
    private List<Comment> comments;
}
