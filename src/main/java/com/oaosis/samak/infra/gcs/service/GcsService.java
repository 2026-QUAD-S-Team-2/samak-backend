package com.oaosis.samak.infra.gcs.service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.oaosis.samak.infra.gcs.config.GcsProperties;
import com.oaosis.samak.infra.gcs.exception.GcsErrorCode;
import com.oaosis.samak.infra.gcs.exception.GcsException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GcsService {
    private final Storage storage;
    private final GcsProperties gcsProperties;

    public GcsService(Storage storage, GcsProperties gcsProperties) {
        this.storage = storage;
        this.gcsProperties = gcsProperties;
    }

    public String uploadImage(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty() || multipartFile.getOriginalFilename() == null) {
            return null;
        }
        String fileName = createFileName(multipartFile.getOriginalFilename());
        BlobId blobId = BlobId.of(gcsProperties.getBucket(), fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(multipartFile.getContentType())
                .build();
        try {
            storage.create(blobInfo, multipartFile.getBytes());
        } catch (IOException e) {
            throw new GcsException(GcsErrorCode.IMAGE_FILE_UPLOAD_FAIL);
        }
        return fileName;
    }

    public List<String> uploadImages(List<MultipartFile> multipartFiles) {
        List<String> fileNames = new ArrayList<>();
        multipartFiles.forEach(file -> fileNames.add(uploadImage(file)));
        return fileNames;
    }

    public String uploadImageFromUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return null;
        }
        try {
            URLConnection connection = new URL(imageUrl).openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            String contentType = connection.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                contentType = "image/jpeg";
            }

            byte[] imageBytes = connection.getInputStream().readAllBytes();
            String extension = contentType.contains("/") ? "." + contentType.split("/")[1].split(";")[0] : ".jpg";
            String fileName = UUID.randomUUID() + extension;

            BlobId blobId = BlobId.of(gcsProperties.getBucket(), fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(contentType)
                    .build();
            storage.create(blobInfo, imageBytes);

            return fileName;
        } catch (IOException e) {
            throw new GcsException(GcsErrorCode.IMAGE_FILE_UPLOAD_FAIL);
        }
    }

    private String createFileName(String fileName) {
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        return UUID.randomUUID().toString().concat(fileExtension);
    }
}
