package syu.meeting.dusata.User.dto;

import lombok.Getter;
import lombok.Setter;
import syu.meeting.dusata.User.entity.Gender;

@Getter
@Setter
public class UpdateRequest {
    private String nickname;
    private String mbti;
    private boolean exercise;
    private boolean smoke;
    private String major;
    private Integer studentNumber;
    private String name;
    private Integer age;
    private Gender gender;

    public UpdateRequest() {
    }

    public UpdateRequest(String nickname, String mbti, boolean exercise, boolean smoke, String major, Integer studentNumber, String name, Integer age, Gender gender) {
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
