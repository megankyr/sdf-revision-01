import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static final int COL_NAME = 0;
    public static final int COL_CAT = 1;
    public static final int COL_RATING = 2;

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Please specify the CSV file name. Thank you");
            return;
        }

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(args[0]))) {
            int totalLines = 0;
            Map<String, List<App>> categorised = new HashMap<>();
            Map<String, Integer> categoryDiscardedCount = new HashMap<>();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                totalLines++;
                if (totalLines > 1) {
                    String[] fields = line.trim().split(",");
                    try {
                        App app = new App(fields[COL_NAME], (fields[COL_CAT]).toUpperCase(),
                                Double.parseDouble(fields[COL_RATING]));

                        String category = app.getCategory();
                        List<App> categorisedApps = categorised.get(category);
                        if (categorisedApps == null) {
                            categorisedApps = new ArrayList<>();
                            categorised.put(category, categorisedApps);
                        }
                        categorisedApps.add(app);
                    } catch (NumberFormatException e) {
                        String category = fields[COL_CAT];
                        Integer discardedCount = categoryDiscardedCount.get(category);
                        if (discardedCount == null) {
                            categoryDiscardedCount.put(category, 1);
                        } else {
                            categoryDiscardedCount.put(category, discardedCount + 1);
                        }
                        continue;
                    }
                }
            }

            for (String category : categorised.keySet()) {
                System.out.println("Category: " + category);

                List<App> apps = categorised.get(category);
                App highest = getHighestApp(apps);
                App lowest = getLowestApp(apps);
                double average = averageRating(apps);

                System.out.println("Highest: " + highest.getName() + " " + highest.getRating());
                System.out.println("Lowest: " + lowest.getName() + " " + lowest.getRating());
                System.out.println("Average: " +  average);
                System.out.println("Count: " +  apps.size());

                int discarded = categoryDiscardedCount.getOrDefault(category, 0);
                System.out.println("Discarded: " + discarded);

                System.out.println();
            }

            System.out.println("Total lines in file: " + totalLines);
        }
    }

    public static App getHighestApp(List<App> apps) {
        if (apps.isEmpty()){
            return null;
        }
        App highestApp = apps.get(0);
        for (App app : apps) {
            if (app.getRating() > highestApp.getRating()) {
                highestApp = app;
            }
        }
        return highestApp;
    }

    public static App getLowestApp(List<App> apps) {
        if (apps.isEmpty()){
            return null;
        }
        App lowestApp = apps.get(0);
        for (App app : apps) {
            if (app.getRating() < lowestApp.getRating()) {
                lowestApp = app;
            }
        }
        return lowestApp;
    }

    public static double averageRating(List<App> apps) {

        if (apps.isEmpty()){
            return 0.0;
        }
        double sum = 0;
        for (App app : apps) {
            sum += app.getRating();
        }
        double average = sum / apps.size();
        return average;
    }
}
