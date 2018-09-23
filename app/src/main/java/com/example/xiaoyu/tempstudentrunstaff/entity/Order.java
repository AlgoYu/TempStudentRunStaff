package com.example.xiaoyu.tempstudentrunstaff.entity;


public class Order {

  private Integer id;
  private String uuid;
  private Integer ridgepole;
  private Integer dorm;
  private String contact;
  private String info;
  private String staff;
  private String phone;
  private Integer flag;
  private String datetime;


  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getContact() {
    return contact;
  }

  public void setContact(String contact) {
    this.contact = contact;
  }

  public String getDatetime() {
    return datetime;
  }

  public void setDatetime(String datetime) {
    this.datetime = datetime;
  }

  public Integer getFlag() {
    return flag;
  }

  public void setFlag(Integer flag) {
    this.flag = flag;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getRidgepole() {
    return ridgepole;
  }

  public void setRidgepole(Integer ridgepole) {
    this.ridgepole = ridgepole;
  }

  public Integer getDorm() {
    return dorm;
  }

  public void setDorm(Integer dorm) {
    this.dorm = dorm;
  }

  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public String getStaff() {
    return staff;
  }

  public void setStaff(String staff) {
    this.staff = staff;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

}
