package view.gavin.com.flexibleview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.gavin.view.flexible.FlexibleLayout;
import com.gavin.view.flexible.callback.OnReadyPullListener;

/**
 * Created by gavin
 * date 2018/6/18
 */
public class ScrollViewActivity extends AppCompatActivity {

    private ImageView mHeader;
    private ScrollView mScrollView;
    private FlexibleLayout mFlexibleLayout;

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
                });
    }

    private void initView() {
        mFlexibleLayout =findViewById(R.id.ffv);
        mHeader = findViewById(R.id.iv_header);
        mScrollView = findViewById(R.id.sv);
    }
}
