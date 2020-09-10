package com.velotio.demo1.services;

import com.velotio.demo1.domains.User;
import com.velotio.demo1.domains.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomOpenIdConnectUserService extends OidcUserService {

    Logger logger = LoggerFactory.getLogger(CustomOpenIdConnectUserService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        Map attributes = oidcUser.getAttributes();
        String userEmail = (String) attributes.get("email");
        String userName = (String) attributes.get("name");

        User user = userRepository.findByEmail(userEmail);

        logger.info(user.toString());

        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        customUserDetails.setOidcUser(oidcUser);

        return customUserDetails;
    }
}
