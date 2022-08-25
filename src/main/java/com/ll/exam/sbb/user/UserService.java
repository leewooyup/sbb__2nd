package com.ll.exam.sbb.user;

import com.ll.exam.sbb.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SiteUser create(String username, String email, String password) throws SignupUsernameDuplicatedException, SignupEmailDuplicatedException {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        try {
            this.userRepository.save(user);
        } catch(DataIntegrityViolationException e) {
            if(userRepository.existsByUsername(username)) {
                throw new SignupUsernameDuplicatedException("이미 사용중인 username입니다.");
            } else {
                throw new SignupEmailDuplicatedException("이미 사용중인 email 입니다.");
            }
        }

        return user;
    }

    public SiteUser getUser(String username) {
        return this.userRepository.findByUsername(username).orElseThrow(() ->
                new DataNotFoundException("siteUser not found")
        );
    }
}
