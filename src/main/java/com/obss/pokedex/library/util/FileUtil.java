package com.obss.pokedex.library.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
public class FileUtil {

    public static String transferFile (MultipartFile file){
        String targetDir = System.getProperty("java.io.tmpdir") + "/uploads";
        File tempDir = new File(targetDir);
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        File convFile = new File(tempDir, file.getOriginalFilename());
try {
    file.transferTo(convFile);
} catch (IOException e) {
    throw new RuntimeException(e);
}


        return convFile.getAbsolutePath();
    }
}
