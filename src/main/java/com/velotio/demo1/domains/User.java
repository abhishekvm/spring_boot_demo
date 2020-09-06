package com.velotio.demo1.domains;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class User {

  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Long id;
  private String name;
  private String email;
  private String organization;
  private String password;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
          name = "users_roles",
          joinColumns = @JoinColumn(
                  name = "user_id", referencedColumnName = "id"),
          inverseJoinColumns = @JoinColumn(
                  name = "role_id", referencedColumnName = "id"))
  private Collection<Role> roles;

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

  public Collection<Role> getRoles() {
    return roles;
  }

  public void setRoles(Collection<Role> roles) {
    this.roles = roles;
  }
}
