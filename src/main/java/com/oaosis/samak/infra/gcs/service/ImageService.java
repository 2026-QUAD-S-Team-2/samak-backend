package com.oaosis.samak.infra.gcs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ImageService {
    private final GcsService gcsService;

    public String uploadImage(MultipartFile multipartFile) {
        return gcsService.uploadImage(multipartFile);
    }

    public List<String> uploadImages(List<MultipartFile> multipartFiles) {
        return gcsService.uploadImages(multipartFiles);
    }
}
