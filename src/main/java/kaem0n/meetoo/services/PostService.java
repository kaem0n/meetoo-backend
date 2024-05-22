package kaem0n.meetoo.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import kaem0n.meetoo.entities.Board;
import kaem0n.meetoo.entities.Comment;
import kaem0n.meetoo.entities.Post;
import kaem0n.meetoo.entities.User;
import kaem0n.meetoo.exceptions.NotFoundException;
import kaem0n.meetoo.payloads.GenericResponseDTO;
import kaem0n.meetoo.payloads.post.PostContentEditDTO;
import kaem0n.meetoo.payloads.post.PostCreationDTO;
import kaem0n.meetoo.repositories.BoardDAO;
import kaem0n.meetoo.repositories.CommentDAO;
import kaem0n.meetoo.repositories.PostDAO;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class PostService {
    @Autowired
    private PostDAO pd;
    @Autowired
    private CommentDAO cd;
    @Autowired
    private BoardDAO bd;
    @Autowired
    private UserService us;
    @Autowired
    private Cloudinary c;

    private Board findBoard(UUID id) {
        return bd.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Post createPost(UUID userID, PostCreationDTO payload) {
        return pd.save(new Post(payload.content(), us.findById(userID), this.findBoard(UUID.fromString(payload.boardID()))));
    }

    public Post findById(UUID id) {
        return pd.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Page<Post> findByBoard(UUID boardID, int page, int size, String sort, String sortDirection) {
        Board board = this.findBoard(boardID);

        if (size > 50) size = 50;
        Pageable p = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(sortDirection), sort));

        return pd.findByBoard(board, p);
    }

    public Post editPostContent(UUID id, PostContentEditDTO payload) {
        Post found = this.findById(id);

        found.setContent(payload.content());

        return pd.save(found);
    }

    public Post addMedia(UUID id, List<MultipartFile> files) throws IOException {
        Post found = this.findById(id);

        List<String> mediaUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            String url = (String) c.uploader().uploadLarge(file.getBytes(), ObjectUtils.emptyMap()).get("url");
            mediaUrls.add(url);
        }

        if (found.getMediaUrls() != null) found.getMediaUrls().addAll(mediaUrls);
        else found.setMediaUrls(mediaUrls);

        return pd.save(found);
    }

    public Post removeMedia(UUID postID, String mediaID) throws IOException {
        Post found = this.findById(postID);
        List<String> urls = found.getMediaUrls();
        String toDelete = "";

        for (String url : urls) {
            if (url.contains(mediaID)) toDelete = url;
        }

        if (toDelete.isEmpty()) throw new NotFoundException("File not found.");
        else {
            c.uploader().destroy(mediaID, ObjectUtils.emptyMap());
            urls.remove(toDelete);
            return pd.save(found);
        }
    }

    public void deletePost(UUID id) {
        Post post = this.findById(id);
        List<Comment> comments = post.getComments();

        for (Comment comment : comments) cd.delete(comment);
        pd.delete(post);
    }

    public List<Post> findBySearchQuery(String query) {
        if (query.startsWith("#")) return pd.findByHashtag(query.replace("#", ""));
        else return pd.findByHashtag(query);
    }

    public GenericResponseDTO likeAPost(UUID userID, UUID postID) {
        User user = us.findById(userID);
        Post post = this.findById(postID);
        List<User> likes = post.getPostLikes();
        boolean likeCheck = false;

        for(User userLike : likes) {
            if (Objects.equals(userLike.getId().toString(), user.getId().toString())) {
                likeCheck = true;
                break;
            }
        }

        if (likeCheck) {
            likes.remove(user);
            pd.save(post);
            return new GenericResponseDTO("Like removed.");
        }
        else {
            likes.add(user);
            pd.save(post);
            return new GenericResponseDTO("Like added.");
        }
    }
}
