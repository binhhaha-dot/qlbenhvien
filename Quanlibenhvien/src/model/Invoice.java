package model;

import java.sql.Date;

public class Invoice {
    private int id;
    private String name;
    private Date day;
    private String service;
    private int money;

    // Constructor có đủ tham số
    public Invoice(int id, String name, Date day, String service, int money) {
        this.id = id;
        this.name = name;
        this.day = day;
        this.service = service;
        this.money = money;
    }

    // Constructor không có ID (khi thêm mới)
    public Invoice(String name, Date day, String service, int money) {
        this.name = name;
        this.day = day;
        this.service = service;
        this.money = money;
    }

    // Getter & Setter chuẩn Java
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
