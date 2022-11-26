package com.example.userservice.service;

import com.example.userservice.entity.Role;
import com.example.userservice.entity.User;
import com.example.userservice.repository.RoleRepository;
import com.example.userservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final String REDIS_CACHE_USER = "user";

    private static final String REDIS_CACHE_ROLE = "role";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = null;
        user = (User) redisTemplate.opsForHash().get(REDIS_CACHE_USER, username);
        if(user != null){
            System.out.println("get user from redis");
        }else {
            System.out.println("get user from db");
            user = userRepository.findByUsername(username);
            redisTemplate.opsForHash().put(REDIS_CACHE_USER, user.getUsername(), user);
        }

        if(user == null){
            throw new UsernameNotFoundException("User not found in the database");
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        redisTemplate.opsForHash().put(REDIS_CACHE_USER, user.getUsername(), user);
        return userRepository.save(user);
    }

    @Override
    public Role saveRole(Role role) {
//        redisTemplate.opsForHash().put(REDIS_CACHE_ROLE, role.getName(), role);
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {

        User user = userRepository.findByUsername(username);
        Role role = roleRepository.findByName(roleName);
        user.getRoles().add(role);

//        User user = null;
//        Role role = null;
//
//        user = (User) redisTemplate.opsForHash().get(REDIS_CACHE_USER, username);
//        if(user != null){
//            System.out.println("get user from redis");
//        }else {
//            System.out.println("get user from db");
//            user = userRepository.findByUsername(username);
//            redisTemplate.opsForHash().put(REDIS_CACHE_USER, user.getUsername(), user);
//        }
//
//        role = (Role) redisTemplate.opsForHash().get(REDIS_CACHE_ROLE, roleName);
//        if(role != null){
//            System.out.println("get role from redis");
//        }else {
//            System.out.println("get role from db");
//            role = roleRepository.findByName(roleName);
//            redisTemplate.opsForHash().put(REDIS_CACHE_ROLE, role.getName(), role);
//        }
//
//        user.getRoles().add(role);
    }

    @Override
    public User getUser(String username) {
//        User user = null;
//
//        user = (User) redisTemplate.opsForHash().get(REDIS_CACHE_USER, username);
//        if(user != null){
//            System.out.println("get user from redis");
//        }else {
//            System.out.println("get user from db");
//            user = userRepository.findByUsername(username);
//            redisTemplate.opsForHash().put(REDIS_CACHE_USER, user.getUsername(), user);
//        }

        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }


}
