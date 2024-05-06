package kaem0n.meetoo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "comments")
@JsonIgnoreProperties({"post", "commentLikes"})
public class Comment {
    @Id
    @Column(name = "comment_id")
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    @Column(columnDefinition = "text")
    private String content;
    @Column(name = "image_url")
    private String imageUrl;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    @ManyToMany
    @JoinTable(name = "comment_likes",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> commentLikes;

    // COMMENT WITHOUT IMAGE
    public Comment(String content, User user, Post post) {
        this.content = content;
        this.user = user;
        this.post = post;
    }
    // COMMENT WITH IMAGE
    public Comment(String content, String imageUrl, User user, Post post) {
        this.content = content;
        this.imageUrl = imageUrl;
        this.user = user;
        this.post = post;
    }
}
