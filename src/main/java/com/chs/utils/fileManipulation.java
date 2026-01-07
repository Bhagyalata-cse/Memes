package com.chs.utils;

import org.springframework.web.multipart.MultipartFile;
import java.util.Arrays;

public class fileManipulation {
    public static boolean isPicture(MultipartFile file){
        try {
            String extension = getExtension(file);
            System.out.println(extension);
            return Arrays.asList(
                    "jpg", "jpeg", "png", "gif", "bmp", "webp","jfif",
                    "tiff", "tif", "svg", "ico", "heif", "heic","svgz",
                    "raw", "apng", "exif", "hdp", "wdp", "jp2", "psd","avif"
            ).contains(extension);
        }catch (Exception e){
            return false;
        }
    }

    public static String getExtension(MultipartFile file){
        String OriginalFilenameName = file.getOriginalFilename();
        return OriginalFilenameName.substring(OriginalFilenameName.lastIndexOf(".") + 1).toLowerCase();
    }
}
