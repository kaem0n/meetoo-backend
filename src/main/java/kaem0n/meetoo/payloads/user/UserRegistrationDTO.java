package kaem0n.meetoo.payloads.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserRegistrationDTO(@NotEmpty(message = "Email field must not be empty.")
                                  @Email(message = "Invalid email format.")
                                  String email,
                                  @NotEmpty(message = "Username field must not be empty.")
                                  @Size(min = 3, max = 20, message = "Username field length must be between 3 and 20 characters.")
                                  String username,
                                  @NotEmpty(message = "Password field must not be empty.")
                                  @Size(min = 8, message = "Password must be at least 8 characters long.")
                                  String password) {
}
