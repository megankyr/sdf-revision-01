import java.util.ArrayList;
import java.util.List;

class CategoryStats {
    private List<App> apps = new ArrayList<>();
    private int discardedCount;

    public List<App> getApps() {
        return apps;
    }

    public void addApp(App app) {
        if (Double.isNaN(app.getRating())) {
            discardedCount++;
        } else {
            apps.add(app);
        }
    }

    public int getDiscardedCount() {
        return discardedCount;
    }

    public void incrementDiscardedCount() {
        discardedCount++;
    }
}