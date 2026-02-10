package com.oaosis.samak.infra.s3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ImageService {

    private final AmazonS3Service amazonS3Service;

    public String uploadImage(MultipartFile multipartFile) {
        return amazonS3Service.uploadImage(multipartFile);
    }

    public List<String> uploadImages(List<MultipartFile> multipartFiles) {
        return amazonS3Service.uploadImages(multipartFiles);
    }
}