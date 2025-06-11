package client.SupRead;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class FileDataInput implements AutoCloseable {
    private final BufferedInputStream bis;
    
    public FileDataInput(String fileName) {
        try {
            this.bis = new BufferedInputStream(new FileInputStream(fileName));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Ошибка: файл не найден - " + fileName, e);
        }
    }
    
    public String input() {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int byteRead;
            while ((byteRead = bis.read()) != -1) {
                if (byteRead == '\n') {
                    break;
                }
                buffer.write(byteRead);
            }
            return buffer.size() > 0 ? buffer.toString().trim() : null;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения файла", e);
        }
    }
    
    @Override
    public void close() {
        try {
            bis.close();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при закрытии файла", e);
        }
    }
  
}