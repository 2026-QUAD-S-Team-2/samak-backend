package com.oaosis.samak.infra.s3.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class S3Util {


    public static String buildImageUrlWithFilePath(String urlPrefix, String filePath, String imageName) {
        if (imageName == null || imageName.isEmpty()) {
            return null;
        }

        String prefix = urlPrefix.endsWith("/") ? urlPrefix : urlPrefix + "/";
        String path = (filePath == null || filePath.isEmpty()) ? "" :
                (filePath.endsWith("/") ? filePath : filePath + "/");
        String name = imageName.startsWith("/") ? imageName.substring(1) : imageName;

        return prefix + path + name;
    }

    public static String buildImageUrl(String urlPrefix, String imageName) {
        if (imageName == null || imageName.isEmpty()) {
            return null;
        }

        String prefix = urlPrefix.endsWith("/") ? urlPrefix : urlPrefix + "/";
        String name = imageName.startsWith("/") ? imageName.substring(1) : imageName;

        return prefix + name;
    }
}