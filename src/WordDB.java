import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class WordDB {
    private HashMap<String, String[]> wordlist;

    private ArrayList<String> categories;

    public WordDB() {
        try {
            wordlist = new HashMap<>();
            categories = new ArrayList<>();

            String filePath = getClass().getClassLoader().getResource(CommonConstants.DATA_PATH).getPath();
            if (filePath.contains("%20")) filePath = filePath.replaceAll("%20", " ");
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                String category = parts[0];
                categories.add(category);

                String values[] = Arrays.copyOfRange(parts, 1, parts.length);
                wordlist.put(category, values);
            }
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    public String[] loadChallenge() {
        Random rand = new Random();

        String catrgory = categories.get(rand.nextInt(categories.size()));

        String[] categoryValues = wordlist.get(catrgory);
        String word = categoryValues[rand.nextInt(categoryValues.length)];

        return new String[]{catrgory.toUpperCase(), word.toUpperCase()};
    }
}
