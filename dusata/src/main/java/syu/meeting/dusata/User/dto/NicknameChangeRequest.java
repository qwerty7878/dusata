package syu.meeting.dusata.User.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NicknameChangeRequest {
    private String newNickname;

    public NicknameChangeRequest() {
    }

    public NicknameChangeRequest(String newNickname) {
        this.newNickname = newNickname;
    }
}
