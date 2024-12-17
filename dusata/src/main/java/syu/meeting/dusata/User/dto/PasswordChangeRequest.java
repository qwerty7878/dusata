package syu.meeting.dusata.User.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeRequest {
    private String currentPassword;
    private String newPassword;

    public PasswordChangeRequest() {
    }

    public PasswordChangeRequest(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }
}
