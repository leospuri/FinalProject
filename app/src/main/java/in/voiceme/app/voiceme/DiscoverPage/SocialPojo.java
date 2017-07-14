package in.voiceme.app.voiceme.DiscoverPage;

/**
 * Created by harishpc on 7/14/2017.
 */
public class SocialPojo {
    private String social_network;
    private int social_network_id;

    public int getSocial_network_id() {
        return social_network_id;
    }

    public String getSocial_network() {
        return social_network;
    }

    public SocialPojo(int social_network_id, String social_network) {
        this.social_network_id = social_network_id;
        this.social_network = social_network;
    }
}
