package kz.kazgeowarning.authgateway.dto;


import kz.kazgeowarning.authgateway.model.User;

import java.io.Serializable;


public class TokenDTO implements Serializable {

    private static final long serialVersionUID = 6710061358371752955L;

    private String token;

    private User user;

    public TokenDTO() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
