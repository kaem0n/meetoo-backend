package kaem0n.meetoo.entities;

import kaem0n.meetoo.enums.UserGender;
import kaem0n.meetoo.enums.UserPermissions;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class User {
    private UUID id;
    private String email;
    private String username;
    private String password;
    private UserPermissions permissions;
    private LocalDate registration;
    private String name;
    private String surname;
    private LocalDate birthday;
    private String proPicUrl;
    private UserGender gender;
    private String occupation;
    private String aboutMe;
    private List<String> hobbies;
    private Board board;
}
