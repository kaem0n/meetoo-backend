package kaem0n.meetoo.services;

import kaem0n.meetoo.entities.Board;
import kaem0n.meetoo.entities.Group;
import kaem0n.meetoo.entities.GroupMembership;
import kaem0n.meetoo.entities.User;
import kaem0n.meetoo.exceptions.NotFoundException;
import kaem0n.meetoo.payloads.GenericResponseDTO;
import kaem0n.meetoo.payloads.group.GroupCreationDTO;
import kaem0n.meetoo.payloads.group.GroupInfoUpdateDTO;
import kaem0n.meetoo.repositories.BoardDAO;
import kaem0n.meetoo.repositories.GroupDAO;
import kaem0n.meetoo.repositories.GroupMembershipDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GroupService {
    @Autowired
    private GroupDAO gd;
    @Autowired
    private UserService us;
    @Autowired
    private BoardDAO bd;
    @Autowired
    private GroupMembershipDAO gmd;

    public Group createGroup(GroupCreationDTO payload) {
        User founder = us.findById(UUID.fromString(payload.founderID()));
        Group newGroup = gd.save(new Group(payload.name(), payload.description(), founder));
        GroupMembership founderMembership = gmd.save(new GroupMembership(founder, newGroup));
        Board board = bd.save(new Board());
        newGroup.setBoard(board);

        return gd.save(newGroup);
    }

    public Group findById(UUID id) {
        return gd.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Group updateGroup(UUID id, GroupInfoUpdateDTO payload) {
        Group found = this.findById(id);

        found.setName(payload.name());
        found.setDescription(payload.description());

        return gd.save(found);
    }

    public GenericResponseDTO deleteGroup(UUID id) {
        Group found = this.findById(id);

        bd.delete(found.getBoard());
        gd.delete(found);

        return new GenericResponseDTO("Group ID '" + id + "' deleted successfully.");
    }

    public GenericResponseDTO promoteUser(UUID founderID, UUID userID, UUID groupID) {
        User founder = us.findById(founderID);
        User user = us.findById(userID);
        Group group = this.findById(groupID);
        GroupMembership userMembership = gmd.findByUserAndGroup(user, group)
                .orElseThrow(() -> new NotFoundException(user.getUsername() + " is not a member of " + group.getName() + " group."));

        if (founder == group.getFounder()) {
            userMembership.setAdmin(true);
            gmd.save(userMembership);
        }

        return new GenericResponseDTO(user.getUsername() + " has been promoted to role 'Admin'.");
    }

    public GenericResponseDTO degradeUser(UUID founderID, UUID userID, UUID groupID) {
        User founder = us.findById(founderID);
        User user = us.findById(userID);
        Group group = this.findById(groupID);
        GroupMembership userMembership = gmd.findByUserAndGroup(user, group)
                .orElseThrow(() -> new NotFoundException(user.getUsername() + " is not a member of " + group.getName() + " group."));

        if (founder == group.getFounder()) {
            userMembership.setAdmin(false);
            gmd.save(userMembership);
        }

        return new GenericResponseDTO(user.getUsername() + " has been degraded to role 'Member'.");
    }

    public GenericResponseDTO banUser(UUID adminID, UUID userID, UUID groupID) {
        User admin = us.findById(adminID);
        User user = us.findById(userID);
        Group group = this.findById(groupID);
        GroupMembership userMembership = gmd.findByUserAndGroup(user, group)
                .orElseThrow(() -> new NotFoundException(user.getUsername() + " is not a member of " + group.getName() + " group."));
        GroupMembership adminMembership = gmd.findByUserAndGroup(admin, group)
                .orElseThrow(() -> new NotFoundException(admin.getUsername() + " is not a member of " + group.getName() + " group."));

        if (adminMembership.isAdmin()) {
            userMembership.setAdmin(false);
            userMembership.setBanned(true);
            gmd.save(userMembership);
        }

        return new GenericResponseDTO(user.getUsername() + " has been banned from the group.");
    }

    public GenericResponseDTO unbanUser(UUID adminID, UUID userID, UUID groupID) {
        User admin = us.findById(adminID);
        User user = us.findById(userID);
        Group group = this.findById(groupID);
        GroupMembership userMembership = gmd.findByUserAndGroup(user, group)
                .orElseThrow(() -> new NotFoundException(user.getUsername() + " is not a member of " + group.getName() + " group."));
        GroupMembership adminMembership = gmd.findByUserAndGroup(admin, group)
                .orElseThrow(() -> new NotFoundException(admin.getUsername() + " is not a member of " + group.getName() + " group."));

        if (adminMembership.isAdmin()) {
            userMembership.setBanned(false);
            gmd.save(userMembership);
        }

        return new GenericResponseDTO(user.getUsername() + " has been unbanned from the group.");
    }

    public GenericResponseDTO joinGroup(UUID userID, UUID groupID) {
        User user = us.findById(userID);
        Group group = this.findById(groupID);
        GroupMembership userMembership = gmd.save(new GroupMembership(user, group));

        return new GenericResponseDTO("You are now a member of the group " + group.getName() + ".");
    }

    public GenericResponseDTO leaveGroup(UUID userID, UUID groupID) {
        User user = us.findById(userID);
        Group group = this.findById(groupID);
        GroupMembership userMembership = gmd.findByUserAndGroup(user, group)
                .orElseThrow(() -> new NotFoundException(user.getUsername() + " is not a member of " + group.getName() + " group."));

        if (!userMembership.isBanned()) gmd.delete(userMembership);

        return new GenericResponseDTO("You have left the group " + group.getName() + ".");
    }

    public GenericResponseDTO handleFollow(UUID userID, UUID groupID) {
        User user = us.findById(userID);
        Group group = this.findById(groupID);
        GroupMembership userMembership = gmd.findByUserAndGroup(user, group)
                .orElseThrow(() -> new NotFoundException(user.getUsername() + " is not a member of " + group.getName() + " group."));

        userMembership.setFollowing(!userMembership.isFollowing());
        gmd.save(userMembership);

        if (userMembership.isFollowing()) return new GenericResponseDTO("You are now following the group " + group.getName() + ".");
        else return new GenericResponseDTO("You are no longer following the group " + group.getName() + ".");
    }
}
