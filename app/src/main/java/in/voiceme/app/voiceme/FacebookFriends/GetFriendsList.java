package in.voiceme.app.voiceme.FacebookFriends;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Sally on 08-Jan-17.
 */

public class GetFriendsList {

   /* public void getTaggableFriends(AccessToken fbToken, GraphRequest.Callback graphRequestCallback) {
        //fbToken return from login with facebook
        GraphRequestAsyncTask r = GraphRequest.newGraphPathRequest(fbToken,
                "/me/taggable_friends", graphRequestCallback
        ).executeAsync();
    }*/

    public void getFBFriendsList(String userId, String accessToken, int limit, String afterPage, Callback<FriendsListResponse> friendsListCallback) {
        FacebookFriendList facebookListService = ApiService.getService().create(FacebookFriendList.class);
        Call<FriendsListResponse> call = facebookListService.getFriendsList(accessToken);
        call.enqueue(friendsListCallback);
    }
}
