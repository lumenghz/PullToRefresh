package lumenghz.com.pulllaunchrocket.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import lumenghz.com.pulllaunchrocket.R;
import lumenghz.com.pullrefresh.PullToRefreshView;

/**
 * @author lumeng on 2016-06-17.
 *         jiahehz@gmail.com
 */
public class RecyclerViewFragment extends BaseFragment {

    @BindView(R.id.pull_to_refresh)
    PullToRefreshView mPullToRefresh;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecylerView();
        initRefreshView();
    }

    private void initRecylerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new SampleAdaper());
    }

    private void initRefreshView() {
        mPullToRefresh.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefresh.setRefreshing(false);
                    }
                }, REFRESH_DELAY);
            }
        });
    }

    class SampleAdaper extends RecyclerView.Adapter<SampleHolder> {
        @Override
        public SampleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new SampleHolder(view);
        }

        @Override
        public void onBindViewHolder(SampleHolder holder, int position) {
            Map<String, Integer> data = mSampleDatas.get(position);
            holder.bindData(data);
        }

        @Override
        public int getItemCount() {
            return mSampleDatas.size();
        }
    }

    class SampleHolder extends RecyclerView.ViewHolder {
        private View mRootView;
        private ImageView iconView;

        private Map<String, Integer> mData;

        public SampleHolder(View itemView) {
            super(itemView);
            mRootView = itemView;
            iconView = (ImageView) itemView.findViewById(R.id.image_view_icon);
        }

        public void bindData(Map<String, Integer> data) {
            mData = data;

            mRootView.setBackgroundResource(mData.get(COLOR));
            iconView.setImageResource(mData.get(ICON));
        }
    }
}
