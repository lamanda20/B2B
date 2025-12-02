package com.b2b.dto;

import lombok.Data;

@Data
public class CompanyDto {
    private Long id;
    private String name;
    private String address;
    private String city;
    private String phone;
    private String createdAt;
    private String email;
    private boolean enabled;
}
