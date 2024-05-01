package kaem0n.meetoo.payloads.user;

import java.util.List;

public record UserInfoUpdateDTO(String name,
                                String surname,
                                String birthday,
                                String proPicUrl,
                                String gender,
                                String occupation,
                                String bio,
                                List<String> hobbies) {
}
