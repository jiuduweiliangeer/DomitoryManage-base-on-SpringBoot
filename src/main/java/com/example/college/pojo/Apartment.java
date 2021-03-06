package com.example.college.pojo;

public class Apartment {
    private String id;
    private String username;
    private String password;
    private String Email;
    private String sex;
    private String age;
    private String phone;
    private String apartment;
    private String apa_sex;
    private String state;
    public Apartment(){

    }
    public Apartment(String id, String username, String password, String email, String sex, String age, String phone, String apartment,String apa_sex, String state) {
        this.id = id;
        this.username = username;
        this.password = password;
        Email = email;
        this.sex = sex;
        this.age = age;
        this.phone = phone;
        this.apartment = apartment;
        this.apa_sex = apa_sex;
        this.state = state;
    }

    @Override
    public String toString() {
        return "Apartment{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", Email='" + Email + '\'' +
                ", sex='" + sex + '\'' +
                ", age='" + age + '\'' +
                ", phone='" + phone + '\'' +
                ", apartment='" + apartment + '\'' +
                ", apa_sex='" + apa_sex + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getApa_sex() {
        return apa_sex;
    }

    public void setApa_sex(String apa_sex) {
        this.apa_sex = apa_sex;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
