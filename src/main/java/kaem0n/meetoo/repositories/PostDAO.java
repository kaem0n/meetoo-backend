package kaem0n.meetoo.repositories;

import kaem0n.meetoo.entities.Board;
import kaem0n.meetoo.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostDAO extends JpaRepository<Post, UUID> {
    @Query("SELECT p FROM Post p WHERE UPPER(p.content) LIKE '%#' || UPPER(:hashtag) || '%'")
    List<Post> findByHashtag(String hashtag);

    Page<Post> findByBoard(Board board, Pageable pageable);
}
