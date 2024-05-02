package kaem0n.meetoo.payloads.post;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UUID;

public record PostCreationDTO(@NotEmpty(message = "Content field cannot be empty.")
                              @Size(min = 10, message = "Content length must be at least 10 characters long.")
                              String content,
                              @NotEmpty(message = "boardID field is mandatory.")
                              @UUID(message = "Invalid UUID format.")
                              String boardID) {
}
