package client.SupCheck;

import java.util.ArrayList;

import client.ExecuteScript.ExecuteScript;
import client.SupCheck.Type;

public class InputFileCheck {

    public static String[] data = new String[]{};

    public String[] checkInput() {
        ArrayList<String> list = new ArrayList<>();

        try {
            ArrayList<String> currentFileCommands = ExecuteScript.collectionOfMultipleFiles
                    .get(ExecuteScript.collectionOfMultipleFiles.size() - 1);

            if (currentFileCommands.size() < 7) {
                return null;
            }

            // name
            String name = currentFileCommands.remove(0).trim();
            if (name.isEmpty()) return null;
            list.add(name);

            // x
            String xStr = currentFileCommands.remove(0).trim();
            if (!DataCheck.isLong(xStr)) return null;
            long x = Long.parseLong(xStr);
            if (x <= -978) return null;
            list.add(xStr);

            // y
            String yStr = currentFileCommands.remove(0).trim();
            if (!DataCheck.isLong(yStr)) return null;
            long y = Long.parseLong(yStr);
            if (y <= -45) return null;
            list.add(yStr);

            // enginePower (nullable, must be > 0 if provided)
            String enginePowerStr = currentFileCommands.remove(0).trim();
            if (!enginePowerStr.isEmpty()) {
                if (!DataCheck.isLong(enginePowerStr)) return null;
                long enginePower = Long.parseLong(enginePowerStr);
                if (enginePower <= 0) return null;
                list.add(enginePowerStr);
            } else {
                list.add(""); // nullable field
            }

            // fuelConsumption
            String fuelStr = currentFileCommands.remove(0).trim();
            try {
                float fuel = Float.parseFloat(fuelStr);
                if (fuel <= 0) return null;
                list.add(fuelStr);
            } catch (NumberFormatException e) {
                return null;
            }

            // distanceTravelled
            String distanceStr = currentFileCommands.remove(0).trim();
            if (!DataCheck.isLong(distanceStr)) return null;
            long distance = Long.parseLong(distanceStr);
            if (distance <= 0) return null;
            list.add(distanceStr);

            // vehicleType (nullable enum)
            String typeStr = currentFileCommands.remove(0).trim();
            if (!typeStr.isEmpty()) {
                try {
                    Type.valueOf(typeStr.toUpperCase());
                    list.add(typeStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    return null;
                }
            } else {
                list.add(""); // nullable
            }

            data = list.toArray(new String[0]);
            return data;

        } catch (Exception e) {
            return null;
        }
    }
}
