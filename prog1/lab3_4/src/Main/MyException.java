package Main;

public class MyException extends Exception{
 public MyException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "Неверно введенное значение настроения: " + super.getMessage();
    }
}

