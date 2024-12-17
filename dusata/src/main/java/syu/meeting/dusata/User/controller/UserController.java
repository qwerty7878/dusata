package syu.meeting.dusata.User.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder; // 인증 정보 가져오기 위함
import org.springframework.security.crypto.password.PasswordEncoder; // 비번 암호화
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import syu.meeting.dusata.User.dto.*;
import syu.meeting.dusata.User.entity.User;
import syu.meeting.dusata.User.service.UserService;
import syu.meeting.dusata.config.JwtUtil;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRequest request) {
        try {
            String encodedPassword = passwordEncoder.encode(request.getLoginPwd());
            User user = userService.register(
                    request.getLoginId(),
                    encodedPassword,
                    request.getNickname(),
                    request.getMbti(),
                    request.isExercise(),
                    request.isSmoke(),
                    request.getMajor(),
                    request.getStudentNumber(),
                    request.getName(),
                    request.getAge(),
                    request.getGender()
            );

            UserResponse userResponse = new UserResponse(
                    user.getId(),
                    user.getLoginId(),
                    user.getNickname(),
                    user.getMbti(),
                    user.isExercise(),
                    user.isSmoke(),
                    user.getMajor(),
                    user.getStudentNumber(),
                    user.getName(),
                    user.getAge(),
                    user.getGender()
            );

            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 일반로그인
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        try {
            User user = userService.getUserByLoginId(request.getLoginId());
            if (user != null && passwordEncoder.matches(request.getLoginPwd(), user.getLoginPwd())) {
                String token = jwtUtil.generateToken(user.getLoginId(), user.getId());
                return new LoginResponse(token, request.getLoginId(), user.getNickname());
            } else {
                return new LoginResponse(null, request.getLoginId(), null);
            }
        } catch (Exception e) {
            return new LoginResponse(null, null, null);
        }
    }

    // 아이디 중복 체크
    @GetMapping("/check/loginId")
    public CheckResponse checkLoginId(@RequestParam String loginId) {
        boolean exists = userService.checkLoginId(loginId);
        return new CheckResponse(!exists);
        // exists=true면 사용불가, false면 사용가능 → 사용가능 여부를 반환하므로 !exists
    }

    // 닉네임 중복 체크
    @GetMapping("/check/nickname")
    public CheckResponse checkNickname(@RequestParam String nickname) {
        if (nickname == null || nickname.isEmpty()) {
            return new CheckResponse(false);
        } else {
            boolean exists = userService.checkNickname(nickname);
            return new CheckResponse(!exists);
        }
    }

    // 프로필(닉네임, mbti, 운동여부, 흡연여부, 전공, 학번, 이름, 나이, 성별) 변경
    @PatchMapping("/profile/update")
    public ResponseEntity<UpdateResponse> updateProfile(@RequestBody UpdateRequest request) {
        try {
            Long userId = getAuthenticatedUserId();
            User user = userService.getUserById(userId);

            if (user != null) {
                User updatedUser = userService.updateUserProfile(
                        user.getId(),
                        request.getNickname(),
                        request.getMbti(),
                        request.isExercise(),
                        request.isSmoke(),
                        request.getMajor(),
                        request.getStudentNumber(),
                        request.getName(),
                        request.getAge(),
                        request.getGender()
                );

                UpdateResponse response = new UpdateResponse(
                        updatedUser.getNickname(),
                        updatedUser.getMbti(),
                        updatedUser.isExercise(),
                        updatedUser.isSmoke(),
                        updatedUser.getMajor(),
                        updatedUser.getStudentNumber(),
                        updatedUser.getName(),
                        updatedUser.getAge(),
                        updatedUser.getGender()
                );
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 회원탈퇴
    @DeleteMapping("/profile/delete")
    public ResponseEntity<String> deleteUser() {
        try {
            Long userId = getAuthenticatedUserId();
            User user = userService.getUserById(userId);

            if (user != null) {
                userService.deleteUser(user.getId());
                return ResponseEntity.ok("User deleted successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the user.");
        }
    }

    // 비밀번호 변경
    @PatchMapping("/change/password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeRequest request) {
        try {
            Long userId = getAuthenticatedUserId();
            User user = userService.getUserById(userId);

            if (!passwordEncoder.matches(request.getCurrentPassword(), user.getLoginPwd())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Current password is incorrect.");
            }

            userService.changePassword(user, request.getNewPassword());
            return ResponseEntity.ok("Password changed successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while changing the password.");
        }
    }

    // 닉네임 변경
    @PatchMapping("/change/nickname")
    public ResponseEntity<String> changeNickname(@RequestBody NicknameChangeRequest request) {
        try {
            Long userId = getAuthenticatedUserId();
            User user = userService.getUserById(userId);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found or invalid.");
            }

            userService.changeNickname(user, request.getNewNickname());
            return ResponseEntity.ok("Nickname changed successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while changing the nickname.");
        }
    }

    // 프로필 사진 설정
    @PutMapping(value = "/profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadProfilePicture(@RequestParam("file") MultipartFile file) {
        try {
            Long userId = getAuthenticatedUserId();
            User user = userService.getUserById(userId);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
            String uniqueFilename = userId + "_" + UUID.randomUUID().toString() + extension;

            Path path = Paths.get("uploads/profile-pictures/" + uniqueFilename);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());

            user.setProfilePicture(path.toString());
            userService.updateUser(user);

            return ResponseEntity.ok("File uploaded successfully and profile picture updated");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File upload failed: " + e.getMessage());
        }
    }

    @GetMapping("/profile-picture")
    public ResponseEntity<byte[]> getProfilePicture(HttpServletRequest request) {
        try {
            String token = jwtUtil.extractTokenFromRequest(request);
            Long userId = jwtUtil.extractUserId(token);

            User user = userService.getUserById(userId);

            if (user == null || user.getProfilePicture() == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Path path = Paths.get(user.getProfilePicture());
            byte[] data = Files.readAllBytes(path);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentLength(data.length);

            return new ResponseEntity<>(data, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Long getAuthenticatedUserId() {
        // SecurityContextHolder에서 인증된 사용자 ID(=loginId) 추출
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByLoginId(loginId);
        return user.getId();
    }
}
