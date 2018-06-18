package view.gavin.com.flexibleview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by gavin
 * date 2018/6/18
 */
public class ViewGroup1 extends FrameLayout{
    public ViewGroup1(@NonNull Context context) {
        super(context);
    }

    public ViewGroup1(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewGroup1(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected boolean dispatchHoverEvent(MotionEvent event) {
        log("dispatchHoverEvent");
        return super.dispatchHoverEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        log("onInterceptTouchEvent");
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        log("onTouchEvent");
        return super.onTouchEvent(event);
    }

    private void log(String str) {
        Log.i("ViewGroup", str + " -- 1");
    }

}
