package kaem0n.meetoo.repositories;

import kaem0n.meetoo.entities.Board;
import kaem0n.meetoo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserDAO extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
    Optional<User> findByBoard(Board board);

    @Query(value = "SELECT * FROM users " +
            "WHERE UPPER(users.username) LIKE UPPER('%' || :query || '%') " +
            "OR UPPER(users.name) LIKE UPPER('%' || :query || '%') " +
            "OR UPPER(users.surname) LIKE UPPER('%' || :query || '%')",
            nativeQuery = true)
    List<User> searchUsers(String query);
}
