package syu.meeting.dusata.User.dto;

import lombok.Getter;
import lombok.Setter;
import syu.meeting.dusata.User.entity.Gender;

@Getter
@Setter
public class UserRequest {
    private String loginId;
    private String loginPwd;
    private String nickname;
    private String mbti;
    private boolean exercise;
    private boolean smoke;
    private String major;
    private Integer studentNumber;
    private String name;
    private Integer age;
    private Gender gender;

    public UserRequest() {
    }

    // 로그인 시 사용
    public UserRequest(String loginId, String loginPwd) {
        this.loginId = loginId;
        this.loginPwd = loginPwd;
    }

    // 전체 정보를 받는 생성자
    public UserRequest(String loginId, String loginPwd, String nickname, String mbti, boolean exercise, boolean smoke, String major, Integer studentNumber, String name, Integer age, Gender gender) {
        this.loginId = loginId;
        this.loginPwd = loginPwd;
        this.nickname = nickname;
        this.mbti = mbti;
        this.exercise = exercise;
        this.smoke = smoke;
        this.major = major;
        this.studentNumber = studentNumber;
        this.name = name;
        this.age = age;
        this.gender = gender;
    }
}
