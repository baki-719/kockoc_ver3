package com.kocapplication.pixeleye.kockoc.main;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.kocapplication.pixeleye.kockoc.R;
import com.kocapplication.pixeleye.kockoc.detail.DetailActivity;
import com.kocapplication.pixeleye.kockoc.main.course.CourseFragment;
import com.kocapplication.pixeleye.kockoc.main.main.MainFragment;
import com.kocapplication.pixeleye.kockoc.main.myKockoc.MyKocKocFragment;
import com.kocapplication.pixeleye.kockoc.main.story.StoryFragment;
import com.kocapplication.pixeleye.kockoc.main.tour.TourFragment;

import java.util.ArrayList;
import java.util.List;

//import com.google.android.gms.common.GooglePlayServicesUtil;

public class MainActivity extends BaseActivity {
    private final String TAG = "MainActivity";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final int DETAIL_ACTIVITY_REQUEST_CODE = 1222;
    public static final int COURSE_WRITE_ACTIVITY_REQUEST_CODE = 575;
    public static final int NEW_WRITE_REQUEST_CODE = 12433;
    public static final int CONTINUOUS_WRITE_REQUEST_CODE = 1234;
    private final int PROFILE_SET = 1;

    private ViewPageAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private MainFragment mainFragment;
    private StoryFragment storyFragment;
    private CourseFragment courseFragment;
    private MyKocKocFragment myKocKocFragment;
    private TourFragment tourFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        init();
        actionBarTitleSet();

        ImageView logo = (ImageView) findViewById(R.id.actionbar_image_title);

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);

            }
        });
    }

    @Override
    protected void init() {
        super.init();
        mainFragment = new MainFragment();
        storyFragment = new StoryFragment();
        courseFragment = new CourseFragment();
        myKocKocFragment = new MyKocKocFragment();
        tourFragment = new TourFragment();

        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(mainFragment);
        fragments.add(storyFragment);
        fragments.add(tourFragment);
        fragments.add(courseFragment);
        fragments.add(myKocKocFragment);

        List<String> titles = new ArrayList<String>();
        titles.add("소식");
        titles.add("여행후기");
        titles.add("관광");
        titles.add("코스");
        titles.add("내콕콕");

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        adapter = new ViewPageAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
        tabLayout.setupWithViewPager(viewPager);

        startService(new Intent(MainActivity.this, com.kocapplication.pixeleye.kockoc.background.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private class ViewPageAdapter extends FragmentPagerAdapter {
        private List<Fragment> items;
        private List<String> titles;

        public ViewPageAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
            super(fm);
            if (fragments == null) throw new IllegalArgumentException("Data cMust Not be Null");
            this.items = fragments;
            this.titles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return items.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        @Override
        public int getCount() {
            return items.size();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * set_navProfile
     * myKocKocFragment의 핸들러에서 데이터를 받아 BaseActivity에 set
     */
    public void set_navProfileImg() {
        super.set_navProfileImg();
    } // 프사 변경 할때

    public void set_navProfileName(String name) {
        super.set_navProfileName(name);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case DETAIL_ACTIVITY_REQUEST_CODE:
                if(data != null)   // 글 삭제 누를 시 호출
                    storyFragment.deleteItem(data.getIntExtra("position", -1));
                break;
            case NEW_WRITE_REQUEST_CODE:
                storyFragment.refresh();
                detail_intent(data);
                break;
            case COURSE_WRITE_ACTIVITY_REQUEST_CODE:
                try {courseFragment.refresh();}catch (ClassCastException e){Log.e(TAG,e.getMessage());}
                break;
            case CONTINUOUS_WRITE_REQUEST_CODE:
                storyFragment.refresh();
                detail_intent(data);
                break;
            default:
                break;
        }
    }

    void detail_intent(Intent data) {
        if (data == null) return;

        int boardNo = data.getIntExtra("boardNo", 0);
        int courseNo = data.getIntExtra("courseNo", 0);
        int userNo = data.getIntExtra("board_userNo", 0);

        if (boardNo == 0 || courseNo == 0 || userNo == 0) return;

        Intent detail_intent = new Intent(MainActivity.this, DetailActivity.class);
        detail_intent.putExtra("boardNo", boardNo);
        detail_intent.putExtra("courseNo", courseNo);
        detail_intent.putExtra("board_userNo", userNo);
        startActivity(detail_intent);
    }



    public MainFragment getMainFragment() {
        return mainFragment;
    }
}