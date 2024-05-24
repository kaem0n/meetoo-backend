package kaem0n.meetoo.controllers;

import kaem0n.meetoo.entities.GroupMembership;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
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

    @PatchMapping("/{id}/changeProPic")
    @PreAuthorize("hasAuthority('ADMIN')")
    public GenericResponseDTO changeProPic(@PathVariable UUID id,
                                           @RequestParam("image") MultipartFile img) throws IOException {
        return us.changeProPic(id, img);
    }

    @PatchMapping("/{id}/removeProPic")
    @PreAuthorize("hasAuthority('ADMIN')")
    public GenericResponseDTO removeProPic(@PathVariable UUID id) throws IOException {
        return us.removeProPic(id);
    }

    @PatchMapping("/{id}/ban")
    @PreAuthorize("hasAuthority('ADMIN')")
    public GenericResponseDTO handlePlatformBan(@PathVariable UUID id) {
        return us.handlePlatformBan(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteAccount(@PathVariable UUID id) {
        us.deleteAccount(id);
    }

    @GetMapping("/{id}/following")
    public List<UserEssentialsDTO> getFollowingList(@PathVariable UUID id) {
        return us.getFollowingList(id);
    }

    @GetMapping("/{id}/followedBy")
    public List<UserEssentialsDTO> getFollowedByList(@PathVariable UUID id) {
        return us.getFollowedByList(id);
    }

    @GetMapping("/{id}/memberships")
    public List<GroupMembership> getMemberships(@PathVariable UUID id) {
        return us.getMemberships(id);
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
        else return us.changeMyPassword(currentAuthenticatedUser.getId(), payload);
    }

    @PatchMapping("/me/changeProPic")
    public GenericResponseDTO changeMyProPic(@AuthenticationPrincipal User currentAuthenticatedUser,
                                             @RequestParam("image") MultipartFile img) throws IOException {
        return us.changeProPic(currentAuthenticatedUser.getId(), img);
    }

    @PatchMapping("/me/changeDateFormat")
    public GenericResponseDTO changeDateFormat(@AuthenticationPrincipal User currentAuthenticatedUser,
                                               @Validated @RequestBody UserDateFormatChangeDTO payload,
                                               BindingResult validation) {
        if (validation.hasErrors()) throw new BadRequestException(validation.getAllErrors());
        else return us.changeDateFormat(currentAuthenticatedUser.getId(), payload);
    }

    @PatchMapping("/me/changeTimeFormat")
    public GenericResponseDTO changeTimeFormat(@AuthenticationPrincipal User currentAuthenticatedUser,
                                               @Validated @RequestBody UserTimeFormatChangeDTO payload,
                                               BindingResult validation) {
        if (validation.hasErrors()) throw new BadRequestException(validation.getAllErrors());
        else return us.changeTimeFormat(currentAuthenticatedUser.getId(), payload);
    }

    @PatchMapping("/me/removeProPic")
    public GenericResponseDTO removeMyProPic(@AuthenticationPrincipal User currentAuthenticatedUser) throws IOException {
        return us.removeProPic(currentAuthenticatedUser.getId());
    }

    @PostMapping("/me/follow/{id}")
    public GenericResponseDTO handleFollow(@AuthenticationPrincipal User currentAuthenticatedUser,
                                           @PathVariable UUID id) {
        return us.handleFollow(currentAuthenticatedUser.getId(), id);
    }

    @GetMapping("/me/following")
    public List<UserEssentialsDTO> getMyFollowingList(@AuthenticationPrincipal User currentAuthenticatedUser) {
        return us.getFollowingList(currentAuthenticatedUser.getId());
    }

    @GetMapping("/me/followedBy")
    public List<UserEssentialsDTO> getMyFollowedByList(@AuthenticationPrincipal User currentAuthenticatedUser) {
        return us.getFollowedByList(currentAuthenticatedUser.getId());
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMyAccount(@AuthenticationPrincipal User currentAuthenticatedUser) {
        us.deleteAccount(currentAuthenticatedUser.getId());
    }

    @GetMapping("/me/memberships")
    public List<GroupMembership> getMyMemberships(@AuthenticationPrincipal User currentAuthenticatedUser) {
        return us.getMemberships(currentAuthenticatedUser.getId());
    }
}
