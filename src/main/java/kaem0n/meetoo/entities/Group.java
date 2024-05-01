package kaem0n.meetoo.entities;

import jakarta.persistence.*;
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
@Table(name = "groups")
public class Group {
    @Id
    @Column(name = "group_id")
    @Setter(AccessLevel.NONE)
    private UUID id;
    private String name;
    @Column(columnDefinition = "text")
    private String description;
    private LocalDate creation;
    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User founder;
    @OneToMany(mappedBy = "group")
    private List<GroupParticipation> participations;

    public Group(String name, String description, User founder) {
        this.name = name;
        this.description = description;
        this.founder = founder;
    }
}
