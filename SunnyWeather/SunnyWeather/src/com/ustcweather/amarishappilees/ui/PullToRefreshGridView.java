package com.ustcweather.amarishappilees.ui;

import com.ustcweather.amarishappilees.ui.ILoadingLayout.State;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.GridView;

/**
 * 杩欎釜绫诲疄鐜颁簡GridView涓嬫媺鍒锋柊锛屼笂鍔犺浇鏇村鍜屾粦鍒板簳閮ㄨ嚜鍔ㄥ姞杞�
 * 
 * @author Li Hong
 * @since 2013-8-15
 */
public class PullToRefreshGridView extends PullToRefreshBase<GridView> implements OnScrollListener {
    
    /**ListView*/
    private GridView mGridView;
    /**鐢ㄤ簬婊戝埌搴曢儴鑷姩鍔犺浇鐨凢ooter*/
    private LoadingLayout mFooterLayout;
    /**婊氬姩鐨勭洃鍚櫒*/
    private OnScrollListener mScrollListener;
    
    /**
     * 鏋勯�犳柟娉�
     * 
     * @param context context
     */
    public PullToRefreshGridView(Context context) {
        this(context, null);
    }
    
    /**
     * 鏋勯�犳柟娉�
     * 
     * @param context context
     * @param attrs attrs
     */
    public PullToRefreshGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    /**
     * 鏋勯�犳柟娉�
     * 
     * @param context context
     * @param attrs attrs
     * @param defStyle defStyle
     */
    public PullToRefreshGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
        setPullLoadEnabled(false);
    }

    @Override
    protected GridView createRefreshableView(Context context, AttributeSet attrs) {
        GridView gridView = new GridView(context);
        mGridView = gridView;
        gridView.setOnScrollListener(this);
        
        return gridView;
    }
    
    /**
     * 璁剧疆鏄惁鏈夋洿澶氭暟鎹殑鏍囧織
     * 
     * @param hasMoreData true琛ㄧず杩樻湁鏇村鐨勬暟鎹紝false琛ㄧず娌℃湁鏇村鏁版嵁浜�
     */
    public void setHasMoreData(boolean hasMoreData) {
        if (null != mFooterLayout) {
            if (!hasMoreData) {
                mFooterLayout.setState(State.NO_MORE_DATA);
            }
        }
    }

    /**
     * 璁剧疆婊戝姩鐨勭洃鍚櫒
     * 
     * @param l 鐩戝惉鍣�
     */
    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l;
    }
    
    @Override
    protected boolean isReadyForPullUp() {
        return isLastItemVisible();
    }

    @Override
    protected boolean isReadyForPullDown() {
        return isFirstItemVisible();
    }

    @Override
    protected void startLoading() {
        super.startLoading();
        
        
        if (null != mFooterLayout) {
            mFooterLayout.setState(State.REFRESHING);
        }
    }
    
    @Override
    public void onPullUpRefreshComplete() {
        super.onPullUpRefreshComplete();
        
        if (null != mFooterLayout) {
            mFooterLayout.setState(State.RESET);
        }
    }
    
    @Override
    public void setScrollLoadEnabled(boolean scrollLoadEnabled) {
        super.setScrollLoadEnabled(scrollLoadEnabled);
        
        if (scrollLoadEnabled) {
            // 璁剧疆Footer
            if (null == mFooterLayout) {
                mFooterLayout = new FooterLoadingLayout(getContext());
            }
            
            //mGridView.removeFooterView(mFooterLayout);
            //mGridView.addFooterView(mFooterLayout, null, false);
        } else {
            if (null != mFooterLayout) {
                //mGridView.removeFooterView(mFooterLayout);
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (isScrollLoadEnabled() && hasMoreData()) {
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE 
                    || scrollState == OnScrollListener.SCROLL_STATE_FLING) {
                if (isReadyForPullUp()) {
                    startLoading();
                }
            }
        }
        
        if (null != mScrollListener) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (null != mScrollListener) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }
    
    /**
     * 琛ㄧず鏄惁杩樻湁鏇村鏁版嵁
     * 
     * @return true琛ㄧず杩樻湁鏇村鏁版嵁
     */
    private boolean hasMoreData() {
        if ((null != mFooterLayout) && (mFooterLayout.getState() == State.NO_MORE_DATA)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * 鍒ゆ柇绗竴涓猚hild鏄惁瀹屽叏鏄剧ず鍑烘潵
     * 
     * @return true瀹屽叏鏄剧ず鍑烘潵锛屽惁鍒檉alse
     */
    private boolean isFirstItemVisible() {
        final Adapter adapter = mGridView.getAdapter();

        if (null == adapter || adapter.isEmpty()) {
            return true;
        }

        int mostTop = (mGridView.getChildCount() > 0) ? mGridView.getChildAt(0).getTop() : 0;
        if (mostTop >= 0) {
            return true;
        }

        return false;
    }

    /**
     * 鍒ゆ柇鏈�鍚庝竴涓猚hild鏄惁瀹屽叏鏄剧ず鍑烘潵
     * 
     * @return true瀹屽叏鏄剧ず鍑烘潵锛屽惁鍒檉alse
     */
    private boolean isLastItemVisible() {
        final Adapter adapter = mGridView.getAdapter();

        if (null == adapter || adapter.isEmpty()) {
            return true;
        }

        final int lastItemPosition = adapter.getCount() - 1;
        final int lastVisiblePosition = mGridView.getLastVisiblePosition();

        /**
         * This check should really just be: lastVisiblePosition == lastItemPosition, but ListView
         * internally uses a FooterView which messes the positions up. For me we'll just subtract
         * one to account for it and rely on the inner condition which checks getBottom().
         */
        if (lastVisiblePosition >= lastItemPosition - 1) {
            final int childIndex = lastVisiblePosition - mGridView.getFirstVisiblePosition();
            final int childCount = mGridView.getChildCount();
            final int index = Math.min(childIndex, childCount - 1);
            final View lastVisibleChild = mGridView.getChildAt(index);
            if (lastVisibleChild != null) {
                return lastVisibleChild.getBottom() <= mGridView.getBottom();
            }
        }

        return false;
    }
}
