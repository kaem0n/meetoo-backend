package kaem0n.meetoo.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import kaem0n.meetoo.enums.UserGender;
import kaem0n.meetoo.enums.UserPermissions;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
@JsonIgnoreProperties({"password", "permissions", "board", "memberships", "likedComments", "likedPosts"})
public class User {
    @Id
    @Column(name = "user_id")
    @Setter(AccessLevel.NONE)
    private UUID id;
    private String email;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserPermissions permissions;
    private LocalDate registration;
    private String name;
    private String surname;
    private LocalDate birthday;
    @Column(name = "profile_picture_url")
    private String proPicUrl;
    @Enumerated(EnumType.STRING)
    private UserGender gender;
    private String occupation;
    @Column(columnDefinition = "text")
    private String bio;
    private List<String> hobbies;
    @OneToOne
    @JoinColumn(name = "board_id")
    private Board board;
    @OneToMany(mappedBy = "user")
    private List<GroupMembership> memberships;
    @ManyToMany(mappedBy = "commentLikes")
    private List<Comment> likedComments;
    @ManyToMany(mappedBy = "postLikes")
    private List<Post> likedPosts;

    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.permissions = UserPermissions.USER;
        this.registration = LocalDate.now();
        this.proPicUrl = "https://res.cloudinary.com/kaem0n/image/upload/v1714550501/default_user_icon_nm5w0s.png";
        this.gender = UserGender.UNDEFINED;
    }
}
