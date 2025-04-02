package com.example.jeevanjyotandroidapplication;

public class Doctor {
    private String name;
    private String specialization;
    private String phone;
    private String email;
    private String password;

    public Doctor(String name, String specialization, String phone, String email,String password) {
        this.name = name;
        this.specialization = specialization;
        this.phone = phone;
        this.email = email;
        this.password=password;
    }

    public String getName() { return name; }
    public String getSpecialization() { return specialization; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getPassword() {return password;}
}
