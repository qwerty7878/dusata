package syu.meeting.dusata.User.service;

import java.security.Principal;
import java.sql.Timestamp;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import syu.meeting.dusata.User.entity.Gender;
import syu.meeting.dusata.User.entity.User;
import syu.meeting.dusata.User.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // loginId를 통해 사용자 조회
    public User getUserByLoginId(String loginId) {
        System.out.println("Fetching user by loginId: " + loginId);
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid login_id: " + loginId));
    }

    // userId를 통해 사용자 조회
    public User getUserById(Long userId) {
        if (userId == null) {
            System.err.println("getUserById called with null userId");
            throw new IllegalArgumentException("User ID cannot be null");
        }

        System.out.println("Fetching user by userId: " + userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));
    }

    // 현재 로그인된 사용자 정보 가져오기
    public User getUserByPrincipal(Principal principal) {
        if (principal == null) {
            System.err.println("getUserByPrincipal called with null principal");
            throw new IllegalArgumentException("Principal cannot be null");
        }

        String loginId = principal.getName();
        System.out.println("Fetching user by principal loginId: " + loginId);
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with loginId: " + loginId));
    }

    public boolean checkLoginId(String loginId) {
        System.out.println("Checking existence of loginId: " + loginId);
        return userRepository.existsByLoginId(loginId);
    }

    public boolean checkNickname(String nickname) {
        System.out.println("Checking existence of nickname: " + nickname);
        return userRepository.existsByNickname(nickname);
    }

    // 회원가입
    public User register(String loginId, String loginPwd, String nickname,
                         String mbti, boolean exercise, boolean smoke,
                         String major, Integer studentNumber, String name, Integer age, Gender gender) {

        User user = User.builder()
                .loginId(loginId)
                .loginPwd(loginPwd)
                .nickname(nickname)
                .mbti(mbti)
                .exercise(exercise)
                .smoke(smoke)
                .major(major)
                .studentNumber(studentNumber)
                .name(name)
                .age(age)
                .gender(gender)
                .date(new Timestamp(System.currentTimeMillis()))
                .build();

        return saveUser(user);
    }

    // 프로필 정보 업데이트 (mbti, exercise, smoke, major, studentNumber, name, age, gender, nickname)
    public User updateUserProfile(Long userId, String nickname, String mbti, boolean exercise, boolean smoke,
                                  String major, Integer studentNumber, String name, Integer age, Gender gender) {
        User user = getUserById(userId);

        if (nickname != null && !nickname.isEmpty()) {
            user.setNickname(nickname);
        }
        if (mbti != null && !mbti.isEmpty()) {
            user.setMbti(mbti);
        }
        user.setExercise(exercise);
        user.setSmoke(smoke);
        if (major != null && !major.isEmpty()) {
            user.setMajor(major);
        }
        if (studentNumber != null) {
            user.setStudentNumber(studentNumber);
        }
        if (name != null && !name.isEmpty()) {
            user.setName(name);
        }
        if (age != null) {
            user.setAge(age);
        }
        if (gender != null) {
            user.setGender(gender);
        }

        return saveUser(user);
    }

    // 회원 삭제
    public void deleteUser(Long userId) {
        System.out.println("Deleting user with userId: " + userId);
        userRepository.deleteById(userId);
    }

    // 비밀번호 변경
    public void changePassword(User user, String newPassword) {
        String encryptedPassword = passwordEncoder.encode(newPassword);
        user.setLoginPwd(encryptedPassword);
        saveUser(user);
    }

    // 닉네임 변경
    public void changeNickname(User user, String newNickname) {
        user.setNickname(newNickname);
        saveUser(user);
    }

    // 유저 저장
    private User saveUser(User user) {
        System.out.println("Saving user: " + user);
        return userRepository.save(user);
    }

    // 유저 update
    public User updateUser(User user) {
        System.out.println("Updating user: " + user);
        return userRepository.save(user);
    }
}
