package kaem0n.meetoo.repositories;

import kaem0n.meetoo.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostDAO extends JpaRepository<Post, UUID> {
}
