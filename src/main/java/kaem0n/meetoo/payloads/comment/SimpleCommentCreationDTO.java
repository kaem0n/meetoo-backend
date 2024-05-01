package kaem0n.meetoo.payloads.comment;

public record SimpleCommentCreationDTO(String content,
                                       String userID,
                                       String postID) {
}
