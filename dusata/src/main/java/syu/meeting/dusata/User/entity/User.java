package syu.meeting.dusata.User.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import syu.meeting.dusata.User.entity.Gender;
import syu.meeting.dusata.Post.entity.Post; // Post 엔티티 위치에 맞게 import 수정

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user") // 테이블명 'user'로 지정
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "loginId", length = 20, nullable = false, unique = true)
    private String loginId;

    @Column(name = "loginPwd", length = 255, nullable = false)
    private String loginPwd;

    @Column(name = "nickname", length = 20, nullable = false, unique = true)
    private String nickname;

    // 제거된 필드: height, weight, platform

    @Column(name = "mbti", length = 5) // MBTI는 4글자이나 여유롭게 5로 지정
    private String mbti;

    @Column(name = "exercise")
    private boolean exercise; // true면 운동함

    @Column(name = "smoke")
    private boolean smoke; // true면 흡연

    @Column(name = "major", length = 50)
    private String major;

    @Column(name = "studentNumber")
    private Integer studentNumber;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "age")
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender; // 남/여 등

    @CreationTimestamp
    @Column(name = "date", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp date;

    @Column(name = "profilePicture")
    private String profilePicture;

    // 게시물 리스트 추가 (양방향 관계)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Post> posts;

    public Long getId() {
        return userId;
    }

    public void setId(Long userId) {
        this.userId = userId;
    }
}
