package ru.nsqk.web;

import java.math.BigDecimal;
import java.util.List;

public class Validator {

    private static final String[] ALLOWED_Y = {"-4","-3","-2","-1","0","1","2","3","4"};
    private static final String[] ALLOWED_R = {"1","2","3","4","5"};

    private static final BigDecimal MIN_X = new BigDecimal("-5"); // граница (не включительно)
    private static final BigDecimal MAX_X = new BigDecimal("5");  // граница (не включительно)

    /**
     * Проверяет корректность параметров. Никаких double-сравнений для X — только BigDecimal.
     * Возвращает null если всё ок, иначе текст ошибки.
     */
    public static String validate(String xStr, String yStr, List<String> rList) {
        if (xStr == null || yStr == null || rList == null || rList.isEmpty()) {
            return "Не все параметры переданы";
        }

        // Приводим запятую к точке
        xStr = xStr.trim().replace(',', '.');

        BigDecimal xBD;
        try {
            // Запрещаем экспоненциальную форму вроде 1e2 (если надо разрешить — убери проверку)
            if (xStr.matches("(?i).*[e].*")) {
                return "X не должен быть в экспоненциальной форме";
            }
            // Проверка формата (одно число с optional знаком)
            if (!xStr.matches("^[+-]?(\\d+(\\.\\d*)?|\\.\\d+)$")) {
                return "X некорректен";
            }
            xBD = new BigDecimal(xStr);
        } catch (Exception e) {
            return "X некорректен";
        }

        // Открытый интервал (-5; 5)
        if (xBD.compareTo(MIN_X) <= 0 || xBD.compareTo(MAX_X) >= 0) {
            return "X вне диапазона (-5; 5)";
        }

        boolean yOk = false;
        for (String yv : ALLOWED_Y) {
            if (yv.equals(yStr)) { yOk = true; break; }
        }
        if (!yOk) return "Y некорректен";

        for (String rStr : rList) {
            boolean rOk = false;
            for (String allowed : ALLOWED_R) {
                if (allowed.equals(rStr)) { rOk = true; break; }
            }
            if (!rOk) return "R некорректен: " + rStr;
        }

        return null;
    }

    /**
     * Преобразование X в double для дальнейших вычислений (после успешной валидации).
     * Используем ту же логику (BigDecimal) чтобы не зависеть от повторного парсинга.
     */
    public static double parseXToDouble(String xStr) {
        xStr = xStr.trim().replace(',', '.');
        return new BigDecimal(xStr).doubleValue();
    }
}