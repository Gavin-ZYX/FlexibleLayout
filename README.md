可以下拉放大的Layout

![](https://upload-images.jianshu.io/upload_images/1638147-1d8b8b96141f3a71.gif?imageMogr2/auto-orient/strip)

## 支持
- 下拉刷新
- ScrollView
- RecyclerView
- CoordinatorLayout
（其他Layout需要处理改Layout的onTouchEvent事件，否则可能无法使用）

## 使用

**依赖**

```gradle
compile 'com.gavin.view.flexible:library:1.0.0'
```

**xml**(ScrollView)

```xml
<com.gavin.view.flexible.FlexibleLayout
    ...>
    <ScrollView
        ...>
        <LinearLayout
            ...
            android:orientation="vertical">
            <ImageView
                android:id="@+id/iv_header"
                .../>
            <TextView
                ... />
        </LinearLayout>
    </ScrollView>
</com.gavin.view.flexible.FlexibleLayout>
```

**Activity**

下拉放大

```java
private ImageView mHeader;
private ScrollView mScrollView;
private FlexibleLayout mFlexibleLayout;
...
mFlexibleLayout.setHeader(mHeader)
            .setReadyListener(new OnReadyPullListener() {
                @Override
                public boolean isReady() {
                    //下拉放大的条件
                    return mScrollView.getScrollY() == 0;
                }
            });
```

下拉放大 + 刷新

```java
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
                //刷新操作
                ...
                //刷新完成后需要调用onRefreshComplete()通知FlexibleLayout
                mFlexibleLayout.onRefreshComplete();
            }
        });
```

## 支持的方法

**配置**

|方法 | 功能 | 默认 |
| - | - | - |
| setEnable(boolean isEnable) | 允许下拉放大 | true |
| setHeader(View header) | 设置Header | null |
| setMaxPullHeight(int height) | Header最大下拉高度 | header 高度 + 1/3屏幕宽度 |
| setRefreshable(boolean isEnable) | 是否允许下拉刷新 | false |
| setMaxRefreshPullHeight(int height) | 刷新View最大下拉高度 | 1/3屏幕宽度 |
| setRefreshSize(int size) | 刷新View的尺寸（正方形）| 1/15屏幕宽度 |


**监听**

|方法 | 功能 |
| - | - |
| setReadyListener(OnReadyPullListener listener) | 设置准备监听 |
| setOnPullListener(OnPullListener onPullListener) | 下拉监听 |
| setRefreshView(View refreshView, OnRefreshListener listener) | 设置下拉刷新View 以及监听 |
| setDefaultRefreshView(OnRefreshListener listener) | 使用默认的下拉刷新View |
