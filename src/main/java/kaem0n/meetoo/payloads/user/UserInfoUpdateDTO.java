package kaem0n.meetoo.payloads.user;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.util.List;

public record UserInfoUpdateDTO(@Size(max = 50, message = "Name field length must not exceed 50 characters.")
                                String name,
                                @Size(max = 50, message = "Surname field length must not exceed 50 characters.")
                                String surname,
                                @Pattern(regexp = "^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$",
                                        message = "Birthday field must follow YYYY-MM-DD format.")
                                String birthday,
                                @URL(message = "Invalid URL.")
                                String proPicUrl,
                                @Pattern(regexp = "MALE|FEMALE|UNDEFINED", message = "Invalid gender value (available: MALE, FEMALE, UNDEFINED).")
                                String gender,
                                @Size(max = 50, message = "Occupation field length must not exceed 50 characters.")
                                String occupation,
                                String bio,
                                List<@Size(min = 3, max = 50, message = "Hobby field length must be between 3 and 50 characters.") String> hobbies) {
}
