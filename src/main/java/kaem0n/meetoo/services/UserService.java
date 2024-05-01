package kaem0n.meetoo.services;

import kaem0n.meetoo.entities.Board;
import kaem0n.meetoo.entities.User;
import kaem0n.meetoo.enums.UserGender;
import kaem0n.meetoo.exceptions.BadRequestException;
import kaem0n.meetoo.exceptions.NotFoundException;
import kaem0n.meetoo.payloads.GenericResponseDTO;
import kaem0n.meetoo.payloads.user.*;
import kaem0n.meetoo.repositories.BoardDAO;
import kaem0n.meetoo.repositories.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserDAO ud;
    @Autowired
    private BoardDAO bd;

    public User register(UserRegistrationDTO payload) {
        if (ud.existsByEmail(payload.email())) throw new BadRequestException("Email " + payload.email() + " is already being used.");
        else if (ud.existsByUsername(payload.username())) throw new BadRequestException("Username " + payload.username() + " is not available.");
        else {
            User newUser = ud.save(new User(payload.email(), payload.username(), payload.password()));
            Board board = bd.save(new Board());
            newUser.setBoard(board);
            return ud.save(newUser);
        }
    }

    public User findById(UUID id) {
        return ud.findById(id).orElseThrow(() -> new NotFoundException(id));
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

    public GenericResponseDTO updateEmail(UUID id, UserEmailChangeDTO payload) {
        User found = this.findById(id);

        if (ud.existsByEmail(payload.email()) && !Objects.equals(found.getEmail(), payload.email())) throw new BadRequestException("Email " + payload.email() + " is already being used.");
        else if (!ud.existsByEmail(payload.email()) && !Objects.equals(found.getEmail(), payload.email())) found.setEmail(payload.email());

        ud.save(found);

        return new GenericResponseDTO("Email successfully changed.");
    }

    public GenericResponseDTO updateUsername(UUID id, UserUsernameChangeDTO payload) {
        User found = this.findById(id);

        if (ud.existsByUsername(payload.username()) && !Objects.equals(found.getUsername(), payload.username())) throw new BadRequestException("Username " + payload.username() + " is not available.");
        else if (!ud.existsByUsername(payload.username()) && !Objects.equals(found.getUsername(), payload.username())) found.setUsername(payload.username());

        ud.save(found);

        return new GenericResponseDTO("Username successfully changed.");
    }

    public GenericResponseDTO updatePassword(UUID id, UserPasswordChangeDTO payload) {
        User found = this.findById(id);

        if (Objects.equals(found.getPassword(), payload.oldPassword())) found.setPassword(payload.newPassword());

        ud.save(found);

        return new GenericResponseDTO("Password successfully changed.");
    }

    public GenericResponseDTO deleteAccount(UUID id) {
        User found = this.findById(id);

        bd.delete(found.getBoard());
        ud.delete(found);

        return new GenericResponseDTO("User ID '" + id + "' account permanently deleted.");
    }
}
