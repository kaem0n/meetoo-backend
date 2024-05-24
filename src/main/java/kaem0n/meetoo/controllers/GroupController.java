package kaem0n.meetoo.controllers;

import kaem0n.meetoo.entities.Group;
import kaem0n.meetoo.entities.GroupMembership;
import kaem0n.meetoo.entities.User;
import kaem0n.meetoo.enums.UserPermissions;
import kaem0n.meetoo.exceptions.BadRequestException;
import kaem0n.meetoo.exceptions.UnauthorizedException;
import kaem0n.meetoo.payloads.GenericResponseDTO;
import kaem0n.meetoo.payloads.group.GroupDTO;
import kaem0n.meetoo.services.GroupService;
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
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/groups")
public class GroupController {
    @Autowired
    private GroupService gs;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public Group findById(@PathVariable UUID id) {
        return gs.findById(id);
    }

    @GetMapping("/byFounder/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public Page<Group> findByFounder(@PathVariable UUID id,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(defaultValue = "id") String sort) {
        return gs.findByFounder(id, page, size, sort);
    }

    @GetMapping("/byFounder/me")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public Page<Group> findMyFoundedGroups(@AuthenticationPrincipal User currentAuthenticatedUser,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(defaultValue = "id") String sort) {
        return gs.findByFounder(currentAuthenticatedUser.getId(), page, size, sort);
    }

    @GetMapping("/{id}/memberships")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public List<GroupMembership> findGroupMemberships(@PathVariable UUID id) {
        return gs.findGroupMemberships(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public Group createGroup(@AuthenticationPrincipal User currentAuthenticatedUser,
                             @Validated @RequestBody GroupDTO payload,
                             BindingResult validation) {
        if (validation.hasErrors()) throw new BadRequestException(validation.getAllErrors());
        else return gs.createGroup(currentAuthenticatedUser.getId(), payload);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public Group updateGroup(@PathVariable UUID id,
                             @Validated @RequestBody GroupDTO payload,
                             BindingResult validation,
                             @AuthenticationPrincipal User currentAuthenticatedUser) {
        Group group = gs.findById(id);
        if (validation.hasErrors()) throw new BadRequestException(validation.getAllErrors());
        else if (currentAuthenticatedUser.getPermissions() == UserPermissions.ADMIN ||
                Objects.equals(currentAuthenticatedUser.getId().toString(), group.getFounder().getId().toString()) ||
                gs.findUserMembership(currentAuthenticatedUser.getId(), id).isAdmin()) {
            return gs.updateGroup(id, payload);
        } else throw new UnauthorizedException("Invalid request: not authorized.");
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public void deleteGroup(@PathVariable UUID id, @AuthenticationPrincipal User currentAuthenticatedUser) {
        Group group = gs.findById(id);
        if (currentAuthenticatedUser.getPermissions() == UserPermissions.ADMIN ||
                Objects.equals(currentAuthenticatedUser.getId().toString(), group.getFounder().getId().toString())) {
            gs.deleteGroup(id);
        } else throw new UnauthorizedException("Invalid request: not authorized.");
    }

    @PatchMapping("/{groupID}/promote")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public GenericResponseDTO handlePromotion(@AuthenticationPrincipal User currentAuthenticatedUser,
                                              @RequestParam UUID userID,
                                              @PathVariable UUID groupID) {
        Group group = gs.findById(groupID);
        if (currentAuthenticatedUser.getPermissions() == UserPermissions.ADMIN ||
                Objects.equals(currentAuthenticatedUser.getId().toString(), group.getFounder().getId().toString())) {
            return gs.handlePromotion(userID, groupID);
        } else throw new UnauthorizedException("Invalid request: not authorized.");
    }

    @PatchMapping("/{groupID}/ban")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public GenericResponseDTO handleBan(@AuthenticationPrincipal User currentAuthenticatedUser,
                                        @RequestParam UUID userID,
                                        @PathVariable UUID groupID) {
        Group group = gs.findById(groupID);
        if (currentAuthenticatedUser.getPermissions() == UserPermissions.ADMIN ||
                Objects.equals(currentAuthenticatedUser.getId().toString(), group.getFounder().getId().toString()) ||
                gs.findUserMembership(currentAuthenticatedUser.getId(), groupID).isAdmin()) {
            return gs.handleBan(userID, groupID);
        } else throw new UnauthorizedException("Invalid request: not authorized.");
    }

    @PatchMapping("/{id}/changeCover")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public GenericResponseDTO changeCover(@AuthenticationPrincipal User currentAuthenticatedUser,
                                          @PathVariable UUID id,
                                          @RequestParam("image")MultipartFile img) throws IOException {
        Group group = gs.findById(id);
        if (currentAuthenticatedUser.getPermissions() == UserPermissions.ADMIN ||
                Objects.equals(currentAuthenticatedUser.getId().toString(), group.getFounder().getId().toString()) ||
                gs.findUserMembership(currentAuthenticatedUser.getId(), id).isAdmin()) {
            return gs.changeCover(id, img);
        } else throw new UnauthorizedException("Invalid request: not authorized.");
    }

    @PatchMapping("/{id}/removeCover")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public GenericResponseDTO removeCover(@AuthenticationPrincipal User currentAuthenticatedUser,
                                          @PathVariable UUID id) throws IOException {
        Group group = gs.findById(id);
        if (currentAuthenticatedUser.getPermissions() == UserPermissions.ADMIN ||
                Objects.equals(currentAuthenticatedUser.getId().toString(), group.getFounder().getId().toString()) ||
                gs.findUserMembership(currentAuthenticatedUser.getId(), id).isAdmin()) {
            return gs.removeCover(id);
        } else throw new UnauthorizedException("Invalid request: not authorized.");
    }

    @PostMapping("/{groupID}/join")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public GenericResponseDTO joinGroup(@AuthenticationPrincipal User currentAuthenticatedUser, @PathVariable UUID groupID) {
        return gs.joinGroup(currentAuthenticatedUser.getId(), groupID);
    }

    @PostMapping("/{groupID}/leave")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public GenericResponseDTO leaveGroup(@AuthenticationPrincipal User currentAuthenticatedUser, @PathVariable UUID groupID) {
        return gs.leaveGroup(currentAuthenticatedUser.getId(), groupID);
    }

    @PostMapping("/{groupID}/follow")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public GenericResponseDTO handleFollow(@AuthenticationPrincipal User currentAuthenticatedUser, @PathVariable UUID groupID) {
        return gs.handleFollow(currentAuthenticatedUser.getId(), groupID);
    }
}
