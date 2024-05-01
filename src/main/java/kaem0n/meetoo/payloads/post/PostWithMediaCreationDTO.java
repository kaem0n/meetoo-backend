package kaem0n.meetoo.payloads.post;

import java.util.List;

public record PostWithMediaCreationDTO(String content,
                                       List<String> mediaUrls,
                                       String userID,
                                       String boardID) {
}
