package kaem0n.meetoo.payloads.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserPasswordChangeDTO(@NotEmpty(message = "Old password field is mandatory.")
                                    String oldPassword,
                                    @NotEmpty(message = "New password field must not be empty.")
                                    @Size(min = 8, message = "New password must be at least 8 characters long.")
                                    String newPassword) {
}
