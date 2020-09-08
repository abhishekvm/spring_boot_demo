package com.velotio.demo1.services;

import com.velotio.demo1.domains.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public String history(Organization organization) {
        String histroy = "<html>\n" + "<header><title>History</title></header>\n" +
                "<body><table style=\"width:100%\"><tr><th>Actions</th></tr>\n";

        List<Action> actions = actionRepository.findByOrganization(organization);
        for (Action action: actions) {
            histroy += "<tr><td>" + action.getDescription() + "</tr></td>";
        }

        histroy += "</table></body>\n" + "</html>";

        return histroy;
    }
}
