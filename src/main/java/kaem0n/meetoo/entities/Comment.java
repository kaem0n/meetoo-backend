package kaem0n.meetoo.entities;

import java.util.UUID;

public class Comment {
    private UUID id;
    private String content;
    private String imageUrl;
    private User user;
    private Post post;
}
