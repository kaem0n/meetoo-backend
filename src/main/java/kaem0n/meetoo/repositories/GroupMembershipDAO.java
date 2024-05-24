package kaem0n.meetoo.repositories;

import kaem0n.meetoo.entities.Group;
import kaem0n.meetoo.entities.GroupMembership;
import kaem0n.meetoo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMembershipDAO extends JpaRepository<GroupMembership, Long> {
    Optional<GroupMembership> findByUserAndGroup(User user, Group group);
    List<GroupMembership> findByGroup(Group group);
}
