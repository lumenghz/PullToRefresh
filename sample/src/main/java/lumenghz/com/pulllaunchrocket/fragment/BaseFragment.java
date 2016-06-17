package lumenghz.com.pulllaunchrocket.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lumenghz.com.pulllaunchrocket.R;

/**
 * @author lumeng on 2016-06-17.
 *         jiahehz@gmail.com
 */
abstract class BaseFragment extends Fragment {
    public static final int REFRESH_DELAY = 2000;

    protected static final String ICON = "icon";
    protected static final String COLOR = "color";

    protected List<Map<String, Integer>> mSampleDatas;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Map<String, Integer> mMap;
        mSampleDatas = new ArrayList<>();

        int[] icons = {
                R.drawable.airplane,
                R.drawable.big_ben,
                R.drawable.bridge,
                R.drawable.flower
        };

        int[] colors = {
                R.color.airplane,
                R.color.big_ben,
                R.color.bridge,
                R.color.flower
        };

        for (int i = 0; i < icons.length; i++) {
            mMap = new HashMap<>();
            mMap.put(ICON, icons[i]);
            mMap.put(COLOR, colors[i]);
            mSampleDatas.add(mMap);
        }
    }
}
