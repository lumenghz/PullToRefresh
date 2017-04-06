package lumenghz.com.pulllaunchrocket.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import lumenghz.com.pulllaunchrocket.R;
import lumenghz.com.pulllaunchrocket.custom.SunRefreshView;
import lumenghz.com.pullrefresh.PullToRefreshView;

/**
 * @author lumeng on 2016-06-17.
 *         jiahehz@gmail.com
 */
public class ListViewFragment extends BaseFragment implements PullToRefreshView.OnRefreshListener {

    @BindView(R.id.list_view)
    ListView             mListView;
    @BindView(R.id.pull_to_refresh)
    PullToRefreshView    mPullRefreshView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listview, container, false);
        ButterKnife.bind(this, view);

        SunRefreshView sunRefreshView = new SunRefreshView(mPullRefreshView);
        mPullRefreshView.setRefreshView(sunRefreshView);
        mPullRefreshView.setTotalDragDistance(getContext(), 140);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPullRefreshView.setOnRefreshListener(this);
        mListView.setAdapter(new SampleAdapter(getActivity(), R.layout.list_item, mSampleDatas));
    }

    @Override
    public void onRefresh() {
        mPullRefreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPullRefreshView.setRefreshing(false);
            }
        }, REFRESH_DELAY);
    }

    class SampleAdapter extends ArrayAdapter<Map<String, Integer>> {

        public static final String KEY_ICON = "icon";
        public static final String KEY_COLOR = "color";

        private final LayoutInflater mInflater;
        private final List<Map<String, Integer>> mDatas;

        public SampleAdapter(Context context, int layoutResourceId, List<Map<String, Integer>> mDatas) {
            super(context, layoutResourceId, mDatas);
            this.mDatas = mDatas;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.list_item, parent, false);
                viewHolder.icon = (ImageView) convertView.findViewById(R.id.image_view_icon);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.icon.setImageResource(mDatas.get(position).get(KEY_ICON));
            convertView.setBackgroundResource(mDatas.get(position).get(KEY_COLOR));

            return convertView;
        }

        class ViewHolder {
            ImageView icon;
        }
    }
}
