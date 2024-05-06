package kaem0n.meetoo.controllers;

import kaem0n.meetoo.entities.User;
import kaem0n.meetoo.exceptions.BadRequestException;
import kaem0n.meetoo.payloads.GenericResponseDTO;
import kaem0n.meetoo.payloads.user.*;
import kaem0n.meetoo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService us;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public User findById(@PathVariable UUID id) {
        return us.findById(id);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public Page<User> findAll(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(defaultValue = "id") String sort) {
        return us.findAll(page, size, sort);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User updateInfo(@PathVariable UUID id,
                           @Validated @RequestBody UserInfoUpdateDTO payload,
                           BindingResult validation) {
        if (validation.hasErrors()) throw new BadRequestException(validation.getAllErrors());
        else return us.updateInfo(id, payload);
    }

    @PatchMapping("/{id}/changeEmail")
    @PreAuthorize("hasAuthority('ADMIN')")
    public GenericResponseDTO changeEmail(@PathVariable UUID id,
                                          @Validated @RequestBody UserEmailChangeDTO payload,
                                          BindingResult validation) {
        if (validation.hasErrors()) throw new BadRequestException(validation.getAllErrors());
        else return us.changeEmail(id, payload);
    }

    @PatchMapping("/{id}/changeUsername")
    @PreAuthorize("hasAuthority('ADMIN')")
    public GenericResponseDTO changeUsername(@PathVariable UUID id,
                                             @Validated @RequestBody UserUsernameChangeDTO payload,
                                             BindingResult validation) {
        if (validation.hasErrors()) throw new BadRequestException(validation.getAllErrors());
        else return us.changeUsername(id, payload);
    }

    @PatchMapping("/{id}/changePassword")
    @PreAuthorize("hasAuthority('ADMIN')")
    public GenericResponseDTO changePassword(@PathVariable UUID id,
                                             @Validated @RequestBody AdminPasswordChangeDTO payload,
                                             BindingResult validation) {
        if (validation.hasErrors()) throw new BadRequestException(validation.getAllErrors());
        return us.changePassword(id, payload);
    }

    @PatchMapping("/{id}/ban")
    @PreAuthorize("hasAuthority('ADMIN')")
    public GenericResponseDTO handlePlatformBan(@PathVariable UUID id) {
        return us.handlePlatformBan(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public GenericResponseDTO deleteAccount(@PathVariable UUID id) {
        return us.deleteAccount(id);
    }

    @GetMapping("/me")
    public User getMyProfile(@AuthenticationPrincipal User currentAuthenticatedUser) {
        return currentAuthenticatedUser;
    }

    @PutMapping("/me")
    public User updateMyInfo(@AuthenticationPrincipal User currentAuthenticatedUser,
                             @Validated @RequestBody UserInfoUpdateDTO payload,
                             BindingResult validation) {
        if (validation.hasErrors()) throw new BadRequestException(validation.getAllErrors());
        else return us.updateInfo(currentAuthenticatedUser.getId(), payload);
    }

    @PatchMapping("/me/changeEmail")
    public GenericResponseDTO changeMyEmail(@AuthenticationPrincipal User currentAuthenticatedUser,
                                            @Validated @RequestBody UserEmailChangeDTO payload,
                                            BindingResult validation) {
        if (validation.hasErrors()) throw new BadRequestException(validation.getAllErrors());
        else return us.changeEmail(currentAuthenticatedUser.getId(), payload);
    }

    @PatchMapping("/me/changeUsername")
    public GenericResponseDTO changeMyUsername(@AuthenticationPrincipal User currentAuthenticatedUser,
                                               @Validated @RequestBody UserUsernameChangeDTO payload,
                                               BindingResult validation) {
        if (validation.hasErrors()) throw new BadRequestException(validation.getAllErrors());
        else return us.changeUsername(currentAuthenticatedUser.getId(), payload);
    }

    @PatchMapping("/me/changePassword")
    public GenericResponseDTO changeMyPassword(@AuthenticationPrincipal User currentAuthenticatedUser,
                                               @Validated @RequestBody UserPasswordChangeDTO payload,
                                               BindingResult validation) {
        if (validation.hasErrors()) throw new BadRequestException(validation.getAllErrors());
        return us.changeMyPassword(currentAuthenticatedUser.getId(), payload);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public GenericResponseDTO deleteMyAccount(@AuthenticationPrincipal User currentAuthenticatedUser) {
        return us.deleteAccount(currentAuthenticatedUser.getId());
    }
}
