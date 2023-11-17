import java.io.BufferedReader;
import java.io.FileReader;
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
            Map<String, CategoryStats> categorised = new HashMap<>();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                totalLines++;
                if (totalLines > 1) {
                    String[] fields = line.trim().split(",");
                    try {
                        App app = new App(fields[COL_NAME], fields[COL_CAT].toUpperCase(),
                                Double.parseDouble(fields[COL_RATING]));

                        String category = app.getCategory();
                        CategoryStats stats = categorised.get(category);
                        if (stats == null) {
                            stats = new CategoryStats();
                            categorised.put(category, stats);
                        }

                        stats.addApp(app);
                    } catch (NumberFormatException e) {
                        CategoryStats stats = categorised.get(fields[COL_CAT]);
                        if (stats == null) {
                            stats = new CategoryStats();
                            categorised.put(fields[COL_CAT], stats);
                        }

                        stats.incrementDiscardedCount();
                    }
                }
            }

            for (String category : categorised.keySet()) {
                System.out.println("Category: " + category);

                CategoryStats stats = categorised.get(category);
                List<App> apps = stats.getApps();

                App highest = getHighestApp(apps);
                App lowest = getLowestApp(apps);
                double average = averageRating(apps);

                System.out.println("Highest: " + highest.getName() + " " + highest.getRating());
                System.out.println("Lowest: " + lowest.getName() + " " + lowest.getRating());
                System.out.println("Average: " + average);
                System.out.println("Count: " + apps.size());

                int discarded = stats.getDiscardedCount();
                System.out.println("Discarded: " + discarded);

                System.out.println();
            }

            System.out.println("Total lines in file: " + totalLines);
        }
    }

    public static App getHighestApp(List<App> apps) {
        App highestApp = null;
        for (App app : apps) {
            if (!Double.isNaN(app.getRating())) {
                if (highestApp == null || app.getRating() > highestApp.getRating()) {
                    highestApp = app;
                }
            }
        }
        return highestApp;
    }

    public static App getLowestApp(List<App> apps) {
        App lowestApp = null;
        for (App app : apps) {
            if (!Double.isNaN(app.getRating())) {
                if (lowestApp == null || app.getRating() < lowestApp.getRating()) {
                    lowestApp = app;
                }
            }
        }
        return lowestApp;
    }

    public static double averageRating(List<App> apps) {
        double sum = 0;
        int count = 0;
        for (App app : apps) {
            double rating = app.getRating();

            if (!Double.isNaN(rating)) {
                sum += rating;
                count++;
            }
        }

        double average;
        average = sum / count;
        return average;
    }
}