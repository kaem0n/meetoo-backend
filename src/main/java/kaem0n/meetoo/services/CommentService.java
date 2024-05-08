package kaem0n.meetoo.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import kaem0n.meetoo.entities.Comment;
import kaem0n.meetoo.entities.User;
import kaem0n.meetoo.exceptions.NotFoundException;
import kaem0n.meetoo.payloads.GenericResponseDTO;
import kaem0n.meetoo.payloads.comment.CommentContentEditDTO;
import kaem0n.meetoo.payloads.comment.CommentCreationDTO;
import kaem0n.meetoo.repositories.CommentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
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

    public Comment createComment(UUID userID, CommentCreationDTO payload) {
        return cd.save(new Comment(payload.content(), us.findById(userID), ps.findById(UUID.fromString(payload.postID()))));
    }

    public Comment findById(UUID id) {
        return cd.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Comment editCommentContent(UUID id, CommentContentEditDTO payload) {
        Comment found = this.findById(id);

        found.setContent(payload.content());

        return cd.save(found);
    }

    public Comment addImage(UUID id, MultipartFile img) throws IOException {
        Comment found = this.findById(id);

        String url = (String) c.uploader().upload(img.getBytes(), ObjectUtils.emptyMap()).get("url");
        found.setImageUrl(url);

        return cd.save(found);
    }

    public Comment removeImage(UUID commentID) throws IOException {
        Comment found = this.findById(commentID);
        String url = found.getImageUrl();

        String imageID = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
        c.uploader().destroy(imageID, ObjectUtils.emptyMap());
        found.setImageUrl(null);

        return cd.save(found);
    }

    public void deleteComment(UUID id) {
        cd.delete(this.findById(id));
    }

    public List<Comment> findBySearchQuery(String query) {
        if (query.startsWith("#")) return cd.findByHashtag(query.replace("#", ""));
        else return cd.findByHashtag(query);
    }

    public GenericResponseDTO likeAComment(UUID userID, UUID commentID) {
        User user = us.findById(userID);
        Comment comment = this.findById(commentID);
        List<User> likes = comment.getCommentLikes();
        boolean likeCheck = false;

        for(User userLike : likes) {
            if (Objects.equals(userLike.getId().toString(), user.getId().toString())) {
                likeCheck = true;
                break;
            }
        }

        if (likeCheck) {
            likes.remove(user);
            cd.save(comment);
            return new GenericResponseDTO("Like removed.");
        }
        else {
            likes.add(user);
            cd.save(comment);
            return new GenericResponseDTO("Like added.");
        }
    }
}
