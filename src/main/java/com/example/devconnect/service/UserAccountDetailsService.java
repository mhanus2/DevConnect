package com.example.devconnect.service;

import com.example.devconnect.model.UserAccount;
import com.example.devconnect.repository.UserAccountRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAccountDetailsService implements UserDetailsService {

    private final UserAccountRepository repository;

    public UserAccountDetailsService(UserAccountRepository repository, UserAccountRepository userAccountRepository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserAccount> user = repository.findByUsername(username);
        if (user.isPresent()) {
            var userObj = user.get();
            return User.builder()
                    .username(userObj.getUsername())
                    .password(userObj.getPassword())
                    .roles(getRole(userObj))
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    private String getRole(UserAccount user) {
        if (user.getRole() == null) {
            return "USER";
        }
        return user.getRole();
    }

    public void saveUser(UserAccount user) {
        repository.save(user);
    }

    public Optional<UserAccount> getUserByUsername(String username) {
        return repository.findByUsername(username);
    }

    public Optional<UserAccount> getUserById(Integer id) {
        return repository.findById(id);
    }

}
