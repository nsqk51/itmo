package client.ExecuteScript;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Map;

import client.SupCheck.InputFileCheck;
import client.SupCheck.CommandParser;

public class ExecuteScript {
    InputFileCheck checkInputFile = new InputFileCheck();
    CommandParser commandParser = new CommandParser();
    public static ArrayList<ArrayList<String>> collectionOfMultipleFiles = new ArrayList<>();

    public void executeScript(String file, ObjectOutputStream out, ObjectInputStream in, Map<String, Boolean> additionalInput) throws IOException, ClassNotFoundException {
        CollectionCommandsInFile collectionCommandInFile = new CollectionCommandsInFile();

        try {
            ArrayList<String> fileCommands = collectionCommandInFile.readComands(file);
            collectionOfMultipleFiles.add(fileCommands); // Добавляем команды из файла
            
            for (String oldFile : FileStack.fileStack) {
                if (file.equals(oldFile)) {
                    System.out.println("команда execute_script не выполнена из-за рекурсии в ваших файлах, имя файла: " + file);
                    collectionOfMultipleFiles.clear();
                    return;
                }
            }
            FileStack.fileStack.add(file);

            while (!collectionOfMultipleFiles.isEmpty()) {
                ArrayList<String> currentFileCommands = collectionOfMultipleFiles.get(collectionOfMultipleFiles.size() - 1);

                while (!currentFileCommands.isEmpty()) {
                    String command = currentFileCommands.remove(0); // Удаляем первую команду
                    
                    String[] commandParts = commandParser.parseCommandName(command);
                    String commandName = commandParts[0];

                    if (commandName.equals("execute_script")) {
                        String scriptFileName = commandParts[1];

                        if (FileStack.fileStack.contains(scriptFileName)) { 
                            System.out.println("команда execute_script не выполнена из-за рекурсии в ваших файлах, имя файла: " + scriptFileName);
                            continue; // Пропускаем этот файл, но продолжаем выполнение
                        }

                        // Читаем команды нового файла и добавляем в список
                        ArrayList<String> newFileCommands = collectionCommandInFile.readComands(scriptFileName);
                        collectionOfMultipleFiles.add(newFileCommands);
                        FileStack.fileStack.add(scriptFileName); // Запоминаем, что этот файл уже выполняется
                        // Прерываем обработку текущего файла, чтобы обработать новый
                        break;
                    }
                    
                    if (commandName.equals("exit")) {

                        // Отправляем exit на сервер
                        out.writeObject(new Object[]{"exit", new String[]{}});
                        out.flush();

                        Object response = in.readObject();
                        if (response instanceof String) {
                            System.out.println("Ответ от сервера: " + response);
                        }
                        // Завершаем только процесс клиента, потоки ОС не трогаем
                        System.exit(0);
                    }

                    String[] arg;
                    boolean needsAdditionalInput = additionalInput.getOrDefault(commandName, false);
                    boolean hasArgument = commandParts.length > 1;

                    if (needsAdditionalInput && hasArgument) {
                        String id = commandParts[1];
                        String[] movieData = checkInputFile.checkInput();
                        String[] argId = new String[]{id};
                        arg = new String[argId.length + movieData.length];
                    } else if (needsAdditionalInput) {
                        String[] movieData = checkInputFile.checkInput();
                        arg = movieData;
                    } else if (hasArgument) {
                        arg = new String[]{commandParts[1]};
                    } else {
                        arg = new String[]{};
                    }

                    try {
                        out.writeObject(new Object[]{commandName, arg});
                        out.flush();

                        String response = (String) in.readObject();
                        System.out.println("Ответ от сервера: " + response);
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                if(collectionOfMultipleFiles.size()>0 && collectionOfMultipleFiles.get(collectionOfMultipleFiles.size()-1).size() == 0){
		    		collectionOfMultipleFiles.remove(collectionOfMultipleFiles.size()-1);
		    	}
            }
        } catch (RuntimeException e) {
            System.out.println("Ошибка: файл не найден");
        }
    } 
}