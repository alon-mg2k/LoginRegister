package com.example.loginregister.Model;

public class Users {
    private String id, email, name, password, dispositivo;

    public Users() {

    }

    public Users(String id, String email, String name, String password, String dispositivo) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.dispositivo = dispositivo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(String dispositivo) {
        this.dispositivo = dispositivo;
    }


}
