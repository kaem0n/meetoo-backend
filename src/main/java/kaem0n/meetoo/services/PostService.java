package kaem0n.meetoo.services;

import kaem0n.meetoo.entities.Board;
import kaem0n.meetoo.entities.Post;
import kaem0n.meetoo.exceptions.NotFoundException;
import kaem0n.meetoo.payloads.GenericResponseDTO;
import kaem0n.meetoo.payloads.post.PostContentEditDTO;
import kaem0n.meetoo.payloads.post.PostMediaEditDTO;
import kaem0n.meetoo.payloads.post.PostWithMediaCreationDTO;
import kaem0n.meetoo.payloads.post.SimplePostCreationDTO;
import kaem0n.meetoo.repositories.BoardDAO;
import kaem0n.meetoo.repositories.PostDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PostService {
    @Autowired
    private PostDAO pd;
    @Autowired
    private BoardDAO bd;
    @Autowired
    private UserService us;

    public Board findBoard(UUID id) {
        return bd.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Post createSimplePost(SimplePostCreationDTO payload) {
        return pd.save(new Post(payload.content(), us.findById(UUID.fromString(payload.userID())), this.findBoard(UUID.fromString(payload.boardID()))));
    }

    public Post createPostWithMedia(PostWithMediaCreationDTO payload) {
        return pd.save(new Post(payload.content(), payload.mediaUrls(), us.findById(UUID.fromString(payload.userID())), this.findBoard(UUID.fromString(payload.boardID()))));
    }

    public Post findById(UUID id) {
        return pd.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Post editPostContent(UUID id, PostContentEditDTO payload) {
        Post found = this.findById(id);

        found.setContent(payload.content());

        return pd.save(found);
    }

    public Post editPostMedia(UUID id, PostMediaEditDTO payload) {
        Post found = this.findById(id);

        found.setMediaUrls(payload.mediaUrls());

        return pd.save(found);
    }

    public GenericResponseDTO deletePost(UUID id) {
        Post found = this.findById(id);

        pd.delete(found);

        return new GenericResponseDTO("Post ID '" + id + "' successfully deleted.");
    }
}
