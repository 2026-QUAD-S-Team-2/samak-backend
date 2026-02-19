package com.oaosis.samak.infra.s3.service;

import com.oaosis.samak.infra.s3.config.S3Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class S3UrlBuilder {

    private final S3Properties s3Properties;

    public String buildImageUrlWithFilePath(String filePath, String imageName) {
        if (imageName == null || imageName.isEmpty()) {
            return null;
        }

        String urlPrefix = s3Properties.getS3().getUrlPrefix();
        String prefix = urlPrefix.endsWith("/") ? urlPrefix : urlPrefix + "/";
        String path = (filePath == null || filePath.isEmpty()) ? "" :
                (filePath.endsWith("/") ? filePath : filePath + "/");
        String name = imageName.startsWith("/") ? imageName.substring(1) : imageName;

        return prefix + path + name;
    }

    public String buildImageUrl(String imageName) {
        if (imageName == null || imageName.isEmpty()) {
            return null;
        }

        String urlPrefix = s3Properties.getS3().getUrlPrefix();
        String prefix = urlPrefix.endsWith("/") ? urlPrefix : urlPrefix + "/";
        String name = imageName.startsWith("/") ? imageName.substring(1) : imageName;

        return prefix + name;
    }
}