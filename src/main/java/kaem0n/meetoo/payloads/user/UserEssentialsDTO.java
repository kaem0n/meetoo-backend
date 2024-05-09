package kaem0n.meetoo.payloads.user;

import java.util.UUID;

public record UserEssentialsDTO(UUID userID, String username, String proPicUrl) {
}
