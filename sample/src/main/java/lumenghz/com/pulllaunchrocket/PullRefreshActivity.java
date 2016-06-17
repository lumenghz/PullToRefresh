package lumenghz.com.pulllaunchrocket;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TableLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import lumenghz.com.pulllaunchrocket.fragment.ListViewFragment;
import lumenghz.com.pulllaunchrocket.fragment.RecyclerViewFragment;

public class PullRefreshActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar   toolbar;
    @BindView(R.id.pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pullrefresh);
        ButterKnife.bind(this);

        initToolbar();
        initViewpager();
    }

    private void initToolbar() {
        if (null != toolbar)
            setSupportActionBar(toolbar);
    }

    private void initViewpager() {
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ListViewFragment();
                case 1:
                default:
                    return new RecyclerViewFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "listview";
                case 1:
                    default:
                        return "recyclerview";
            }
        }
    }
}
