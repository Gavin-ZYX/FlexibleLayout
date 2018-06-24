package view.gavin.com.flexibleview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;

import com.gavin.view.flexible.FlexibleLayout;
import com.gavin.view.flexible.callback.OnReadyPullListener;
import com.gavin.view.flexible.callback.OnRefreshListener;

/**
 * Created by gavin
 * date 2018/6/18
 */
public class ScrollViewActivity extends AppCompatActivity {

    private View mHeader;
    private ScrollView mScrollView;
    private FlexibleLayout mFlexibleLayout;
    private View mRefreshView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_view);
        initView();
        mFlexibleLayout.setHeader(mHeader)
                .setReadyListener(new OnReadyPullListener() {
                    @Override
                    public boolean isReady() {
                        return mScrollView.getScrollY() == 0;
                    }
                })
                .setRefreshable(true)
                .setDefaultRefreshView(new OnRefreshListener() {
                    @Override
                    public void onRefreshing() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                //刷新完成后需要调用onRefreshComplete()通知FlexibleLayout
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mFlexibleLayout.onRefreshComplete();
                                    }
                                });
                            }
                        }).start();
                    }
                });
    }

    private void initView() {
        mFlexibleLayout = (FlexibleLayout) findViewById(R.id.ffv);
        mHeader = findViewById(R.id.header);
        mScrollView = (ScrollView) findViewById(R.id.sv);
        mRefreshView = LayoutInflater.from(this).inflate(R.layout.refresh_layout, null);
    }
}
