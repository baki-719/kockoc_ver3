package com.kocapplication.pixeleye.kockoc.util.connect;

/**
 * Created by Han_ on 2016-06-21.
 */
public class BasicValue {
    private static BasicValue ourInstance = new BasicValue();
    private String urlHead;
    private final String DAUM_MAP_API_KEY = "47078ec18861a1616a43a513024eef81"; //fd87e70c9d3984b8efea777c78112f1e daum
    private final String DAUM_MAP_APP_KEY = "9b6bb8886ea6b7a8ae3e05ff6b989ea6"; //9b6bb8886ea6b7a8ae3e05ff6b989ea6 kakao
                                                                                //47078ec18861a1616a43a513024eef81 kakao rest api
    private boolean isRealServer = true; //서버 쉬프트 true : 레알서버
                                         //            false : 테썹
    private int userNo = -1;
    private String userNickname = "";
    private String userName = "";

    public static BasicValue getInstance() {
        return ourInstance;
    }

    private BasicValue() {
        if(isRealServer){
            urlHead = "http://115.68.14.27:8080/";
        } else {
            urlHead = "http://221.160.54.160:8080/";
        }
    }

    public String getUrlHead() {
        return urlHead;
    }
    public int getUserNo() {
        return userNo;
    }

    public String getDAUM_MAP_API_KEY() {return DAUM_MAP_API_KEY;}

    public String getDAUM_MAP_APP_KEY() {
        return DAUM_MAP_APP_KEY;
    }
    public String getUserNickname() {return userNickname;}
    public String getUserName() {return userName;}


    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }
    public void setUserNickname(String userNickname) {this.userNickname = userNickname;}
    public void setUserName(String userName){this.userName = userName;}

}
