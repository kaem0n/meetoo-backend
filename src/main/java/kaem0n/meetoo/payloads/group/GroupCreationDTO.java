package kaem0n.meetoo.payloads.group;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UUID;

public record GroupCreationDTO(@NotEmpty(message = "Name field cannot be empty.")
                               @Size(min = 3, max = 30, message = "Name length must be between 3 and 30 characters.")
                               String name,
                               String description,
                               @NotEmpty(message = "founderID field is mandatory.")
                               @UUID(message = "Invalid UUID format.")
                               String founderID) {
}
