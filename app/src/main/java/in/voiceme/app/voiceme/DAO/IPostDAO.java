package in.voiceme.app.voiceme.DAO;

import java.util.List;

import in.voiceme.app.voiceme.DTO.PostsModel;

/**
 * Created by harish on 2/5/2017.
 */

public interface IPostDAO {

    List<PostsModel> getAllPost(String follower, String user_id, int page);
}
