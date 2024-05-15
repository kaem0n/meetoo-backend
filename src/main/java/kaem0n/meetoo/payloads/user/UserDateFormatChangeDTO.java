package kaem0n.meetoo.payloads.user;

import jakarta.validation.constraints.Pattern;

public record UserDateFormatChangeDTO(@Pattern(regexp = "YMD|MDY|DMY", message = "Invalid format value (available: YMD, MDY, DMY).")
                                      String dateFormat) {
}
