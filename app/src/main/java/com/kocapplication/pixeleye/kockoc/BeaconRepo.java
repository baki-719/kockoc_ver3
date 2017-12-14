package com.kocapplication.pixeleye.kockoc;

import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Pixeleye_server on 2017-11-15.
 */

// http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailCommon?ServiceKey=%2FPY%2FrC05SWjkibjwVdcdM9oEFIigSw3ePTOzJRtbCB3Li5gdZVTDZkYG5A1U4fah%2BvyfFw%2BGIGkeE3gpg3EaPQ%3D%3D&contentTypeId=12&contentId=126508&MobileOS=ETC&MobileApp=TourAPI3.0_Guide&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y&transGuideYN=Y
// serviceKey : %2FPY%2FrC05SWjkibjwVdcdM9oEFIigSw3ePTOzJRtbCB3Li5gdZVTDZkYG5A1U4fah%2BvyfFw%2BGIGkeE3gpg3EaPQ%3D%3D
public class BeaconRepo {

    @SerializedName("response")
    response response;

    public class response {
        @SerializedName("header")
        header header;
        @SerializedName("body")
        body body;

        public class header {
            @SerializedName("resultCode") String resultCode;
            @SerializedName("resultMsg") String resultMsg;

            public String getResultCode() {
                return resultCode;
            }
            public String getResultMsg() {
                return resultMsg;
            }
        }
        public class body {
            @SerializedName("items") items items;
            @SerializedName("numOfRows") String numOfRows;
            @SerializedName("pageNo") String pageNo;
            @SerializedName("totalCount") String totalCount;

            public class items {
                @SerializedName("item") item item;

                public class item {
                    @SerializedName("title") String title;
                    @SerializedName("firstimage") String firstImage;
                    @SerializedName("firstimage2") String firstImage2;
                    @SerializedName("overview") String overview;

                    public String getTitle() {
                        return title;
                    }
                    public String getFirstImage() {
                        return firstImage;
                    }
                    public String getFirstImage2() {
                        return firstImage2;
                    }
                    public String getOverview() {
                        return overview;
                    }
                }

                public item getItem(){return item;}
            }

            public BeaconRepo.response.body.items getItems() {
                return items;
            }

            public String getNumOfRows() {
                return numOfRows;
            }

            public String getPageNo() {
                return pageNo;
            }

            public String getTotalCount() {
                return totalCount;
            }
        }

        public BeaconRepo.response.header getHeader() {
            return header;
        }

        public BeaconRepo.response.body getBody() {
            return body;
        }
    }

    public BeaconRepo.response getResponse() {
        return response;
    }

    public interface TourApiInterface{
        @GET("{url}")
        Call<BeaconRepo> getBeaconRepo(@Path("url") String url);
    }
}
