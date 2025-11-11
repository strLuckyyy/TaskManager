package com.trendingtech.taskmanager.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    public static String saveFile(MultipartFile file, String directory) throws IOException {
        File folder = new File(directory);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String filePath = directory + File.separator + file.getOriginalFilename();
        file.transferTo(new File(filePath));
        return filePath;
    }
}
