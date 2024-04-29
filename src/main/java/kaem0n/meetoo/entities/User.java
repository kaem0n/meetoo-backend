package kaem0n.meetoo.entities;

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
    @Column(name = "about_me", columnDefinition = "text")
    private String aboutMe;
    private List<String> hobbies;
    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;
    @OneToMany(mappedBy = "user")
    private List<GroupParticipation> participations;
}
