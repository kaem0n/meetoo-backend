package kaem0n.meetoo.payloads.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserUsernameChangeDTO(@NotEmpty(message = "Username field must not be empty.")
                                    @Size(min = 3, max = 20, message = "Username field length must be between 3 and 20 characters.")
                                    String username) {
}
