import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
	public static void saveObject(String filePath, Object object) {
		FileOutputStream fileOutputStream;
		ObjectOutputStream objectOutputStream;
		try {
			File file = new File(filePath);
			file.createNewFile();
			fileOutputStream = new FileOutputStream(filePath);
			objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(object);
			objectOutputStream.close();
		} catch (Exception e) {
		}
	}

	public static Object loadObject(String filePath) {
		Object object = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(filePath);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			object = objectInputStream.readObject();
			objectInputStream.close();
		} catch (ClassNotFoundException | IOException e) {
		}
		return object;
	}

	public static List<String> loadLines(String filePath) throws IOException {
		InputStreamReader fileReader = new InputStreamReader(new FileInputStream(filePath), "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> lines = new ArrayList<String>();
        String line = null;
         
        while ((line = bufferedReader.readLine()) != null) 
        {
            lines.add(line);
        }
         
        bufferedReader.close();
         
        return lines;
	}
}
