package kaem0n.meetoo.payloads.user;

import jakarta.validation.constraints.Pattern;

public record UserTimeFormatChangeDTO(@Pattern(regexp = "H24|H12", message = "Invalid format value (available: H24, H12).")
                                      String timeFormat) {
}
