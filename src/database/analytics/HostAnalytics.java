package database.analytics;

import java.util.HashMap;
import java.util.Map;

public final class HostAnalytics extends ContentCreatorAnalytics {
    private Map<String, Integer> hostTopEpisodes;
    private Map<String, Integer> hostTopFans;

    /* Constructor */
    public HostAnalytics() {
        hostTopEpisodes = new HashMap<>();
        hostTopFans = new HashMap<>();
    }

    public void addEpisode(String episodeName) {
        int listens = hostTopEpisodes.getOrDefault(episodeName, 0);
        hostTopEpisodes.put(episodeName, listens + 1);
    }

    public void addFan(String fanName) {
        int listens = hostTopFans.getOrDefault(fanName, 0);
        hostTopFans.put(fanName, listens + 1);
    }

    /* Getters and Setters */
    public Map<String, Integer> getHostTopEpisodes() {
        return hostTopEpisodes;
    }
    public Map<String, Integer> getHostTopFans() {
        return hostTopFans;
    }
}
