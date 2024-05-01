package kaem0n.meetoo.controllers;

import kaem0n.meetoo.entities.Comment;
import kaem0n.meetoo.payloads.GenericResponseDTO;
import kaem0n.meetoo.payloads.comment.CommentContentEditDTO;
import kaem0n.meetoo.payloads.comment.CommentCreationDTO;
import kaem0n.meetoo.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentService cs;

    @PostMapping("/image")
    @ResponseStatus(HttpStatus.CREATED)
    public Comment createComment(@RequestBody CommentCreationDTO payload, @RequestParam(value = "image", required = false) MultipartFile img) throws IOException {
        return cs.createComment(payload, img);
    }

    @GetMapping("/{id}")
    public Comment findById(@PathVariable UUID id) {
        return cs.findById(id);
    }

    @PatchMapping("/{id}")
    public Comment editCommentContent(@PathVariable UUID id, @RequestBody CommentContentEditDTO payload) {
        return cs.editCommentContent(id, payload);
    }

    @PatchMapping("/{id}/image")
    public Comment editCommentImage(@PathVariable UUID id, @RequestParam("image") MultipartFile img) throws IOException {
        return cs.editCommentImage(id, img);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public GenericResponseDTO deleteComment(@PathVariable UUID id) {
        return cs.deleteComment(id);
    }
}
