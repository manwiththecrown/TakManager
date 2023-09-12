package com.example.workissues.utilites;

public class User {
    private String id;
    private String name;
    private String login;
    private String password;
    private Role userRole;

    public User() {}

    public User(String id, String name, String login, String password, Role userRole) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
        this.userRole = userRole;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getUserRole() {
        return userRole;
    }

    public void setUserRole(Role userRole) {
        this.userRole = userRole;
    }
}
