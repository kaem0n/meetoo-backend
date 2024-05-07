package kaem0n.meetoo.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import kaem0n.meetoo.entities.Board;
import kaem0n.meetoo.entities.Post;
import kaem0n.meetoo.entities.User;
import kaem0n.meetoo.exceptions.NotFoundException;
import kaem0n.meetoo.payloads.GenericResponseDTO;
import kaem0n.meetoo.payloads.post.PostContentEditDTO;
import kaem0n.meetoo.payloads.post.PostCreationDTO;
import kaem0n.meetoo.repositories.BoardDAO;
import kaem0n.meetoo.repositories.PostDAO;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Post editPostContent(UUID id, PostContentEditDTO payload) {
        Post found = this.findById(id);

        found.setContent(payload.content());

        return pd.save(found);
    }

    public Post addMedia(UUID id, List<MultipartFile> files) throws IOException {
        Post found = this.findById(id);

        List<String> mediaUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            String url = (String) c.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
            mediaUrls.add(url);
        }

        if (found.getMediaUrls() != null) found.getMediaUrls().addAll(mediaUrls);
        else found.setMediaUrls(mediaUrls);

        return pd.save(found);
    }

    public GenericResponseDTO deletePost(UUID id) {
        Post found = this.findById(id);

        pd.delete(found);

        return new GenericResponseDTO("Post ID '" + id + "' successfully deleted.");
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
