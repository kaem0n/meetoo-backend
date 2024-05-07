package kaem0n.meetoo.services;

import kaem0n.meetoo.entities.Board;
import kaem0n.meetoo.entities.User;
import kaem0n.meetoo.enums.UserGender;
import kaem0n.meetoo.exceptions.BadRequestException;
import kaem0n.meetoo.exceptions.NotFoundException;
import kaem0n.meetoo.exceptions.UnauthorizedException;
import kaem0n.meetoo.payloads.GenericResponseDTO;
import kaem0n.meetoo.payloads.user.*;
import kaem0n.meetoo.repositories.BoardDAO;
import kaem0n.meetoo.repositories.UserDAO;
import kaem0n.meetoo.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserDAO ud;
    @Autowired
    private BoardDAO bd;
    @Autowired
    private PasswordEncoder bcrypt;
    @Autowired
    private JWTTools tools;

    public User register(UserRegistrationDTO payload) {
        if (ud.existsByEmail(payload.email())) throw new BadRequestException("Email " + payload.email() + " is already being used.");
        else if (ud.existsByUsername(payload.username())) throw new BadRequestException("Username " + payload.username() + " is not available.");
        else {
            User newUser = ud.save(new User(payload.email(), payload.username(), bcrypt.encode(payload.password())));
            Board board = bd.save(new Board());
            newUser.setBoard(board);
            return ud.save(newUser);
        }
    }

    public UserLoginResponseDTO login(UserLoginDTO payload) {
        User found = this.findByUsername(payload.username());
        if (bcrypt.matches(payload.password(), found.getPassword())) return new UserLoginResponseDTO(tools.createToken(found));
        else throw new UnauthorizedException("Invalid credentials, try again.");
    }

    public User findById(UUID id) {
        return ud.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public User findByUsername(String username) {
        return ud.findByUsername(username).orElseThrow(() -> new NotFoundException("Invalid username."));
    }

    public Page<User> findAll(int page, int size, String sort) {
        if (size > 50) size = 50;
        Pageable p = PageRequest.of(page, size, Sort.by(sort));
        return ud.findAll(p);
    }

    public User updateInfo(UUID id, UserInfoUpdateDTO payload) {
        User found = this.findById(id);

        if (payload.name() != null) found.setName(payload.name());
        if (payload.surname() != null) found.setSurname(payload.surname());
        if (payload.birthday() != null) found.setBirthday(LocalDate.parse(payload.birthday()));
        if (payload.proPicUrl() != null) found.setProPicUrl(payload.proPicUrl());
        if (payload.gender() != null) found.setGender(UserGender.valueOf(payload.gender()));
        if (payload.occupation() != null) found.setOccupation(payload.occupation());
        if (payload.bio() != null) found.setBio(payload.bio());
        if (!payload.hobbies().isEmpty()) found.setHobbies(payload.hobbies());

        return ud.save(found);
    }

    public GenericResponseDTO changeEmail(UUID id, UserEmailChangeDTO payload) {
        User found = this.findById(id);

        if (ud.existsByEmail(payload.email()) && !Objects.equals(found.getEmail(), payload.email())) throw new BadRequestException("Email " + payload.email() + " is already being used.");
        else if (!ud.existsByEmail(payload.email()) && !Objects.equals(found.getEmail(), payload.email())) found.setEmail(payload.email());

        ud.save(found);

        return new GenericResponseDTO("Email successfully changed.");
    }

    public GenericResponseDTO changeUsername(UUID id, UserUsernameChangeDTO payload) {
        User found = this.findById(id);

        if (ud.existsByUsername(payload.username()) && !Objects.equals(found.getUsername(), payload.username())) throw new BadRequestException("Username " + payload.username() + " is not available.");
        else if (!ud.existsByUsername(payload.username()) && !Objects.equals(found.getUsername(), payload.username())) found.setUsername(payload.username());

        ud.save(found);

        return new GenericResponseDTO("Username successfully changed.");
    }

    public GenericResponseDTO changeMyPassword(UUID id, UserPasswordChangeDTO payload) {
        User found = this.findById(id);

        if (bcrypt.matches(found.getPassword(), payload.oldPassword())) found.setPassword(bcrypt.encode(payload.newPassword()));

        ud.save(found);

        return new GenericResponseDTO("Password successfully changed.");
    }

    public GenericResponseDTO changePassword(UUID id, AdminPasswordChangeDTO payload) {
        User found = this.findById(id);

        found.setPassword(bcrypt.encode(payload.newPassword()));

        ud.save(found);

        return new GenericResponseDTO("Password successfully changed.");
    }

    public void deleteAccount(UUID id) {
        User found = this.findById(id);

        bd.delete(found.getBoard());
        ud.delete(found);
    }

    public GenericResponseDTO handlePlatformBan(UUID id) {
        User found = this.findById(id);

        found.setAccountNonLocked(!found.isAccountNonLocked());
        ud.save(found);

        if (!found.isAccountNonLocked()) return new GenericResponseDTO("User ID '" + id + "' account permanently banned from the platform.");
        else return new GenericResponseDTO("User ID '" + id + "' account account lock has been lifted.");
    }

    public List<User> findBySearchQuery(String query) {
        return ud.searchUsers(query);
    }
}
