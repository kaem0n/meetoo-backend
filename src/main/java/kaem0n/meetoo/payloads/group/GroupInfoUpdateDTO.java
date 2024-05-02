package kaem0n.meetoo.payloads.group;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record GroupInfoUpdateDTO(@NotEmpty(message = "Name field cannot be empty.")
                                 @Size(min = 3, max = 30, message = "Name length must be between 3 and 30 characters.")
                                 String name,
                                 String description) {
}
