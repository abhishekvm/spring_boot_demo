package com.velotio.demo1.config;

import com.velotio.demo1.domains.User;
import com.velotio.demo1.domains.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomOpenIdConnectUserService extends OidcUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        Map attributes = oidcUser.getAttributes();
        String userEmail = (String) attributes.get("email");
        String userName = (String) attributes.get("name");
        String userOrganization = userEmail.split("@")[1];

        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            user = new User();
        }

        user.setEmail(userEmail);
        user.setName(userName);
        user.setOrganization(userOrganization);
        userRepository.save(user);

        return oidcUser;
    }
}

