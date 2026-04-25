package com.oaosis.samak.infra.s3.service;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.oaosis.samak.infra.s3.config.S3Properties;
import com.oaosis.samak.infra.s3.exception.S3Exception;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.oaosis.samak.infra.s3.exception.S3ErrorCode.IMAGE_FILE_UPLOAD_FAIL;

@Service
public class AmazonS3Service {

    private final AmazonS3Client amazonS3Client;
    private final S3Properties s3Properties;

    public AmazonS3Service(AmazonS3Client amazonS3Client, S3Properties s3Properties) {
        this.amazonS3Client = amazonS3Client;
        this.s3Properties = s3Properties;
    }

    public String uploadImage(MultipartFile multipartFile) {
        if (multipartFile == null
                || multipartFile.isEmpty()
                || multipartFile.getOriginalFilename() == null) {
            return null;
        }

        String fileName = createFileName(multipartFile.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        try {
            InputStream inputStream = multipartFile.getInputStream();
            String bucketName = s3Properties.getS3().getBucket();
            amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata));
        } catch (IOException e) {
            throw new S3Exception(IMAGE_FILE_UPLOAD_FAIL);
        }

        return fileName;
//        return amazonS3Client.getUrl(bucketName, fileName).toString();
    }

    public List<String> uploadImages(List<MultipartFile> multipartFiles) {
        List<String> fileNames = new ArrayList<>();

        multipartFiles.forEach(file -> {
            String fileName = uploadImage(file);
            fileNames.add(fileName);
        });

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

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            metadata.setContentLength(imageBytes.length);

            String bucketName = s3Properties.getS3().getBucket();
            amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, new ByteArrayInputStream(imageBytes), metadata));

            return fileName;
        } catch (IOException e) {
            throw new S3Exception(IMAGE_FILE_UPLOAD_FAIL);
        }
    }

    private String createFileName(String fileName) {
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        return UUID.randomUUID().toString().concat(fileExtension);
    }
}