package cardvault.CardVault.security.user_details;

import cardvault.CardVault.core.users.entities.UserEntity;
import cardvault.CardVault.core.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> optional = userRepository.findByEmail(username);
        if (optional.isPresent()) {
            return new UserDetailsImpl(optional.get());
        }
        throw new UsernameNotFoundException("Username " + username + " not found!");
    }
}
