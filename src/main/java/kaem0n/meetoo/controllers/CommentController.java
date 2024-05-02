package kaem0n.meetoo.controllers;

import kaem0n.meetoo.entities.Comment;
import kaem0n.meetoo.entities.User;
import kaem0n.meetoo.enums.UserPermissions;
import kaem0n.meetoo.exceptions.BadRequestException;
import kaem0n.meetoo.exceptions.UnauthorizedException;
import kaem0n.meetoo.payloads.GenericResponseDTO;
import kaem0n.meetoo.payloads.comment.CommentContentEditDTO;
import kaem0n.meetoo.payloads.comment.CommentCreationDTO;
import kaem0n.meetoo.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public Comment createComment(@Validated @RequestBody CommentCreationDTO payload,
                                 @RequestParam(value = "image", required = false) MultipartFile img,
                                 BindingResult validation) throws IOException {
        if (validation.hasErrors()) throw new BadRequestException(validation.getAllErrors());
        else return cs.createComment(payload, img);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public Comment findById(@PathVariable UUID id) {
        return cs.findById(id);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public Comment editCommentContent(@PathVariable UUID id,
                                      @Validated @RequestBody CommentContentEditDTO payload,
                                      BindingResult validation,
                                      @AuthenticationPrincipal User currentAuthenticatedUser) {
        Comment comment = cs.findById(id);
        if (validation.hasErrors()) throw new BadRequestException(validation.getAllErrors());
        else if (currentAuthenticatedUser.getPermissions() == UserPermissions.ADMIN || currentAuthenticatedUser == comment.getUser()) {
            return cs.editCommentContent(id, payload);
        } else throw new UnauthorizedException("Invalid request: not authorized.");
    }

    @PatchMapping("/{id}/image")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public Comment editCommentImage(@PathVariable UUID id,
                                    @RequestParam("image") MultipartFile img,
                                    @AuthenticationPrincipal User currentAuthenticatedUser) throws IOException {
        Comment comment = cs.findById(id);
        if (currentAuthenticatedUser.getPermissions() == UserPermissions.ADMIN || currentAuthenticatedUser == comment.getUser()) {
            return cs.editCommentImage(id, img);
        } else throw new UnauthorizedException("Invalid request: not authorized.");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public GenericResponseDTO deleteComment(@PathVariable UUID id, @AuthenticationPrincipal User currentAuthenticatedUser) {
        Comment comment = cs.findById(id);
        if (currentAuthenticatedUser.getPermissions() == UserPermissions.ADMIN || currentAuthenticatedUser == comment.getUser()) {
            return cs.deleteComment(id);
        } else throw new UnauthorizedException("Invalid request: not authorized.");
    }
}
