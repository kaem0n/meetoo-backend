package kaem0n.meetoo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "boards")
public class Board {
    @Id
    @Column(name = "board_id")
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    @OneToMany(mappedBy = "board")
    @JsonIgnore
    private List<Post> posts;
}
