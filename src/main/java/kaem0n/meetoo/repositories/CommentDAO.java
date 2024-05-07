package kaem0n.meetoo.repositories;

import kaem0n.meetoo.entities.Comment;
import kaem0n.meetoo.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentDAO extends JpaRepository<Comment, UUID> {
    Page<Comment> findByPost(Post post, Pageable p);

    @Query("SELECT c FROM Comment c WHERE UPPER(c.content) LIKE '%#' || UPPER(:hashtag) || '%'")
    List<Comment> findByHashtag(String hashtag);
}
