package kaem0n.meetoo.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @Column(name = "post_id")
    @Setter(AccessLevel.NONE)
    private UUID id;
    @Column(columnDefinition = "text")
    private String content;
    @Column(name = "media_urls")
    private List<String> mediaUrls;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;
    @OneToMany(mappedBy = "post")
    private List<Comment> comments;
    @ManyToMany
    @JoinTable(name = "post_likes",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> postLikes;

    // POST WITHOUT MEDIA
    public Post(String content, User user, Board board) {
        this.content = content;
        this.user = user;
        this.board = board;
    }

    // POST WITH MEDIA
    public Post(String content, List<String> mediaUrls, User user, Board board) {
        this.content = content;
        this.mediaUrls = mediaUrls;
        this.user = user;
        this.board = board;
    }
}
