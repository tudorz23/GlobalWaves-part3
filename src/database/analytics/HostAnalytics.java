package database.analytics;

import java.util.Map;
import java.util.TreeMap;

public class HostAnalytics extends Analytics {
    private Map<String, Integer> hostTopEpisodes;
    private Map<String, Integer> hostTopFans;

    /* Constructor */
    public HostAnalytics() {
        hostTopEpisodes = new TreeMap<>();
        hostTopFans = new TreeMap<>();
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
    public void setHostTopEpisodes(Map<String, Integer> hostTopEpisodes) {
        this.hostTopEpisodes = hostTopEpisodes;
    }
    public Map<String, Integer> getHostTopFans() {
        return hostTopFans;
    }
    public void setHostTopFans(Map<String, Integer> hostTopFans) {
        this.hostTopFans = hostTopFans;
    }
}
