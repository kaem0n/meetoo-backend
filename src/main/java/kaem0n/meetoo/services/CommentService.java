package kaem0n.meetoo.services;

import kaem0n.meetoo.entities.Comment;
import kaem0n.meetoo.exceptions.NotFoundException;
import kaem0n.meetoo.payloads.GenericResponseDTO;
import kaem0n.meetoo.payloads.comment.CommentContentEditDTO;
import kaem0n.meetoo.payloads.comment.CommentImageEditDTO;
import kaem0n.meetoo.payloads.comment.CommentWithImageCreationDTO;
import kaem0n.meetoo.payloads.comment.SimpleCommentCreationDTO;
import kaem0n.meetoo.repositories.CommentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CommentService {
    @Autowired
    private CommentDAO cd;
    @Autowired
    private UserService us;
    @Autowired
    private PostService ps;

    public Comment createSimpleComment(SimpleCommentCreationDTO payload) {
        return cd.save(new Comment(payload.content(), us.findById(UUID.fromString(payload.userID())), ps.findById(UUID.fromString(payload.postID()))));
    }

    public Comment createCommentWithImage(CommentWithImageCreationDTO payload) {
        return cd.save(new Comment(payload.content(), payload.imageUrl(), us.findById(UUID.fromString(payload.userID())), ps.findById(UUID.fromString(payload.postID()))));
    }

    public Comment findById(UUID id) {
        return cd.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Comment editCommentContent(UUID id, CommentContentEditDTO payload) {
        Comment found = this.findById(id);

        found.setContent(payload.content());

        return cd.save(found);
    }

    public Comment editCommentImage(UUID id, CommentImageEditDTO payload) {
        Comment found = this.findById(id);

        found.setImageUrl(payload.imageUrl());

        return cd.save(found);
    }

    public GenericResponseDTO deleteComment(UUID id) {
        Comment found = this.findById(id);

        cd.delete(found);

        return new GenericResponseDTO("Comment ID '" + id + "' deleted successfully.");
    }
}
