package com.b2b.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dhcg9l3kj",
                "api_key", "577499511431825",
                "api_secret", "1vQ04pA0U1zJFnqjqcZZL9xwab0"
        ));
    }
}
