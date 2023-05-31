package com.example.internet.request;

import com.example.internet.fragment.TimelineFragment;
import com.example.internet.util.Global;

import okhttp3.Callback;

public class GetMomentRequest extends BaseRequest{

    String getNewestUrl = Global.API_URL + "/moment/get_new_moment/";

    String getHottestUrl = Global.API_URL + "/moment/get_hot_moment/";

    String getFollowingUrl = Global.API_URL + "/moment/get_followings_moment/";
    String getStarUrl = Global.API_URL + "/moment/get_star_moment/";

    String getTagUrl = Global.API_URL + "/moment/get_tag_moment/";

    String getPersonUrl = Global.API_URL + "/moment/get_moment/";


    // NEWEST, HOTTEST, STARRED
    public GetMomentRequest(Callback saveCallback, int pageAttr, String jwt, int lastId){
        super();
        try{
            String attachStr = lastId != -1 ? String.valueOf(lastId) + "/": "";
            if (pageAttr == TimelineFragment.NEWEST_PAGE){
                getNewestUrl += attachStr;
                BaseRequest saveRequest = new BaseRequest();
                saveRequest.get(getNewestUrl, saveCallback, jwt);
            }
            else if (pageAttr == TimelineFragment.HOTTEST_PAGE){
                getHottestUrl += attachStr;
                BaseRequest saveRequest = new BaseRequest();
                saveRequest.get(getHottestUrl, saveCallback, jwt);
            }
            else if (pageAttr == TimelineFragment.STARRED_PAGE){
                getStarUrl += attachStr;
                BaseRequest saveRequest = new BaseRequest();
                saveRequest.get(getStarUrl, saveCallback, jwt);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    // FOLLOWING
    public GetMomentRequest(Callback saveCallback, int pageAttr, String filter, String jwt, int lastId){
        super();
        try {
            String attachStr = lastId != -1 ? String.valueOf(lastId) + "/": "";
            if (pageAttr == TimelineFragment.FOLLOWINGS_PAGE){
                getFollowingUrl += filter + "/" + attachStr;
                BaseRequest saveRequest = new BaseRequest();
                saveRequest.get(getFollowingUrl, saveCallback, jwt);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    // TAGGED
    public GetMomentRequest(Callback saveCallback, int pageAttr, String tag, String filter, String jwt, int lastId){
        super();
        try {
            String attachStr = lastId != -1 ? String.valueOf(lastId) + "/": "";
            if (pageAttr == TimelineFragment.TAGGED_PAGE){
                getTagUrl += tag + "/" +  filter + "/" + attachStr;
                BaseRequest saveRequest = new BaseRequest();
                saveRequest.get(getTagUrl, saveCallback, jwt);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    // PERSONAL
    public GetMomentRequest(Callback saveCallback, String username, String jwt, int lastId){
        super();
        try {
            String attachStr = lastId != -1 ? String.valueOf(lastId) + "/": "";
            getPersonUrl += username + "/" + attachStr;
            BaseRequest saveRequest = new BaseRequest();
            saveRequest.get(getPersonUrl, saveCallback, jwt);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
