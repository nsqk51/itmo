package client.SupRead;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;

/**
 * Класс для считывания ввода с консоли или из файла.
 * Позволяет читать строки, целые числа и числа с плавающей запятой.
 */
public class InputReader {
    
    private BufferedReader reader;

    /**
     * Конструктор, инициализирует считыватель для ввода с консоли.
     */
    public InputReader() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Конструктор, инициализирует считыватель для ввода из указанного файла.
     * 
     * @param filePath путь к файлу для чтения.
     * @throws IOException если произошла ошибка при открытии файла.
     */
    public InputReader(String filePath) throws IOException {
        InputStream inputStream = new FileInputStream(filePath);
        this.reader = new BufferedReader(new InputStreamReader(inputStream));
    }

    /**
     * Читает строку из ввода (с консоли или файла), убирает пробелы в начале и в конце.
     * 
     * @return строку, считанную с ввода, или null в случае ошибки.
     */
    public String readString() {
        try {
            return reader.readLine().trim();
        } catch (IOException e) {
            System.err.println("Ошибка чтения ввода: " + e.getMessage());
            return null;
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Читает целое число из ввода.
     * Повторяет запрос ввода, пока не будет введено корректное число.
     * 
     * @return целое число, введенное пользователем.
     */
    public int readInt() {
        while (true) {
            try {
                String input = readString();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.err.println("Некорректный ввод. Пожалуйста, введите целое число.");
            }
        }
    }

    /**
     * Читает число с плавающей запятой из ввода.
     * Повторяет запрос ввода, пока не будет введено корректное число.
     * 
     * @return число с плавающей запятой, введенное пользователем.
     */
    public double readDouble() {
        while (true) {
            try {
                String input = readString();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.err.println("Некорректный ввод. Пожалуйста, введите число с плавающей запятой.");
            }
        }
    }

    /**
     * Закрывает считыватель ввода.
     * Освобождает ресурсы, связанные с чтением ввода.
     */
    public void close() {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            System.err.println("Ошибка при закрытии считывателя: " + e.getMessage());
        }
    }
}
