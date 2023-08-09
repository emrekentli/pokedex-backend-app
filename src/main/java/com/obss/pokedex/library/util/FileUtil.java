package com.obss.pokedex.library.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
public class FileUtil {

    public static String transferFile (MultipartFile file)  {
        File tempDir = new File("src/main/resources/static/images");
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        File convFile = new File(tempDir, file.getOriginalFilename());
        try {
            file.transferTo(convFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return convFile.getAbsolutePath();
    }
}
