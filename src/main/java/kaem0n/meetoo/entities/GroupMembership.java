package kaem0n.meetoo.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "group_memberships")
public class GroupMembership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "membership_id")
    @Setter(AccessLevel.NONE)
    private long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
    private boolean isAdmin;
    private boolean isBanned;
    private boolean isFollowing;

    public GroupMembership(User user, Group group) {
        this.user = user;
        this.group = group;
        this.isAdmin = user == group.getFounder();
        this.isBanned = false;
        this.isFollowing = true;
    }
}
