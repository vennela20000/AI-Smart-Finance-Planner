package financeplanner.planner;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Fetch user from PostgreSQL
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // 2. Map your User entity to Spring Security's UserDetails
        // We pass the account status (isAccountNonLocked) to Spring's internal checker
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                true,          // enabled
                true,          // accountNonExpired
                true,          // credentialsNonExpired
                user.isAccountNonLocked(), // accountNonLocked (Uses our new database field!)
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
        );
    }
}