package kaem0n.meetoo.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import kaem0n.meetoo.entities.Comment;
import kaem0n.meetoo.exceptions.NotFoundException;
import kaem0n.meetoo.payloads.GenericResponseDTO;
import kaem0n.meetoo.payloads.comment.CommentContentEditDTO;
import kaem0n.meetoo.payloads.comment.CommentCreationDTO;
import kaem0n.meetoo.repositories.CommentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class CommentService {
    @Autowired
    private CommentDAO cd;
    @Autowired
    private UserService us;
    @Autowired
    private PostService ps;
    @Autowired
    private Cloudinary c;

    public Comment createComment(CommentCreationDTO payload, MultipartFile img) throws IOException {
        if (img == null) return cd.save(new Comment(payload.content(), us.findById(UUID.fromString(payload.userID())), ps.findById(UUID.fromString(payload.postID()))));
        else {
            String url = (String) c.uploader().upload(img.getBytes(), ObjectUtils.emptyMap()).get("url");
            return cd.save(new Comment(payload.content(), url, us.findById(UUID.fromString(payload.userID())), ps.findById(UUID.fromString(payload.postID()))));
        }
    }

    public Comment findById(UUID id) {
        return cd.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Comment editCommentContent(UUID id, CommentContentEditDTO payload) {
        Comment found = this.findById(id);

        found.setContent(payload.content());

        return cd.save(found);
    }

    public Comment editCommentImage(UUID id, MultipartFile img) throws IOException {
        Comment found = this.findById(id);

        String url = (String) c.uploader().upload(img.getBytes(), ObjectUtils.emptyMap()).get("url");
        found.setImageUrl(url);

        return cd.save(found);
    }

    public GenericResponseDTO deleteComment(UUID id) {
        Comment found = this.findById(id);

        cd.delete(found);

        return new GenericResponseDTO("Comment ID '" + id + "' deleted successfully.");
    }
}
