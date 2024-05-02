package kaem0n.meetoo.payloads.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record UserEmailChangeDTO(@NotEmpty(message = "Email field must not be empty.")
                                 @Email(message = "Invalid email format.")
                                 String email) {
}
