package view.gavin.com.flexibleview.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by dazhuanjia_rx on 16/8/31.
 */
public abstract class BaseRecyclerViewAdapter<T> extends AbRecyclerViewAdapter {
    public Context context;
    public List<T> list;

    private static final int HEADER_VIEW = 0x00000111;
    private static final int LOADING_VIEW = 0x00000222;
    private static final int FOOTER_VIEW = 0x00000333;
    private static final int ITEM_VIEW = 0x00000444;

    // 缓存对应 viewType 的 item
    private HashMap<Integer, MoreItemHelper> moreItemHelperSparseArray = new HashMap<>();

    public BaseRecyclerViewAdapter(Context context, @NonNull List<T> list) {
        this.context = context;
        this.list = list;
    }

    /**
     * 在构造方法里调用
     *
     * @param moreItemHelper
     */
    protected void addMoreItemHelper(MoreItemHelper moreItemHelper) {
        moreItemHelperSparseArray.put(moreItemHelper.getItemType(), moreItemHelper);
    }

    @Override
    final public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case HEADER_VIEW:
                return new HeadViewHolder(mHeaderLayout);
            case FOOTER_VIEW:
                return new FootViewHolder(mFooterLayout);
            default:
                if (moreItemHelperSparseArray.size() != 0) {
                    MoreItemHelper moreItemHelper = moreItemHelperSparseArray.get(viewType);
                    return moreItemHelper.createViewHolder(context, parent);
                } else {
                    view = LayoutInflater.from(context).inflate(getLayoutId(), parent, false);
                    return getViewHolder(view);
                }
        }
    }

    @LayoutRes
    protected abstract int getLayoutId();

    @NonNull
    protected abstract RecyclerView.ViewHolder getViewHolder(View view);

    @Override
    final public int getItemCount() {
        int listSize = getItemSize();
        return listSize + getHeaderLayoutCount() + getFooterLayoutCount() + getLoadMoreViewCount();
    }

    /**
     * 返回item个数 除了head footer loadMoreView
     * 子类可重写
     *
     * @return
     */
    public int getItemSize() {
        if (moreItemHelperSparseArray.size() != 0) {
            int count = 0;
            Set<Integer> integers = moreItemHelperSparseArray.keySet();
            for (Integer viewType : integers) {
                MoreItemHelper moreItemHelper = moreItemHelperSparseArray.get(viewType);
                count = count + moreItemHelper.getItemCount();
            }
            return count;
        }
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        int numHeaders = getHeaderLayoutCount();
        if (position < numHeaders) {
            return HEADER_VIEW;
        } else {
            int adjPosition = position - numHeaders;
            int adapterCount = getItemSize();
            if (adjPosition < adapterCount) {
                if (moreItemHelperSparseArray.size() != 0) {
                    return getMoreItemHelperViewType(adjPosition);
                } else {
                    return ITEM_VIEW;
                }
            } else {
                adjPosition = adjPosition - adapterCount;
                int numFooters = getFooterLayoutCount();
                if (adjPosition < numFooters) {
                    return FOOTER_VIEW;
                } else {
                    return LOADING_VIEW;
                }
            }
        }
    }

    /**
     * 返回对应 position 的viewType
     *
     * @param position
     * @return
     */
    protected int getMoreItemHelperViewType(int position) {
        return 0;
    }

    @Override
    final public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        switch (viewType) {
            case 0:
                onBindView(holder, holder.getLayoutPosition() - getHeaderLayoutCount());
                break;
            case LOADING_VIEW:
                setLoadMoreView((LoadMoreViewHolder) holder);
                break;
            case HEADER_VIEW:
                break;
            case FOOTER_VIEW:
                break;
            default:
                if (moreItemHelperSparseArray.size() != 0) {
                    moreItemHelperSparseArray.get(viewType).onBindView(holder, getRealPosition(viewType, holder.getLayoutPosition() - getHeaderLayoutCount()));
                } else {
                    onBindView(holder, holder.getLayoutPosition() - getHeaderLayoutCount());
                }
                break;
        }
    }

    /**
     * 多 item 布局需要重写该方法拿到对应的 position
     *
     * @param viewType
     * @param position
     * @return
     */
    protected int getRealPosition(int viewType, int position) {
        return position;
    }

    protected abstract void onBindView(RecyclerView.ViewHolder holder, int position);

    /**
     * @param offset
     * @param limit
     * @param newList
     * @return true 代表有数据 false代表无数据
     */
    public boolean updateList(int offset, int limit, List<T> newList) {
        if (offset == 0) {
            list.clear();
            notifyDataSetChanged();
            resetLoadMoreStatus();
        }
        if (newList != null && newList.size() != 0) {
            int size = list.size();
            list.addAll(newList);
            notifyItemRangeInserted(size + getHeaderLayoutCount(), newList.size());
            notifyItemRangeChanged(size + getHeaderLayoutCount(), newList.size());
            if (newList.size() < limit) {
                loadMoreEnd();
            } else {
                loadMoreComplete();
            }
            return true;
        } else { //没有新数据关闭加载更多
            loadMoreEnd();
            return list.size() != 0;
        }
    }

    /**
     * 移除item
     *
     * @param position
     * @return true: list empty ; false: list size more than 0
     */
    public boolean removeItem(int position) {
        if (list.size() > position) {
            list.remove(position);
            if (list.size() == 0) {
                notifyDataSetChanged();
                return true;
            } else {
                notifyItemRemoved(position);
                if (position != list.size()) {
                    notifyItemRangeChanged(position, list.size() - position);
                }
            }
        }
        return false;
    }

    //header footer
    private LinearLayout mHeaderLayout;
    private LinearLayout mFooterLayout;

    /**
     * Return root layout of header
     */
    public LinearLayout getHeaderLayout() {
        return mHeaderLayout;
    }

    /**
     * Return root layout of footer
     */
    public LinearLayout getFooterLayout() {
        return mFooterLayout;
    }

    /**
     * Append header to the rear of the mHeaderLayout.
     *
     * @param header
     */
    public int addHeaderView(View header) {
        return addHeaderView(header, -1);
    }

    /**
     * Add header view to mHeaderLayout and set header view position in mHeaderLayout.
     * When index = -1 or index >= child count in mHeaderLayout,
     * the effect of this method is the same as that of {@link #addHeaderView(View)}.
     *
     * @param header
     * @param index  the position in mHeaderLayout of this header.
     *               When index = -1 or index >= child count in mHeaderLayout,
     *               the effect of this method is the same as that of {@link #addHeaderView(View)}.
     */
    public int addHeaderView(View header, int index) {
        return addHeaderView(header, index, LinearLayout.VERTICAL);
    }

    /**
     * @param header
     * @param index
     * @param orientation
     */
    public int addHeaderView(View header, int index, int orientation) {
        if (mHeaderLayout == null) {
            mHeaderLayout = new LinearLayout(header.getContext());
            if (orientation == LinearLayout.VERTICAL) {
                mHeaderLayout.setOrientation(LinearLayout.VERTICAL);
                mHeaderLayout.setLayoutParams(new RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            } else {
                mHeaderLayout.setOrientation(LinearLayout.HORIZONTAL);
                mHeaderLayout.setLayoutParams(new RecyclerView.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
            }
        }
        final int childCount = mHeaderLayout.getChildCount();
        if (index < 0 || index > childCount) {
            index = childCount;
        }
        mHeaderLayout.addView(header, index);
        if (mHeaderLayout.getChildCount() == 1) {
            notifyItemInserted(0);
        }
        return index;
    }

    public void removeHeaderView(View header) {
        if (getHeaderLayoutCount() == 0) {
            return;
        }
        mHeaderLayout.removeView(header);
        if (mHeaderLayout.getChildCount() == 0) {
            notifyItemRemoved(0);
        }
    }

    /**
     * Append footer to the rear of the mFooterLayout.
     *
     * @param footer
     */
    public int addFooterView(View footer) {
        return addFooterView(footer, -1, LinearLayout.VERTICAL);
    }

    public int addFooterView(View footer, int index) {
        return addFooterView(footer, index, LinearLayout.VERTICAL);
    }

    /**
     * Add footer view to mFooterLayout and set footer view position in mFooterLayout.
     * When index = -1 or index >= child count in mFooterLayout,
     * the effect of this method is the same as that of {@link #addFooterView(View)}.
     *
     * @param footer
     * @param index  the position in mFooterLayout of this footer.
     *               When index = -1 or index >= child count in mFooterLayout,
     *               the effect of this method is the same as that of {@link #addFooterView(View)}.
     */
    public int addFooterView(View footer, int index, int orientation) {
        if (mFooterLayout == null) {
            mFooterLayout = new LinearLayout(footer.getContext());
            if (orientation == LinearLayout.VERTICAL) {
                mFooterLayout.setOrientation(LinearLayout.VERTICAL);
                mFooterLayout.setLayoutParams(new RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            } else {
                mFooterLayout.setOrientation(LinearLayout.HORIZONTAL);
                mFooterLayout.setLayoutParams(new RecyclerView.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
            }
        }
        final int childCount = mFooterLayout.getChildCount();
        if (index < 0 || index > childCount) {
            index = childCount;
        }
        mFooterLayout.addView(footer, index);
        if (mFooterLayout.getChildCount() == 1) {
            int position = getHeaderLayoutCount() + getItemSize();
            if (position != -1) {
                notifyItemInserted(position);
            }
        }
        return index;
    }

    public void removeFooterView(View footer) {
        if (getFooterLayoutCount() == 0) return;

        mFooterLayout.removeView(footer);
        if (mFooterLayout.getChildCount() == 0) {
            int position = getHeaderLayoutCount() + getItemSize();
            if (position != -1) {
                notifyItemRemoved(position);
            }
        }
    }

    /**
     * if addHeaderView will be return 1, if not will be return 0
     */
    public int getHeaderLayoutCount() {
        if (mHeaderLayout == null || mHeaderLayout.getChildCount() == 0) {
            return 0;
        }
        return 1;
    }

    /**
     * if addFooterView will be return 1, if not will be return 0
     */
    public int getFooterLayoutCount() {
        if (mFooterLayout == null || mFooterLayout.getChildCount() == 0) {
            return 0;
        }
        return 1;
    }

    /**
     * Load more view count
     *
     * @return 0 or 1
     */
    public int getLoadMoreViewCount() {
        if (!mLoadMoreEnable) {
            return 0;
        }
        if (loadMoreStatus == LOAD_MORE_NO_MORE) {
            return 0;
        }
        if (getItemSize() == 0) {
            return 0;
        }
        return 1;
    }

    public boolean isHeadLayout(int position) {
        return getItemViewType(position) == HEADER_VIEW;
    }

    public boolean isLoadMoreLayout(int position) {
        return getItemViewType(position) == LOADING_VIEW;
    }

    public boolean isFooterLayout(int position) {
        return getItemViewType(position) == FOOTER_VIEW;
    }

    /**
     * 加载更多完成
     */
    public void loadMoreComplete() {
        setLoadMoreStatus(LOAD_MORE_NORMAL);
    }

    /**
     * 加载更多失败
     */
    public void loadMoreFail() {
        setLoadMoreStatus(LOAD_MORE_FAIL);
    }

    /**
     * 加载到底部了
     */
    public void loadMoreEnd() {
        setLoadMoreStatus(LOAD_MORE_NO_MORE);
    }

    public void setLoadMoreStatus(int status) {
        if (getLoadMoreViewCount() == 0) {
            return;
        }
        isLoading = false;
        loadMoreStatus = status;
        notifyItemChanged(getHeaderLayoutCount() + getItemSize() + getFooterLayoutCount());
    }

    public void resetLoadMoreStatus() {
        loadMoreStatus = LOAD_MORE_NORMAL;
    }

    @Override
    protected boolean canLoadMore() {
        return loadMoreStatus == LOAD_MORE_NORMAL;
    }

    public static final int LOAD_MORE_NORMAL = 0; //正常情况
    public static final int LOAD_MORE_FAIL = 1; //失败
    public static final int LOAD_MORE_NO_MORE = 2; //没有更多

    private int loadMoreStatus = LOAD_MORE_NORMAL;

    public int getLoadMoreStatus() {
        return loadMoreStatus;
    }

    private void setLoadMoreView(LoadMoreViewHolder viewHolder) {
        switch (loadMoreStatus) {
            case LOAD_MORE_NORMAL:
                viewHolder.progressBar.setVisibility(View.VISIBLE);
                viewHolder.tv_status.setText("加载更多中...");
                viewHolder.itemView.setVisibility(View.VISIBLE);
                break;
            case LOAD_MORE_FAIL:
                viewHolder.progressBar.setVisibility(View.GONE);
                viewHolder.tv_status.setText("加载更多失败，点击重试");

                viewHolder.itemView.setVisibility(View.VISIBLE);
                break;
            case LOAD_MORE_NO_MORE:
                viewHolder.progressBar.setVisibility(View.GONE);
                viewHolder.tv_status.setText("已经到底部了");
                viewHolder.itemView.setVisibility(View.VISIBLE);
                break;
        }
    }

    private static class LoadMoreViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;
        private TextView tv_status;

        public LoadMoreViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static class HeadViewHolder extends RecyclerView.ViewHolder {

        public HeadViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static class FootViewHolder extends RecyclerView.ViewHolder {

        public FootViewHolder(View itemView) {
            super(itemView);
        }
    }
}
