package kaem0n.meetoo.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_follows")
public class UserFollow {
    @Id
    @Column(name = "follow_id")
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    private LocalDate followDate;
    @ManyToOne
    @JoinColumn(name = "follower_id")
    private User follower;
    @ManyToOne
    @JoinColumn(name = "followed_id")
    private User followed;

    public UserFollow(User follower, User followed) {
        this.follower = follower;
        this.followed = followed;
        this.followDate = LocalDate.now();
    }
}
