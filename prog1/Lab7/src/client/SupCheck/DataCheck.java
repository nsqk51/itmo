package client.SupCheck;

import java.lang.Integer;
/**
 * Класс для проверки и валидации различных типов данных.
 * Содержит статические методы для определения, является ли строка числом определенного типа,
 * а также для проверки входных данных на null.
 * 
 */
public class DataCheck {
	
	/**
     * Проверяет, может ли строка быть преобразована в целое число (Integer).
     * 
     * @param str Входная строка для проверки
     * @return true если стaрока может быть преобразована в Integer, иначе false
     */
	public static boolean isInteger(String str) {
		try {
			Integer.parseInt(str);
			return true;
		}
		catch (NumberFormatException e ){
			return false;
		
		}
	}
	
	/**
     * Проверяет, может ли строка быть преобразована в число с плавающей точкой (Double).
     * 
     * @param str Входная строка для проверки
     * @return true если строка может быть преобразована в Double, иначе false
     */
	public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
	
	/**
     * Проверяет, может ли строка быть преобразована в длинное целое число (Long).
     * 
     * @param str Входная строка для проверки
     * @return true если строка может быть преобразована в Long, иначе false
     */
	public static boolean isLong(String str) {
		try {
			Long.parseLong(str);
			return true;
		}
		catch(NumberFormatException e) {
			return false;
		}
	}
	
	/**
     * Проверяет, что переданный объект не является null. 
     * Если объект равен null, генерирует исключение {@link IllegalArgumentException}.
     * 
     * @param <T> Тип входного объекта
     * @param input Проверяемый объект
     * @return Исходный объект, если он не null
     * @throws IllegalArgumentException если входной объект равен null
     */
	public static <T> T requireNonNullInput(T input) {
	    if (input == null) {
	        throw new IllegalArgumentException("Ошибка: значение не может быть null. Повторите ввод.");
	    }
	    return input;
	}
	
}