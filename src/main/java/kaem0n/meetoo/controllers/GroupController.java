package kaem0n.meetoo.controllers;

import kaem0n.meetoo.entities.Group;
import kaem0n.meetoo.payloads.GenericResponseDTO;
import kaem0n.meetoo.payloads.group.GroupCreationDTO;
import kaem0n.meetoo.payloads.group.GroupInfoUpdateDTO;
import kaem0n.meetoo.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/groups")
public class GroupController {
    @Autowired
    private GroupService gs;

    @GetMapping("/{id}")
    public Group findById(@PathVariable UUID id) {
        return gs.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Group createGroup(GroupCreationDTO payload) {
        return gs.createGroup(payload);
    }

    @PutMapping("/{id}")
    public Group updateGroup(@PathVariable UUID id, @RequestBody GroupInfoUpdateDTO payload) {
        return gs.updateGroup(id, payload);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public GenericResponseDTO deleteGroup(@PathVariable UUID id) {
        return gs.deleteGroup(id);
    }

    @PatchMapping("/{groupID}/promote")
    public GenericResponseDTO handlePromotion(@RequestParam UUID founderID, @RequestParam UUID userID, @PathVariable UUID groupID) {
        return gs.handlePromotion(founderID, userID, groupID);
    }

    @PatchMapping("/{groupID}/ban")
    public GenericResponseDTO handleBan(@RequestParam UUID adminID, @RequestParam UUID userID, @PathVariable UUID groupID) {
        return gs.handleBan(adminID, userID, groupID);
    }

    @PatchMapping("/{groupID}/join")
    public GenericResponseDTO joinGroup(@RequestParam UUID userID, @PathVariable UUID groupID) {
        return gs.joinGroup(userID, groupID);
    }

    @PatchMapping("/{groupID}/leave")
    public GenericResponseDTO leaveGroup(@RequestParam UUID userID, @PathVariable UUID groupID) {
        return gs.leaveGroup(userID, groupID);
    }

    @PatchMapping("/{groupID}/follow")
    public GenericResponseDTO handleFollow(@RequestParam UUID userID, @PathVariable UUID groupID) {
        return gs.handleFollow(userID, groupID);
    }
}
