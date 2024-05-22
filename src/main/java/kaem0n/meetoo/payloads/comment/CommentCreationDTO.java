package kaem0n.meetoo.payloads.comment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UUID;

public record CommentCreationDTO(@NotEmpty(message = "Content field cannot be empty.")
                                 @Size(min = 3, message = "Content length must be at least 3 characters long.")
                                 String content,
                                 @NotEmpty(message = "postID field is mandatory.")
                                 @UUID(message = "Invalid UUID format.")
                                 String postID) {
}
