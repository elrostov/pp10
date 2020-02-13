package org.example.service;

import org.example.dao.UserDao;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserDao userDao;

    public UserDetailsServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = userDao.loadUserByUsername(username);
        if (userDetails == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return userDetails;
    }
}
