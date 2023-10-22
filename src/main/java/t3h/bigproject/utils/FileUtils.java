package t3h.bigproject.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileUtils {
    @Value("${folder.storage.file}")
    String FOLDER;
    public String saveFile(MultipartFile multipartFile) throws IOException {
        return saveFile(multipartFile, null);
    }
    public String saveFile(MultipartFile multipartFile, String preFixFolder) throws IOException {
        if (preFixFolder == null) preFixFolder = "";
        InputStream initialStream = multipartFile.getInputStream();
        byte[] buffer = new byte[initialStream.available()];
        initialStream.read(buffer);

        File folder = new File(FOLDER + preFixFolder);
        if (!folder.exists()) folder.mkdirs();
        String fileName = System.currentTimeMillis() + multipartFile.getOriginalFilename();
        File targetFile = new File(FOLDER + preFixFolder + "\\" + fileName);
        try (OutputStream outStream = new FileOutputStream(targetFile)) {
            outStream.write(buffer);
        }

        return fileName;
    }

    public void cleanDir(String dir) {
        Path dirPath = Paths.get(dir);
        try {
            Files.list(dirPath).forEach(file -> {
                if (!Files.isDirectory(file)) {
                    try {
                        Files.delete(file);
                    } catch (IOException ex) {
                    }
                }
            });
        } catch (IOException ex) {
        }
    }
}
