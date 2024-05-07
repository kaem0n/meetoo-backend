package kaem0n.meetoo.controllers;

import kaem0n.meetoo.entities.Post;
import kaem0n.meetoo.entities.User;
import kaem0n.meetoo.enums.UserPermissions;
import kaem0n.meetoo.exceptions.BadRequestException;
import kaem0n.meetoo.exceptions.UnauthorizedException;
import kaem0n.meetoo.payloads.GenericResponseDTO;
import kaem0n.meetoo.payloads.post.PostContentEditDTO;
import kaem0n.meetoo.payloads.post.PostCreationDTO;
import kaem0n.meetoo.services.PostService;
import kaem0n.meetoo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService ps;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public Post findById(@PathVariable UUID id) {
        return ps.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Post createPost(@Validated @RequestBody PostCreationDTO payload,
                           BindingResult validation,
                           @AuthenticationPrincipal User currentAuthenticatedUserUser) {
        if (validation.hasErrors()) throw new BadRequestException(validation.getAllErrors());
        else return ps.createPost(currentAuthenticatedUserUser.getId(), payload);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public Post editPostContent(@PathVariable UUID id,
                                @Validated @RequestBody PostContentEditDTO payload,
                                BindingResult validation,
                                @AuthenticationPrincipal User currentAuthenticatedUser) {
        Post post = ps.findById(id);
        if (validation.hasErrors()) throw new BadRequestException(validation.getAllErrors());
        else if (Objects.equals(currentAuthenticatedUser.getId().toString(), post.getUser().getId().toString())
                || currentAuthenticatedUser.getPermissions() == UserPermissions.ADMIN) {
            return ps.editPostContent(id, payload);
        } else throw new UnauthorizedException("Invalid request: not authorized.");
    }

    @PatchMapping("/{id}/media")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public Post addMedia(@PathVariable UUID id,
                         @RequestParam("media") List<MultipartFile> files,
                         @AuthenticationPrincipal User currentAuthenticatedUser) throws IOException {
        Post post = ps.findById(id);
        if (Objects.equals(currentAuthenticatedUser.getId().toString(), post.getUser().getId().toString())
                || currentAuthenticatedUser.getPermissions() == UserPermissions.ADMIN) {
            return ps.addMedia(id, files);
        } else throw new UnauthorizedException("Invalid request: not authorized.");
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public void deletePost(@PathVariable UUID id, @AuthenticationPrincipal User currentAuthenticatedUser) {
        Post post = ps.findById(id);
        if (Objects.equals(currentAuthenticatedUser.getId().toString(), post.getUser().getId().toString())
                || currentAuthenticatedUser.getPermissions() == UserPermissions.ADMIN) {
            ps.deletePost(id);
        } else throw new UnauthorizedException("Invalid request: not authorized.");
    }

    @PostMapping("/{id}/like")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public GenericResponseDTO handleLike(@PathVariable UUID id, @AuthenticationPrincipal User currentAuthenticatedUser) {
        return ps.likeAPost(currentAuthenticatedUser.getId(), id);
    }

}
