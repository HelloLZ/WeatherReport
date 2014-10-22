package com.ustcweather.amarishappilees.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.ustcweather.amarishappilees.ui.PullToRefreshBase.OnRefreshListener;

/**
 * 瀹氫箟浜嗘媺鍔ㄥ埛鏂扮殑鎺ュ彛
 * 
 * @author Li Hong
 * @since 2013-8-22
 * @param <T>
 */
public interface IPullToRefresh<T extends View> {
    
    /**
     * 璁剧疆褰撳墠涓嬫媺鍒锋柊鏄惁鍙敤
     * 
     * @param pullRefreshEnabled true琛ㄧず鍙敤锛宖alse琛ㄧず涓嶅彲鐢�
     */
    public void setPullRefreshEnabled(boolean pullRefreshEnabled);
    
    /**
     * 璁剧疆褰撳墠涓婃媺鍔犺浇鏇村鏄惁鍙敤
     * 
     * @param pullLoadEnabled true琛ㄧず鍙敤锛宖alse琛ㄧず涓嶅彲鐢�
     */
    public void setPullLoadEnabled(boolean pullLoadEnabled);
    
    /**
     * 婊戝姩鍒板簳閮ㄦ槸鍚﹁嚜鍔ㄥ姞杞芥洿澶氭暟鎹�
     * 
     * @param scrollLoadEnabled 濡傛灉杩欎釜鍊间负true鐨勮瘽锛岄偅涔堜笂鎷夊姞杞芥洿澶氱殑鍔熻兘灏嗕細绂佺敤
     */
    public void setScrollLoadEnabled(boolean scrollLoadEnabled);
    
    /**
     * 鍒ゆ柇褰撳墠涓嬫媺鍒锋柊鏄惁鍙敤
     * 
     * @return true濡傛灉鍙敤锛宖alse涓嶅彲鐢�
     */
    public boolean isPullRefreshEnabled();
    
    /**
     * 鍒ゆ柇涓婃媺鍔犺浇鏄惁鍙敤
     * 
     * @return true鍙敤锛宖alse涓嶅彲鐢�
     */
    public boolean isPullLoadEnabled();
    
    /**
     * 婊戝姩鍒板簳閮ㄥ姞杞芥槸鍚﹀彲鐢�
     * 
     * @return true鍙敤锛屽惁鍒欎笉鍙敤
     */
    public boolean isScrollLoadEnabled();
    
    /**
     * 璁剧疆鍒锋柊鐨勭洃鍚櫒
     * 
     * @param refreshListener 鐩戝惉鍣ㄥ璞�
     */
    public void setOnRefreshListener(OnRefreshListener<T> refreshListener);
    
    /**
     * 缁撴潫涓嬫媺鍒锋柊
     */
    public void onPullDownRefreshComplete();
    
    /**
     * 缁撴潫涓婃媺鍔犺浇鏇村
     */
    public void onPullUpRefreshComplete();
    
    /**
     * 寰楀埌鍙埛鏂扮殑View瀵硅薄
     * 
     * @return 杩斿洖璋冪敤{@link #createRefreshableView(Context, AttributeSet)} 鏂规硶杩斿洖鐨勫璞�
     */
    public T getRefreshableView();
    
    /**
     * 寰楀埌Header甯冨眬瀵硅薄
     * 
     * @return Header甯冨眬瀵硅薄
     */
    public LoadingLayout getHeaderLoadingLayout();
    
    /**
     * 寰楀埌Footer甯冨眬瀵硅薄
     * 
     * @return Footer甯冨眬瀵硅薄
     */
    public LoadingLayout getFooterLoadingLayout();
    
    /**
     * 璁剧疆鏈�鍚庢洿鏂扮殑鏃堕棿鏂囨湰
     * 
     * @param label 鏂囨湰
     */
    public void setLastUpdatedLabel(CharSequence label);
}
