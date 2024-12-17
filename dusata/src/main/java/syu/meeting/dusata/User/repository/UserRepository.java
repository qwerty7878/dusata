package syu.meeting.dusata.User.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import syu.meeting.dusata.User.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);
    boolean existsByNickname(String nickname);
}