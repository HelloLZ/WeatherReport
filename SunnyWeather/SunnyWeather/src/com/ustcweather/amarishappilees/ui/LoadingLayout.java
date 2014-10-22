package com.ustcweather.amarishappilees.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * 杩欎釜绫诲畾涔変簡Header鍜孎ooter鐨勫叡閫氳涓�
 * 
 * @author Li Hong
 * @since 2013-8-16
 */
public abstract class LoadingLayout extends FrameLayout implements ILoadingLayout {
    
    /**瀹瑰櫒甯冨眬*/
    private View mContainer;
    /**褰撳墠鐨勭姸鎬�*/
    private State mCurState = State.NONE;
    /**鍓嶄竴涓姸鎬�*/
    private State mPreState = State.NONE;
    
    /**
     * 鏋勯�犳柟娉�
     * 
     * @param context context
     */
    public LoadingLayout(Context context) {
        this(context, null);
    }
    
    /**
     * 鏋勯�犳柟娉�
     * 
     * @param context context
     * @param attrs attrs
     */
    public LoadingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    /**
     * 鏋勯�犳柟娉�
     * 
     * @param context context
     * @param attrs attrs
     * @param defStyle defStyle
     */
    public LoadingLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
        init(context, attrs);
    }
    
    /**
     * 鍒濆鍖�
     * 
     * @param context context
     * @param attrs attrs
     */
    protected void init(Context context, AttributeSet attrs) {
        mContainer = createLoadingView(context, attrs);
        if (null == mContainer) {
            throw new NullPointerException("Loading view can not be null.");
        }
        
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, 
                LayoutParams.WRAP_CONTENT);
        addView(mContainer, params);
    }

    /**
     * 鏄剧ず鎴栭殣钘忚繖涓竷灞�
     * 
     * @param show flag
     */
    public void show(boolean show) {
        // If is showing, do nothing.
        if (show == (View.VISIBLE == getVisibility())) {
            return;
        }
        
        ViewGroup.LayoutParams params = mContainer.getLayoutParams();
        if (null != params) {
            if (show) {
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            } else {
                params.height = 0;
            }
            setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        }
    }
    
    /**
     * 璁剧疆鏈�鍚庢洿鏂扮殑鏃堕棿鏂囨湰
     * 
     * @param label 鏂囨湰
     */
    public void setLastUpdatedLabel(CharSequence label) {
        
    }
    
    /**
     * 璁剧疆鍔犺浇涓殑鍥剧墖
     * 
     * @param drawable 鍥剧墖
     */
    public void setLoadingDrawable(Drawable drawable) {
        
    }

    /**
     * 璁剧疆鎷夊姩鐨勬枃鏈紝鍏稿瀷鐨勬槸鈥滀笅鎷夊彲浠ュ埛鏂扳��
     * 
     * @param pullLabel 鎷夊姩鐨勬枃鏈�
     */
    public void setPullLabel(CharSequence pullLabel) {
        
    }

    /**
     * 璁剧疆姝ｅ湪鍒锋柊鐨勬枃鏈紝鍏稿瀷鐨勬槸鈥滄鍦ㄥ埛鏂扳��
     * 
     * @param refreshingLabel 鍒锋柊鏂囨湰
     */
    public void setRefreshingLabel(CharSequence refreshingLabel) {
        
    }

    /**
     * 璁剧疆閲婃斁鐨勬枃鏈紝鍏稿瀷鐨勬槸鈥滄澗寮�鍙互鍒锋柊鈥�
     * 
     * @param releaseLabel 閲婃斁鏂囨湰
     */
    public void setReleaseLabel(CharSequence releaseLabel) {
        
    }

    @Override
    public void setState(State state) {
        if (mCurState != state) {
            mPreState = mCurState;
            mCurState = state;
            onStateChanged(state, mPreState);
        }
    }
    
    @Override
    public State getState() {
        return mCurState;
    }

    @Override
    public void onPull(float scale) {
        
    }
    
    /**
     * 寰楀埌鍓嶄竴涓姸鎬�
     * 
     * @return 鐘舵��
     */
    protected State getPreState() {
        return mPreState;
    }
    
    /**
     * 褰撶姸鎬佹敼鍙樻椂璋冪敤
     * 
     * @param curState 褰撳墠鐘舵��
     * @param oldState 鑰佺殑鐘舵��
     */
    protected void onStateChanged(State curState, State oldState) {
        switch (curState) {
        case RESET:
            onReset();
            break;
            
        case RELEASE_TO_REFRESH:
            onReleaseToRefresh();
            break;
            
        case PULL_TO_REFRESH:
            onPullToRefresh();
            break;
            
        case REFRESHING:
            onRefreshing();
            break;
            
        case NO_MORE_DATA:
            onNoMoreData();
            break;
            
        default:
            break;
        }
    }
    
    /**
     * 褰撶姸鎬佽缃负{@link State#RESET}鏃惰皟鐢�
     */
    protected void onReset() {
        
    }
    
    /**
     * 褰撶姸鎬佽缃负{@link State#PULL_TO_REFRESH}鏃惰皟鐢�
     */
    protected void onPullToRefresh() {
        
    }
    
    /**
     * 褰撶姸鎬佽缃负{@link State#RELEASE_TO_REFRESH}鏃惰皟鐢�
     */
    protected void onReleaseToRefresh() {
        
    }
    
    /**
     * 褰撶姸鎬佽缃负{@link State#REFRESHING}鏃惰皟鐢�
     */
    protected void onRefreshing() {
        
    }
    
    /**
     * 褰撶姸鎬佽缃负{@link State#NO_MORE_DATA}鏃惰皟鐢�
     */
    protected void onNoMoreData() {
        
    }
    
    /**
     * 寰楀埌褰撳墠Layout鐨勫唴瀹瑰ぇ灏忥紝瀹冨皢浣滀负涓�涓埛鏂扮殑涓寸晫鐐�
     * 
     * @return 楂樺害
     */
    public abstract int getContentSize();
    
    /**
     * 鍒涘缓Loading鐨刅iew
     * 
     * @param context context
     * @param attrs attrs
     * @return Loading鐨刅iew
     */
    protected abstract View createLoadingView(Context context, AttributeSet attrs);
}
