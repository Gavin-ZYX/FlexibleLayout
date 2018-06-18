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
public class ViewGroup2 extends FrameLayout{
    public ViewGroup2(@NonNull Context context) {
        super(context);
    }

    public ViewGroup2(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewGroup2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        return true;
    }

    private void log(String str) {
        Log.i("ViewGroup", str + " -- 2 ");
    }

}
