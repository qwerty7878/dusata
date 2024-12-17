package syu.meeting.dusata.User.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String loginId;
    private String loginPwd;

    public LoginRequest(String loginId, String loginPwd) {
        this.loginId = loginId;
        this.loginPwd = loginPwd;
    }

    public LoginRequest() {
    }
}
