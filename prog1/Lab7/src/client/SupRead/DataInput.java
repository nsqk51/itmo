package client.SupRead;

import java.io.*;

public class DataInput {
    private static final BufferedInputStream bis = new BufferedInputStream(System.in);

    public String input() {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int byteRead;

            while ((byteRead = bis.read()) != -1) { // Читаем побайтно
                if (byteRead == '\n') { // Конец строки
                    break;
                }
                buffer.write(byteRead); // Запись байта в буфер
            }

            // Если Ctrl+D был нажат до ввода — буфер пуст
            if (byteRead == -1 && buffer.size() == 0) {
                return null;
            }

            return buffer.toString().trim();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
