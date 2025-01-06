package com.nob.authz.sample.config;

import com.nob.authorization.core.context.Subject;
import com.nob.authz.sample.model.User;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class SubjectProvider implements com.nob.authorization.authzclient.pip.SubjectProvider {

    private final Subject subject;

    public SubjectProvider() {
        User user = User.builder()
                .id("1")
                .username("user")
                .roles(List.of("ROLE_USER"))
                .address("address")
                .email("user@example.com")
                .phoneNumber("0333777888")
                .build();
        subject = mapUserToSubject(user);
    }

    private Subject mapUserToSubject(User user) {
        Subject subject = new Subject();
        subject.setUserId(user.getId());
        subject.setRoles(user.getRoles());
        Map<String, Object> attributes = new LinkedHashMap<>();
        attributes.put("username", user.getUsername());
        attributes.put("email", user.getEmail());
        attributes.put("phoneNumber", user.getPhoneNumber());
        attributes.put("address", user.getAddress());
        subject.setAttributes(attributes);
        return subject;
    }

    @Override
    public Subject getSubject(Principal principal) {
        return subject;
    }
}
