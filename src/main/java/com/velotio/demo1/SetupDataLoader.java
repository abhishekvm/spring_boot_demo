package com.velotio.demo1;

import com.velotio.demo1.domains.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class SetupDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;

        Role admin = createRoleIfNotFound("ADMIN");
        Role developer = createRoleIfNotFound("DEVELOPER");
        Role security = createRoleIfNotFound("SECURITY");

        Organization organization = createOrganizationIfNotFound("velotio.com");

        createUser("siddharth.shishulkar@velotio.com", "Sid", "1212", organization, Arrays.asList(admin));

        alreadySetup = true;
    }

    @Transactional
    Role createRoleIfNotFound(String name) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            roleRepository.save(role);
        }
        return role;
    }

    @Transactional
    Organization createOrganizationIfNotFound(String name) {
        Organization organization = organizationRepository.findByName(name);
        if (organization == null) {
            organization = new Organization(name);
            organizationRepository.save(organization);
        }
        return organization;
    }

    @Transactional
    User createUser(String email, String name, String password, Organization organization, Collection<Role> roles) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setOrganization(organization);
            user.setRoles(roles);
            user.setPassword(bcryptEncoder.encode(password));
            userRepository.save(user);
        }
        return user;
    }
}
