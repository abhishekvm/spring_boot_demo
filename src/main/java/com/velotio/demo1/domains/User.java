package com.velotio.demo1.domains;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {

  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Long id;
  private String name;
  private String email;
  private String organization;
  private String password;

  public User() {}

  public User(String name, String email, String organization) {
    this.name = name;
    this.email = email;
    this.organization = organization;
  }

  @Override
  public String toString() {
    return String.format(
        "User[id=%d, name='%s', email='%s']",
        id, name, email);
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getOrganization() {
    return organization;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setOrganization(String organization) {
    this.organization = organization;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
