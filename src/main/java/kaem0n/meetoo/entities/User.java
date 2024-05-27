package kaem0n.meetoo.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import kaem0n.meetoo.enums.UserDateFormat;
import kaem0n.meetoo.enums.UserGender;
import kaem0n.meetoo.enums.UserPermissions;
import kaem0n.meetoo.enums.UserTimeFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
@JsonIgnoreProperties({"password", "permissions", "followingList", "followedByList", "memberships", "likedComments", "likedPosts",
        "authorities", "accountNonExpired", "credentialsNonExpired", "accountNonLocked", "enabled"})
public class User implements UserDetails {
    @Id
    @Column(name = "user_id")
    @GeneratedValue
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
    @Enumerated(EnumType.STRING)
    private UserDateFormat dateFormat;
    @Enumerated(EnumType.STRING)
    private UserTimeFormat timeFormat;
    @Column(nullable = false)
    private boolean lightTheme;
    @OneToOne
    @JoinColumn(name = "board_id")
    private Board board;
    @OneToMany(mappedBy = "follower")
    private List<UserFollow> followingList;
    @OneToMany(mappedBy = "followed")
    private List<UserFollow> followedByList;
    @OneToMany(mappedBy = "user")
    private List<GroupMembership> memberships;
    @ManyToMany(mappedBy = "commentLikes")
    private List<Comment> likedComments;
    @ManyToMany(mappedBy = "postLikes")
    private List<Post> likedPosts;
    private boolean accountNonLocked;

    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.permissions = UserPermissions.USER;
        this.registration = LocalDate.now();
        this.proPicUrl = "https://res.cloudinary.com/kaem0n/image/upload/v1714550501/default_user_icon_nm5w0s.png";
        this.gender = UserGender.UNDEFINED;
        this.dateFormat = UserDateFormat.YMD;
        this.timeFormat = UserTimeFormat.H24;
        this.lightTheme = true;
        this.accountNonLocked = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(permissions.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
