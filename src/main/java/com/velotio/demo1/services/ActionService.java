package com.velotio.demo1.services;

import com.velotio.demo1.domains.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActionService {

    @Autowired
    ActionRepository actionRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    public void record(String name, String email) {
        Action action = new Action();
        action.setName(name);
        action.setUserEmail(email);
        action.setOrganization(organizationRepository.findByName(email.split("@")[1]));

        actionRepository.save(action);
    }

    public void record(String name, User user) {
        Action action = new Action();
        action.setName(name);
        action.setUser(user);
        action.setOrganization(user.getOrganization());

        actionRepository.save(action);
    }
}
