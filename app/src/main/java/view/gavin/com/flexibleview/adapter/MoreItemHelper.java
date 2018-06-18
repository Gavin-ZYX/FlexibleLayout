package view.gavin.com.flexibleview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by think on 17/8/3.
 */

public abstract class MoreItemHelper<T> {
    protected List<T> list;
    protected Context context;

    public MoreItemHelper(List<T> list) {
        this.list = list;
    }

    /**
     * @return item 类型
     */
    public abstract int getItemType();

    public RecyclerView.ViewHolder createViewHolder(Context context, ViewGroup parent) {
        if (this.context == null) {
            this.context = context;
        }
        View view = LayoutInflater.from(context).inflate(getLayoutId(), parent, false);
        return getViewHolder(view);
    }

    /**
     * @return item 布局
     */
    public abstract int getLayoutId();

    public abstract RecyclerView.ViewHolder getViewHolder(View view);

    /**
     * @return item 个数
     */
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    /**
     * 绑定视图
     */
    public abstract void onBindView(RecyclerView.ViewHolder holder, int position);

}
