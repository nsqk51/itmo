package client.SupCheck;

import client.SupRead.InputReader;
import client.SupCheck.Type;

public class InputCheck {

public String[] readVehicleInput() {
  try {
        InputReader inputReader = new InputReader();

        // Ввод имени транспорта
        String name = null;
        while (name == null || name.trim().isEmpty()) {
            System.out.print("Введите название транспорта (не может быть пустым): ");
            name = inputReader.readString().trim();
            if (name.isEmpty()) {
                System.out.println("Ошибка: Название транспорта не может быть пустым!");
            }
        }

        Long x = null;
        while (x == null || x <= -978) {
            System.out.print("Введите координату X (целое число, минимум -978): ");
            String xField = inputReader.readString();
            
            if (xField == null) {
              throw new NullPointerException();
            }
            
            try {
                x = Long.parseLong(xField);
                if (x <= -978) {
                    System.out.println("Ошибка: X должен быть больше -978!");
                    x = null;
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: X должен быть числом.");
            }
        }

        // Ввод координаты Y
        Long y = null;
        while (y == null) {
            System.out.print("Введите координату Y (целое число, минимум -45): ");
            String yField = inputReader.readString();
            
            if (yField == null) {
              throw new NullPointerException();
            }
            
            try {
                y = Long.parseLong(yField);
                if (y <= -45) {
                    System.out.println("Ошибка: Y должен быть больше -45!");
                    y = null;
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: Y должен быть числом.");
            }
        }
        // Ввод силы двигателя
        Long enginePower = null;
        while (enginePower == null) {
            System.out.print("Введите силу двигателя (целое число, минимум 1) или оставьте пустым: ");
            String enginePowerField = inputReader.readString();
            
            if (enginePowerField.isEmpty()) {
                break;
            }
            try {
                enginePower = Long.parseLong(enginePowerField);
                if (enginePower <= 0) {
                  enginePower = null;
                  System.out.println("Ошибка: сила двигателя должна быть больше 0!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: сила двигателя должна быть целым числом.");
            }
        }
        // Ввод потребления топлива
        Float fuelConsumption = null;
        while (fuelConsumption == null || fuelConsumption <= 0) {
            System.out.print("Введите потребление топлива (целое число, минимум 1): ");
            String fuelConsumptionField = inputReader.readString();
            try {
                fuelConsumption = Float.parseFloat(fuelConsumptionField);
                if (fuelConsumption <= 0) {
                    System.out.println("Ошибка: потребление топлива должно быть больше 0!");
                    fuelConsumption = null;
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: потребление топлива должно быть числом.");
            }
        }
        // Ввод пройденной дистанции
        Long distanceTravelled = null;
        while (distanceTravelled == null || distanceTravelled <= 0) {
            System.out.print("Введите пройденную дистанцию (целое число, минимум 1): ");
            String distanceTravelledField = inputReader.readString();
            
            if (distanceTravelledField == null) {
              throw new NullPointerException();
            }
            
            try {
                distanceTravelled = Long.parseLong(distanceTravelledField);
                if (distanceTravelled <= 0) {
                    System.out.println("Ошибка: пройденная дистанция должна быть больше 0!");
                    distanceTravelled = null;
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: пройденная дистанция должна быть числом.");
            }
        }

        // Ввод типа транспорта
        Type type = null;
        while (true) {
            System.out.print("Введите тип транспорта (HELICOPTER, DRONE, BOAT, BICYCLE, MOTORCYCLE) или оставьте пустым: ");
            String typeField = inputReader.readString().trim();
            if (typeField.isEmpty()) {
                break;
            }
            try {
                type = Type.valueOf(typeField.toUpperCase());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: Неверный тип транспорта. Допустимые значения: HELICOPTER, DRONE, BOAT, BICYCLE, MOTORCYCLE.");
            }
        }
        
        return new String[] {
                String.valueOf(name),
                String.valueOf(x),
                String.valueOf(y),
                String.valueOf(enginePower),
                String.valueOf(fuelConsumption),
                String.valueOf(distanceTravelled),
                String.valueOf(type)
        };

            
        } catch (NullPointerException e) {
          System.out.println("\nВвод данных прерван пользователем.");
          return null;
      }
  }
  
}