package com.example.college.pojo;

public class Regist {
    private String name;
    private String reason;
    private String in_time;
    private String out_time;
    private String phone;
    private String registrar;
    private String id;

    public Regist() {
    }

    public Regist(String name, String reason, String in_time, String out_time, String phone,String registrar,String id) {
        this.name = name;
        this.reason = reason;
        this.in_time = in_time;
        this.out_time = out_time;
        this.phone = phone;
        this.registrar = registrar;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getIn_time() {
        return in_time;
    }

    public void setIn_time(String in_time) {
        this.in_time = in_time;
    }

    public String getOut_time() {
        return out_time;
    }

    public void setOut_time(String out_time) {
        this.out_time = out_time;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegistrar() {
        return registrar;
    }

    public void setRegistrar(String registrar) {
        this.registrar = registrar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "Regist{" +
                "name='" + name + '\'' +
                ", reason='" + reason + '\'' +
                ", in_time='" + in_time + '\'' +
                ", out_time='" + out_time + '\'' +
                ", phone='" + phone + '\'' +
                ", registrar='" + registrar + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
