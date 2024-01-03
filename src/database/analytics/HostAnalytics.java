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


    /**
     * Adds an episode (i.e. its name as String) to Host's topEpisodes map.
     */
    public void addEpisode(final String episodeName) {
        int listens = hostTopEpisodes.getOrDefault(episodeName, 0);
        hostTopEpisodes.put(episodeName, listens + 1);
    }


    /**
     * Adds a fan (i.e. a user's name as String) to Host's topFans map.
     */
    public void addFan(final String fanName) {
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
