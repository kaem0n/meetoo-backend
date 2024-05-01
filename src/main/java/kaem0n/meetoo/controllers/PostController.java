package kaem0n.meetoo.controllers;

import kaem0n.meetoo.entities.Post;
import kaem0n.meetoo.payloads.GenericResponseDTO;
import kaem0n.meetoo.payloads.post.PostContentEditDTO;
import kaem0n.meetoo.payloads.post.PostCreationDTO;
import kaem0n.meetoo.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService ps;

    @GetMapping("/{id}")
    public Post findById(@PathVariable UUID id) {
        return ps.findById(id);
    }

    @PostMapping("/media")
    @ResponseStatus(HttpStatus.CREATED)
    public Post createPost(@RequestBody PostCreationDTO payload, @RequestParam(value = "media", required = false) List<MultipartFile> files) throws IOException {
        return ps.createPost(payload, files);
    }

    @PatchMapping("/{id}")
    public Post editPostContent(@PathVariable UUID id, @RequestBody PostContentEditDTO payload) {
        return ps.editPostContent(id, payload);
    }

    @PatchMapping("/{id}/media")
    public Post editPostMedia(@PathVariable UUID id, @RequestParam("media") List<MultipartFile> files) throws IOException {
        return ps.editPostMedia(id, files);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public GenericResponseDTO deletePost(@PathVariable UUID id) {
        return ps.deletePost(id);
    }
}
