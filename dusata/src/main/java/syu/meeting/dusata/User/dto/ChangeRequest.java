package syu.meeting.dusata.User.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeRequest {
    private String newNickname;
    private String currentPassword;
    private String newPassword;

    public ChangeRequest(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    // 필요하다면 기본 생성자나 추가 생성자도 둘 수 있음
    public ChangeRequest() {
    }

    public ChangeRequest(String newNickname, String currentPassword, String newPassword) {
        this.newNickname = newNickname;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }
}
