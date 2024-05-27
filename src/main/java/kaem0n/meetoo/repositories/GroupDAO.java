package kaem0n.meetoo.repositories;

import kaem0n.meetoo.entities.Board;
import kaem0n.meetoo.entities.Group;
import kaem0n.meetoo.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupDAO extends JpaRepository<Group, UUID> {
    List<Group> findByNameContainingIgnoreCase(String name);
    Page<Group> findByFounder(User founder, Pageable pageable);
    Optional<Group> findByBoard(Board board);
}
