package kaem0n.meetoo.repositories;

import kaem0n.meetoo.entities.User;
import kaem0n.meetoo.entities.UserFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserFollowDAO extends JpaRepository<UserFollow, UUID> {
    Optional<UserFollow> findByFollowerAndFollowed(User follower, User followed);
}
