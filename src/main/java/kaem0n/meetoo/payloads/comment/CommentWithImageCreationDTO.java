package kaem0n.meetoo.payloads.comment;

public record CommentWithImageCreationDTO(String content,
                                          String imageUrl,
                                          String userID,
                                          String postID) {
}
