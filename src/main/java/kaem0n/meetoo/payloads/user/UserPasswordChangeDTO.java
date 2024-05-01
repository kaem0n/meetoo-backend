package kaem0n.meetoo.payloads.user;

public record UserPasswordChangeDTO(String oldPassword,
                                    String newPassword) {
}
