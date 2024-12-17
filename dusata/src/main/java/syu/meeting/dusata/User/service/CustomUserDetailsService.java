package syu.meeting.dusata.User.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import syu.meeting.dusata.User.entity.User;
import syu.meeting.dusata.User.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with loginId: " + loginId));

        List<GrantedAuthority> authorities = new ArrayList<>();

        return new org.springframework.security.core.userdetails.User(user.getLoginId(), user.getLoginPwd(), authorities);
    }
}
