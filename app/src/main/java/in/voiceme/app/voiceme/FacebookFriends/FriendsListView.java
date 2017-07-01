package in.voiceme.app.voiceme.FacebookFriends;

import java.util.ArrayList;

/**
 * Created by Sally on 01-Jan-17.
 */

public interface FriendsListView {
    void initializeView();

    void loadFriendsList(ArrayList<FriendItemData> friendsList);

    void showError();
}
