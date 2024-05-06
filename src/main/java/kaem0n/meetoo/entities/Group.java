package kaem0n.meetoo.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties({"memberships"})
public class Group {
    @Id
    @Column(name = "group_id")
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    private String name;
    @Column(columnDefinition = "text")
    private String description;
    private LocalDate creation;
    @OneToOne
    @JoinColumn(name = "board_id")
    private Board board;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User founder;
    @OneToMany(mappedBy = "group")
    private List<GroupMembership> memberships;

    public Group(String name, String description, User founder) {
        this.name = name;
        this.description = description;
        this.founder = founder;
        this.creation = LocalDate.now();
    }
}
