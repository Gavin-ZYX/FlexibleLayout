package view.gavin.com.flexibleview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import view.gavin.com.flexibleview.R;

/**
 * Created by gavin
 * date 2018/6/18
 */
public class SimpleListAdapter extends BaseRecyclerViewAdapter<String> {
    public SimpleListAdapter(Context context, @NonNull List<String> list) {
        super(context, list);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_recycler_view;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder getViewHolder(View view) {
        return new Holder(view);
    }

    @Override
    protected void onBindView(RecyclerView.ViewHolder viewHolder, int position) {
        Holder holder = (Holder) viewHolder;
        ((TextView)holder.itemView).setText(list.get(position));
    }

    class Holder extends RecyclerView.ViewHolder {

        public Holder(View itemView) {
            super(itemView);
        }
    }

}
