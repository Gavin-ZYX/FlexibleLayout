package com.gavin.view.flexible;

/**
 * Created by gavin
 * date 2018/6/12
 */
public interface IFlexible {

    /**
     * 是否准备下拉
     * @return
     */
    boolean isReady();

    /**
     * 头部ready
     * @return
     */
    boolean isHeaderReady();

    /**
     * 下拉Header
     * @param offsetY
     */
    void changeHeader(int offsetY);

    /**
     * 重置Header
     */
    void resetHeader();
}
