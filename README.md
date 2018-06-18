# FlexibleLayout

可以下拉放大的Layout

![scrollview.gif](https://upload-images.jianshu.io/upload_images/1638147-aff736c1a471cd4a.gif?imageMogr2/auto-orient/strip)

## 支持
- ScrollView
- RecyclerView
- CoordinatorLayout
（其他Layout需要处理改Layout的onTouchEvent事件，否则可能无法使用）


## 使用

**xml**

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

## 支持的方法

配置

|方法 | 功能 | 默认 |
| - | - | - |
| setEnable(boolean isEnable) | 允许下拉放大 | true |
| setHeader(View header) | 设置Header | null |
| setMaxPullHeight(int height) | 最大下拉高度 | 400px |

监听

|方法 | 功能 |
| - | - |
| setReadyListener(OnReadyPullListener listener) | 设置准备监听 |
| setOnPullListener(OnPullListener onPullListener) | 下拉监听 |

