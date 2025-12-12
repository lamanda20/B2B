package com.b2b.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

    private final Cloudinary cloudinary;

    public String uploadImage(File file) throws Exception {
        Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.asMap(
                "folder", "products"
        ));

        return uploadResult.get("secure_url").toString(); // RETURN REAL URL
    }
}
