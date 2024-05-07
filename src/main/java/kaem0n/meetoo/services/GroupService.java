package kaem0n.meetoo.services;

import kaem0n.meetoo.entities.Board;
import kaem0n.meetoo.entities.Group;
import kaem0n.meetoo.entities.GroupMembership;
import kaem0n.meetoo.entities.User;
import kaem0n.meetoo.exceptions.NotFoundException;
import kaem0n.meetoo.payloads.GenericResponseDTO;
import kaem0n.meetoo.payloads.group.GroupDTO;
import kaem0n.meetoo.repositories.BoardDAO;
import kaem0n.meetoo.repositories.GroupDAO;
import kaem0n.meetoo.repositories.GroupMembershipDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public Group createGroup(UUID userID, GroupDTO payload) {
        User founder = us.findById(userID);
        Group newGroup = gd.save(new Group(payload.name(), payload.description(), founder));
        gmd.save(new GroupMembership(founder, newGroup));
        Board board = bd.save(new Board());
        newGroup.setBoard(board);

        return gd.save(newGroup);
    }

    public Group findById(UUID id) {
        return gd.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public GroupMembership findUserMembership(UUID userID, UUID groupID) {
        User user = us.findById(userID);
        Group group = this.findById(groupID);
        return gmd.findByUserAndGroup(user, group)
                .orElseThrow(() -> new NotFoundException(user.getUsername() + " is not a member of '" + group.getName() + "' group."));
    }

    public Group updateGroup(UUID id, GroupDTO payload) {
        Group found = this.findById(id);

        found.setName(payload.name());
        found.setDescription(payload.description());

        return gd.save(found);
    }

    public void deleteGroup(UUID id) {
        Group found = this.findById(id);

        bd.delete(found.getBoard());
        gd.delete(found);
    }

    public GenericResponseDTO handlePromotion(UUID userID, UUID groupID) {
        User user = us.findById(userID);
        GroupMembership userMembership = this.findUserMembership(userID, groupID);

        userMembership.setAdmin(!userMembership.isAdmin());
        gmd.save(userMembership);

        if (userMembership.isAdmin()) return new GenericResponseDTO(user.getUsername() + " has been promoted to role 'Admin'.");
        else return new GenericResponseDTO(user.getUsername() + " has been degraded to role 'Member'.");
    }

    public GenericResponseDTO handleBan(UUID userID, UUID groupID) {
        User user = us.findById(userID);
        GroupMembership userMembership = this.findUserMembership(userID, groupID);

        userMembership.setAdmin(false);
        userMembership.setBanned(!userMembership.isBanned());
        gmd.save(userMembership);

        if (userMembership.isBanned()) return new GenericResponseDTO(user.getUsername() + " has been banned from the group.");
        else return new GenericResponseDTO(user.getUsername() + " has been unbanned from the group.");
    }

    public GenericResponseDTO joinGroup(UUID userID, UUID groupID) {
        User user = us.findById(userID);
        Group group = this.findById(groupID);
        List<GroupMembership> userMemberships = user.getMemberships();
        for (GroupMembership membership : userMemberships) {
            if (membership.getGroup().getId() == groupID) return new GenericResponseDTO("You are already member of this group.");
        }
        gmd.save(new GroupMembership(user, group));

        return new GenericResponseDTO("You are now a member of the group '" + group.getName() + "'.");
    }

    public GenericResponseDTO leaveGroup(UUID userID, UUID groupID) {
        Group group = this.findById(groupID);
        GroupMembership userMembership = this.findUserMembership(userID, groupID);

        if (!userMembership.isBanned()) gmd.delete(userMembership);

        return new GenericResponseDTO("You have left the group '" + group.getName() + "'.");
    }

    public GenericResponseDTO handleFollow(UUID userID, UUID groupID) {
        Group group = this.findById(groupID);
        GroupMembership userMembership = this.findUserMembership(userID, groupID);

        userMembership.setFollowing(!userMembership.isFollowing());
        gmd.save(userMembership);

        if (userMembership.isFollowing()) return new GenericResponseDTO("You are now following the group '" + group.getName() + "'.");
        else return new GenericResponseDTO("You are no longer following the group '" + group.getName() + "'.");
    }

    public List<Group> findBySearchQuery(String query) {
        return gd.findByNameContainingIgnoreCase(query);
    }
}
