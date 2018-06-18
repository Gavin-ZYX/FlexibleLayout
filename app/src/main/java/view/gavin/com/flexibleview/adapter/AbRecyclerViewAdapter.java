package view.gavin.com.flexibleview.adapter;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Created by dazhuanjia_rx on 16/9/8.
 */
public abstract class AbRecyclerViewAdapter extends RecyclerView.Adapter {

    protected boolean isLoading = false; //是否在加载更多
    protected boolean mLoadMoreEnable = false; //加载更多功能是否打开
    private int lastVisibleItem;

    private void attachRecyclerView(RecyclerView recyclerView) {
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(
                    new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                            lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                        }

                        @Override
                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                            if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) { //正在刷新时不加载更多
                                return;
                            }
                            if (newState == RecyclerView.SCROLL_STATE_IDLE
                                    && lastVisibleItem + 1 == getItemCount() && !isLoading && mLoadMoreEnable && canLoadMore()) {
                                isLoading = true;
                            }
                        }
                    }
            );
        } else if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            final StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int[] into = new int[staggeredGridLayoutManager.getSpanCount()];
                    (staggeredGridLayoutManager).findLastVisibleItemPositions(into);
                    lastVisibleItem = findMax(into);

                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) { //正在刷新时不加载更多
                        return;
                    }
                    if (newState == RecyclerView.SCROLL_STATE_IDLE
                            && lastVisibleItem + 1 == getItemCount() && !isLoading && mLoadMoreEnable && canLoadMore()) {
                        isLoading = true;
                    }
                }
            });
        }
    }
    /**
     * 获取数组中的最大值
     *
     * @param lastPositions 需要找到最大值的数组
     * @return 数组中的最大值
     */
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    protected boolean canLoadMore() {
        return true;
    }

    /**
     * 是否正在加载更多
     *
     * @return
     */
    public boolean isLoadMoring() {
        return isLoading;
    }

    /**
     * @param mLoadMoreEnable 是否可以加载更多
     */
    public void enableLoadMore(boolean mLoadMoreEnable) {
        this.mLoadMoreEnable = mLoadMoreEnable;
    }

    protected SwipeRefreshLayout swipeRefreshLayout;

    public void attachSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    /**
     * 是否在刷新或加载更多
     *
     * @return
     */
    public boolean isRefreshOrLoadMoring() {
        return isLoadMoring() || isRefreshing();
    }

    /**
     * 是否在刷新
     *
     * @return
     */
    public boolean isRefreshing() {
        return swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing();
    }

    /**
     * 刷新完成
     */
    public void refreshComplete() {
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
