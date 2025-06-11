package client.ExecuteScript;

import java.util.ArrayList;

import client.SupRead.FileDataInput;

public class CollectionCommandsInFile {

	 ArrayList<String> collectionComandsInFile = new ArrayList<>();

	public  ArrayList<String> readComands(String file){
		try (FileDataInput fileReader = new FileDataInput("Files/" + file)) {
            String line;
            while ((line = fileReader.input()) != null) {
            	collectionComandsInFile.add(line);
            }
        }
		
		return collectionComandsInFile;
		
	}
	
}