package kaem0n.meetoo.payloads.comment;

public record CommentCreationDTO(String content,
                                 String userID,
                                 String postID) {
}
