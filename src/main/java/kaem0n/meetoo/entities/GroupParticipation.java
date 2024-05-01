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
@Table(name = "group_participations")
public class GroupParticipation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participation_id")
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

    public GroupParticipation(User user, Group group) {
        this.user = user;
        this.group = group;
        this.isAdmin = user == group.getFounder();
        this.isBanned = false;
    }
}
