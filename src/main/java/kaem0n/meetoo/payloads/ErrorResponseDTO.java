package kaem0n.meetoo.payloads;

import java.time.LocalDateTime;

public record ErrorResponseDTO(String msg, LocalDateTime timestamp) {
}
