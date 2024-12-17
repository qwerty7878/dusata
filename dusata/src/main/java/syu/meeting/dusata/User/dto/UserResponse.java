package syu.meeting.dusata.User.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import syu.meeting.dusata.User.entity.Gender;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    private Long userId;
    private String loginId;
    private String nickname;
    private String mbti;
    private boolean exercise;
    private boolean smoke;
    private String major;
    private Integer studentNumber;
    private String name;
    private Integer age;
    private Gender gender;
    private String token;

    public UserResponse(Long userId, String loginId, String nickname, String mbti, boolean exercise, boolean smoke, String major, Integer studentNumber, String name, Integer age, Gender gender) {
        this.userId = userId;
        this.loginId = loginId;
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

    public UserResponse(Long userId, String loginId, String nickname, String mbti, boolean exercise, boolean smoke, String major, Integer studentNumber, String name, Integer age, Gender gender, String token) {
        this(userId, loginId, nickname, mbti, exercise, smoke, major, studentNumber, name, age, gender);
        this.token = token;
    }

    public UserResponse() {
    }
}
