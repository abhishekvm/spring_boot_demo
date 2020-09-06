package com.velotio.demo1.services;

import com.velotio.demo1.domains.RoleRepository;
import com.velotio.demo1.domains.User;
import com.velotio.demo1.domains.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;

    public String signUp(Map<String, String> userInfo) {
        User user = userRepository.findByEmail(userInfo.get("email"));

        if (user == null) {
            user = new User();
        }

        user.setEmail(userInfo.get("email"));
        user.setOrganization(userInfo.get("organization"));
        user.setName(userInfo.get("name"));
        user.setRoles(Arrays.asList(roleRepository.findByName(userInfo.get("role"))));
        user.setPassword(bcryptEncoder.encode(userInfo.get("password")));
        userRepository.save(user);

        return userInfo.get("email");
    }
}
