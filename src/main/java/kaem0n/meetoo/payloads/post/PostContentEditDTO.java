package kaem0n.meetoo.payloads.post;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record PostContentEditDTO(@NotEmpty(message = "Content field cannot be empty.")
                                 @Size(min = 10, message = "Content length must be at least 10 characters long.")
                                 String content) {
}
