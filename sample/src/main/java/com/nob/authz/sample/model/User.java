package com.nob.authz.sample.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class User {
    private String id;
    private String username;
    private List<String> roles;
    private String email;
    private String phoneNumber;
    private String address;
}
