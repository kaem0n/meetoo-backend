package kaem0n.meetoo.controllers;

import kaem0n.meetoo.entities.User;
import kaem0n.meetoo.payloads.GenericResponseDTO;
import kaem0n.meetoo.payloads.user.UserEmailChangeDTO;
import kaem0n.meetoo.payloads.user.UserInfoUpdateDTO;
import kaem0n.meetoo.payloads.user.UserPasswordChangeDTO;
import kaem0n.meetoo.payloads.user.UserUsernameChangeDTO;
import kaem0n.meetoo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService us;

    @GetMapping("/{id}")
    public User findById(@PathVariable UUID id) {
        return us.findById(id);
    }

    @GetMapping
    public Page<User> findAll(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(defaultValue = "id") String sort) {
        return us.findAll(page, size, sort);
    }

    @PutMapping("/{id}")
    public User updateInfo(@PathVariable UUID id, @RequestBody UserInfoUpdateDTO payload) {
        return us.updateInfo(id, payload);
    }

    @PatchMapping("/{id}/changeEmail")
    public GenericResponseDTO changeEmail(@PathVariable UUID id, @RequestBody UserEmailChangeDTO payload) {
        return us.changeEmail(id, payload);
    }

    @PatchMapping("/{id}/changeUsername")
    public GenericResponseDTO changeUsername(@PathVariable UUID id, @RequestBody UserUsernameChangeDTO payload) {
        return us.changeUsername(id, payload);
    }

    @PatchMapping("/{id}/changePassword")
    public GenericResponseDTO changePassword(@PathVariable UUID id, @RequestBody UserPasswordChangeDTO payload) {
        return us.changePassword(id, payload);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public GenericResponseDTO deleteAccount(@PathVariable UUID id) {
        return us.deleteAccount(id);
    }
}
