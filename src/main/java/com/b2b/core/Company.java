package com.b2b.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "company", uniqueConstraints = {
        @UniqueConstraint(name = "uk_company_name", columnNames = "name")
})
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(length = 255)
    private String address;

    @Column(length = 60)
    private String city;

    @Column(length = 30)
    private String phone;

    @Column(length = 120)
    private String website;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "company")
    private List<AppUser> users;


    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    // Getters/Setters
    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
