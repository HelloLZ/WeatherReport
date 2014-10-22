package com.ustcweather.amarishappilees.ui;

import com.ustcweather.amarishappilees.ui.ILoadingLayout.State;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public abstract class PullToRefreshBase<T extends View> extends LinearLayout implements IPullToRefresh<T> {
    /**
     * 瀹氫箟浜嗕笅鎷夊埛鏂板拰涓婃媺鍔犺浇鏇村鐨勬帴鍙ｃ��
     * 
     * @author Li Hong
     * @since 2013-7-29
     */
    public interface OnRefreshListener<V extends View> {
     
        /**
         * 涓嬫媺鏉炬墜鍚庝細琚皟鐢�
         * 
         * @param refreshView 鍒锋柊鐨刅iew
         */
        void onPullDownToRefresh(final PullToRefreshBase<V> refreshView);
        
        /**
         * 鍔犺浇鏇村鏃朵細琚皟鐢ㄦ垨涓婃媺鏃惰皟鐢�
         * 
         * @param refreshView 鍒锋柊鐨刅iew
         */
        void onPullUpToRefresh(final PullToRefreshBase<V> refreshView);
    }
    
    /**鍥炴粴鐨勬椂闂�*/
    private static final int SCROLL_DURATION = 150;
    /**闃诲凹绯绘暟*/
    private static final float OFFSET_RADIO = 2.5f;
    /**涓婁竴娆＄Щ鍔ㄧ殑鐐� */
    private float mLastMotionY = -1;
    /**涓嬫媺鍒锋柊鍜屽姞杞芥洿澶氱殑鐩戝惉鍣� */
    private OnRefreshListener<T> mRefreshListener;
    /**涓嬫媺鍒锋柊鐨勫竷灞� */
    private LoadingLayout mHeaderLayout;
    /**涓婃媺鍔犺浇鏇村鐨勫竷灞�*/
    private LoadingLayout mFooterLayout;
    /**HeaderView鐨勯珮搴�*/
    private int mHeaderHeight;
    /**FooterView鐨勯珮搴�*/
    private int mFooterHeight;
    /**涓嬫媺鍒锋柊鏄惁鍙敤*/
    private boolean mPullRefreshEnabled = true;
    /**涓婃媺鍔犺浇鏄惁鍙敤*/
    private boolean mPullLoadEnabled = false;
    /**鍒ゆ柇婊戝姩鍒板簳閮ㄥ姞杞芥槸鍚﹀彲鐢�*/
    private boolean mScrollLoadEnabled = false;
    /**鏄惁鎴柇touch浜嬩欢*/
    private boolean mInterceptEventEnable = true;
    /**琛ㄧず鏄惁娑堣垂浜唗ouch浜嬩欢锛屽鏋滄槸锛屽垯涓嶈皟鐢ㄧ埗绫荤殑onTouchEvent鏂规硶*/
    private boolean mIsHandledTouchEvent = false;
    /**绉诲姩鐐圭殑淇濇姢鑼冨洿鍊�*/
    private int mTouchSlop;
    /**涓嬫媺鐨勭姸鎬�*/
    private State mPullDownState = State.NONE;
    /**涓婃媺鐨勭姸鎬�*/
    private State mPullUpState = State.NONE;
    /**鍙互涓嬫媺鍒锋柊鐨刅iew*/
    T mRefreshableView;
    /**骞虫粦婊氬姩鐨凴unnable*/
    private SmoothScrollRunnable mSmoothScrollRunnable;
    /**鍙埛鏂癡iew鐨勫寘瑁呭竷灞�*/
    private FrameLayout mRefreshableViewWrapper;
    
    /**
     * 鏋勯�犳柟娉�
     * 
     * @param context context
     */
    public PullToRefreshBase(Context context) {
        super(context);
        init(context, null);
    }

    /**
     * 鏋勯�犳柟娉�
     * 
     * @param context context
     * @param attrs attrs
     */
    public PullToRefreshBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * 鏋勯�犳柟娉�
     * 
     * @param context context
     * @param attrs attrs
     * @param defStyle defStyle
     */
    public PullToRefreshBase(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    /**
     * 鍒濆鍖�
     * 
     * @param context context
     */
    private void init(Context context, AttributeSet attrs) {
        setOrientation(LinearLayout.VERTICAL);
        
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        
        mHeaderLayout = createHeaderLoadingLayout(context, attrs);
        mFooterLayout = createFooterLoadingLayout(context, attrs);
        mRefreshableView = createRefreshableView(context, attrs);
        
        if (null == mRefreshableView) {
            throw new NullPointerException("Refreshable view can not be null.");
        }
        
        addRefreshableView(context, mRefreshableView);
        addHeaderAndFooter(context);

        // 寰楀埌Header鐨勯珮搴︼紝杩欎釜楂樺害闇�瑕佺敤杩欑鏂瑰紡寰楀埌锛屽湪onLayout鏂规硶閲岄潰寰楀埌鐨勯珮搴﹀缁堟槸0
        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                refreshLoadingViewsSize();
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }
    
    /**
     * 鍒濆鍖杙adding锛屾垜浠牴鎹甴eader鍜宖ooter鐨勯珮搴︽潵璁剧疆top padding鍜宐ottom padding
     */
    private void refreshLoadingViewsSize() {
        // 寰楀埌header鍜宖ooter鐨勫唴瀹归珮搴︼紝瀹冨皢浼氫綔涓烘嫋鍔ㄥ埛鏂扮殑涓�涓复鐣屽�硷紝濡傛灉鎷栧姩璺濈澶т簬杩欎釜楂樺害
        // 鐒跺悗鍐嶆澗寮�鎵嬶紝灏变細瑙﹀彂鍒锋柊鎿嶄綔
        int headerHeight = (null != mHeaderLayout) ? mHeaderLayout.getContentSize() : 0;
        int footerHeight = (null != mFooterLayout) ? mFooterLayout.getContentSize() : 0;
        
        if (headerHeight < 0) {
            headerHeight = 0;
        }
        
        if (footerHeight < 0) {
            footerHeight = 0;
        }
        
        mHeaderHeight = headerHeight;
        mFooterHeight = footerHeight;
        
        // 杩欓噷寰楀埌Header鍜孎ooter鐨勯珮搴︼紝璁剧疆鐨刾adding鐨則op鍜宐ottom灏卞簲璇ユ槸header鍜宖ooter鐨勯珮搴�
        // 鍥犱负header鍜宖ooter鏄畬鍏ㄧ湅涓嶈鐨�
        headerHeight = (null != mHeaderLayout) ? mHeaderLayout.getMeasuredHeight() : 0;
        footerHeight = (null != mFooterLayout) ? mFooterLayout.getMeasuredHeight() : 0;
        if (0 == footerHeight) {
            footerHeight = mFooterHeight;
        }
        
        int pLeft = getPaddingLeft();
        int pTop = getPaddingTop();
        int pRight = getPaddingRight();
        int pBottom = getPaddingBottom();
        
        pTop = -headerHeight;
        pBottom = -footerHeight;
        
        setPadding(pLeft, pTop, pRight, pBottom);
    }
    
    @Override
    protected final void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        
        // We need to update the header/footer when our size changes
        refreshLoadingViewsSize();
        
        // 璁剧疆鍒锋柊View鐨勫ぇ灏�
        refreshRefreshableViewSize(w, h);
        
        /**
         * As we're currently in a Layout Pass, we need to schedule another one
         * to layout any changes we've made here
         */
        post(new Runnable() {
            @Override
            public void run() {
                requestLayout();
            }
        });
    }
    
    @Override
    public void setOrientation(int orientation) {
        if (LinearLayout.VERTICAL != orientation) {
            throw new IllegalArgumentException("This class only supports VERTICAL orientation.");
        }
        
        // Only support vertical orientation
        super.setOrientation(orientation);
    }
    
    @Override
    public final boolean onInterceptTouchEvent(MotionEvent event) {
        if (!isInterceptTouchEventEnabled()) {
            return false;
        }
        
        if (!isPullLoadEnabled() && !isPullRefreshEnabled()) {
            return false;
        }
        
        final int action = event.getAction();
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mIsHandledTouchEvent = false;
            return false;
        }
        
        if (action != MotionEvent.ACTION_DOWN && mIsHandledTouchEvent) {
            return true;
        }
        
        switch (action) {
        case MotionEvent.ACTION_DOWN:
            mLastMotionY = event.getY();
            mIsHandledTouchEvent = false;
            break;
            
        case MotionEvent.ACTION_MOVE:
            final float deltaY = event.getY() - mLastMotionY;
            final float absDiff = Math.abs(deltaY);
            // 杩欓噷鏈変笁涓潯浠讹細
            // 1锛屼綅绉诲樊澶т簬mTouchSlop锛岃繖鏄负浜嗛槻姝㈠揩閫熸嫋鍔ㄥ紩鍙戝埛鏂�
            // 2锛宨sPullRefreshing()锛屽鏋滃綋鍓嶆鍦ㄤ笅鎷夊埛鏂扮殑璇濓紝鏄厑璁稿悜涓婃粦鍔紝骞舵妸鍒锋柊鐨凥eaderView鎸や笂鍘�
            // 3锛宨sPullLoading()锛岀悊鐢变笌绗�2鏉＄浉鍚�
            if (absDiff > mTouchSlop || isPullRefreshing() || isPullLoading())  {
                mLastMotionY = event.getY();
                // 绗竴涓樉绀哄嚭鏉ワ紝Header宸茬粡鏄剧ず鎴栨媺涓�
                if (isPullRefreshEnabled() && isReadyForPullDown()) {
                    // 1锛孧ath.abs(getScrollY()) > 0锛氳〃绀哄綋鍓嶆粦鍔ㄧ殑鍋忕Щ閲忕殑缁濆鍊煎ぇ浜�0锛岃〃绀哄綋鍓岺eaderView婊戝嚭鏉ヤ簡鎴栧畬鍏�
                    // 涓嶅彲瑙侊紝瀛樺湪杩欐牱涓�绉峜ase锛屽綋姝ｅ湪鍒锋柊鏃跺苟涓擱efreshableView宸茬粡婊戝埌椤堕儴锛屽悜涓婃粦鍔紝閭ｄ箞鎴戜滑鏈熸湜鐨勭粨鏋滄槸
                    // 渚濈劧鑳藉悜涓婃粦鍔紝鐩村埌HeaderView瀹屽叏涓嶅彲瑙�
                    // 2锛宒eltaY > 0.5f锛氳〃绀轰笅鎷夌殑鍊煎ぇ浜�0.5f
                    mIsHandledTouchEvent = (Math.abs(getScrollYValue()) > 0 || deltaY > 0.5f);
                    // 濡傛灉鎴柇浜嬩欢锛屾垜浠垯浠嶇劧鎶婅繖涓簨浠朵氦缁欏埛鏂癡iew鍘诲鐞嗭紝鍏稿瀷鐨勬儏鍐垫槸璁㎜istView/GridView灏嗘寜涓�
                    // Child鐨凷elector闅愯棌
                    if (mIsHandledTouchEvent) {
                        mRefreshableView.onTouchEvent(event);
                    }
                } else if (isPullLoadEnabled() && isReadyForPullUp()) {
                    // 鍘熺悊濡備笂
                    mIsHandledTouchEvent = (Math.abs(getScrollYValue()) > 0 || deltaY < -0.5f);
                }
            }
            break; 
            
        default:
            break;
        }
        
        return mIsHandledTouchEvent;
    }

    @Override
    public final boolean onTouchEvent(MotionEvent ev) {
        boolean handled = false;
        switch (ev.getAction()) {
        case MotionEvent.ACTION_DOWN:
            mLastMotionY = ev.getY();
            mIsHandledTouchEvent = false;
            break;
            
        case MotionEvent.ACTION_MOVE:
            final float deltaY = ev.getY() - mLastMotionY;
            mLastMotionY = ev.getY();
            if (isPullRefreshEnabled() && isReadyForPullDown()) {
                pullHeaderLayout(deltaY / OFFSET_RADIO);
                handled = true;
            } else if (isPullLoadEnabled() && isReadyForPullUp()) {
                pullFooterLayout(deltaY / OFFSET_RADIO);
                handled = true;
            } else {
                mIsHandledTouchEvent = false;
            }
            break;
            
        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:
            if (mIsHandledTouchEvent) {
                mIsHandledTouchEvent = false;
                // 褰撶涓�涓樉绀哄嚭鏉ユ椂
                if (isReadyForPullDown()) {
                    // 璋冪敤鍒锋柊
                    if (mPullRefreshEnabled && (mPullDownState == State.RELEASE_TO_REFRESH)) {
                        startRefreshing();
                        handled = true;
                    }
                    resetHeaderLayout();
                } else if (isReadyForPullUp()) {
                    // 鍔犺浇鏇村
                    if (isPullLoadEnabled() && (mPullUpState == State.RELEASE_TO_REFRESH)) {
                        startLoading();
                        handled = true;
                    }
                    resetFooterLayout();
                }
            }
            break;

        default:
            break;
        }
        
        return handled;
    }
    
    @Override
    public void setPullRefreshEnabled(boolean pullRefreshEnabled) {
        mPullRefreshEnabled = pullRefreshEnabled;
    }
    
    @Override
    public void setPullLoadEnabled(boolean pullLoadEnabled) {
        mPullLoadEnabled = pullLoadEnabled;
    }
    
    @Override
    public void setScrollLoadEnabled(boolean scrollLoadEnabled) {
        mScrollLoadEnabled = scrollLoadEnabled;
    }
    
    @Override
    public boolean isPullRefreshEnabled() {
        return mPullRefreshEnabled && (null != mHeaderLayout);
    }
    
    @Override
    public boolean isPullLoadEnabled() {
        return mPullLoadEnabled && (null != mFooterLayout);
    }
  
    @Override
    public boolean isScrollLoadEnabled() {
        return mScrollLoadEnabled;
    }
    
    @Override
    public void setOnRefreshListener(OnRefreshListener<T> refreshListener) {
        mRefreshListener = refreshListener;
    }
    
    @Override
    public void onPullDownRefreshComplete() {
        if (isPullRefreshing()) {
            mPullDownState = State.RESET;
            onStateChanged(State.RESET, true);
            
            // 鍥炴粴鍔ㄦ湁涓�涓椂闂达紝鎴戜滑鍦ㄥ洖婊氬畬鎴愬悗鍐嶈缃姸鎬佷负normal
            // 鍦ㄥ皢LoadingLayout鐨勭姸鎬佽缃负normal涔嬪墠锛屾垜浠簲璇ョ姝�
            // 鎴柇Touch浜嬩欢锛屽洜涓鸿閲屾湁涓�涓猵ost鐘舵�侊紝濡傛灉鏈塸ost鐨凴unnable
            // 鏈鎵ц鏃讹紝鐢ㄦ埛鍐嶄竴娆″彂璧蜂笅鎷夊埛鏂帮紝濡傛灉姝ｅ湪鍒锋柊鏃讹紝杩欎釜Runnable
            // 鍐嶆琚墽琛屽埌锛岄偅涔堝氨浼氭妸姝ｅ湪鍒锋柊鐨勭姸鎬佹敼涓烘甯哥姸鎬侊紝杩欏氨涓嶇鍚堟湡鏈�
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    setInterceptTouchEventEnabled(true);
                    mHeaderLayout.setState(State.RESET);
                }
            }, getSmoothScrollDuration());
            
            resetHeaderLayout();
            setInterceptTouchEventEnabled(false);
        }
    }
    
    @Override
    public void onPullUpRefreshComplete() {
        if (isPullLoading()) {
            mPullUpState = State.RESET;
            onStateChanged(State.RESET, false);

            postDelayed(new Runnable() {
                @Override
                public void run() {
                    setInterceptTouchEventEnabled(true);
                    mFooterLayout.setState(State.RESET);
                }
            }, getSmoothScrollDuration());
            
            resetFooterLayout();
            setInterceptTouchEventEnabled(false);
        }
    }
    
    @Override
    public T getRefreshableView() {
        return mRefreshableView;
    }
    
    @Override
    public LoadingLayout getHeaderLoadingLayout() {
        return mHeaderLayout;
    }
    
    @Override
    public LoadingLayout getFooterLoadingLayout() {
        return mFooterLayout;
    }

    @Override
    public void setLastUpdatedLabel(CharSequence label){
        if (null != mHeaderLayout) {
            mHeaderLayout.setLastUpdatedLabel(label);
        }
        
        if (null != mFooterLayout) {
            mFooterLayout.setLastUpdatedLabel(label);
        }
    }
    
    /**
     * 寮�濮嬪埛鏂帮紝閫氬父鐢ㄤ簬璋冪敤鑰呬富鍔ㄥ埛鏂帮紝鍏稿瀷鐨勬儏鍐垫槸杩涘叆鐣岄潰锛屽紑濮嬩富鍔ㄥ埛鏂帮紝杩欎釜鍒锋柊骞朵笉鏄敱鐢ㄦ埛鎷夊姩寮曡捣鐨�
     * 
     * @param smoothScroll 琛ㄧず鏄惁鏈夊钩婊戞粴鍔紝true琛ㄧず骞虫粦婊氬姩锛宖alse琛ㄧず鏃犲钩婊戞粴鍔�
     * @param delayMillis 寤惰繜鏃堕棿
     */
    public void doPullRefreshing(final boolean smoothScroll, final long delayMillis) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                int newScrollValue = -mHeaderHeight;
                int duration = smoothScroll ? SCROLL_DURATION : 0;
                
                startRefreshing();
                smoothScrollTo(newScrollValue, duration, 0);
            }
        }, delayMillis);
    }
    
    /**
     * 鍒涘缓鍙互鍒锋柊鐨刅iew
     * 
     * @param context context
     * @param attrs 灞炴��
     * @return View
     */
    protected abstract T createRefreshableView(Context context, AttributeSet attrs);
    
    /**
     * 鍒ゆ柇鍒锋柊鐨刅iew鏄惁婊戝姩鍒伴《閮�
     * 
     * @return true琛ㄧず宸茬粡婊戝姩鍒伴《閮紝鍚﹀垯false
     */
    protected abstract boolean isReadyForPullDown();
    
    /**
     * 鍒ゆ柇鍒锋柊鐨刅iew鏄惁婊戝姩鍒板簳
     * 
     * @return true琛ㄧず宸茬粡婊戝姩鍒板簳閮紝鍚﹀垯false
     */
    protected abstract boolean isReadyForPullUp();
    
    /**
     * 鍒涘缓Header鐨勫竷灞�
     * 
     * @param context context
     * @param attrs 灞炴��
     * @return LoadingLayout瀵硅薄
     */
    protected LoadingLayout createHeaderLoadingLayout(Context context, AttributeSet attrs) {
        return new HeaderLoadingLayout(context);
    }
    
    /**
     * 鍒涘缓Footer鐨勫竷灞�
     * 
     * @param context context
     * @param attrs 灞炴��
     * @return LoadingLayout瀵硅薄
     */
    protected LoadingLayout createFooterLoadingLayout(Context context, AttributeSet attrs) {
        return new FooterLoadingLayout(context);
    }
    
    /**
     * 寰楀埌骞虫粦婊氬姩鐨勬椂闂达紝娲剧敓绫诲彲浠ラ噸鍐欒繖涓柟娉曟潵鎺т欢婊氬姩鏃堕棿
     * 
     * @return 杩斿洖鍊兼椂闂翠负姣
     */
    protected long getSmoothScrollDuration() {
        return SCROLL_DURATION;
    }
    
    /**
     * 璁＄畻鍒锋柊View鐨勫ぇ灏�
     * 
     * @param width 褰撳墠瀹瑰櫒鐨勫搴�
     * @param height 褰撳墠瀹瑰櫒鐨勫搴�
     */
    protected void refreshRefreshableViewSize(int width, int height) {
        if (null != mRefreshableViewWrapper) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mRefreshableViewWrapper.getLayoutParams();
            if (lp.height != height) {
                lp.height = height;
                mRefreshableViewWrapper.requestLayout();
            }
        }
    }
    
    /**
     * 灏嗗埛鏂癡iew娣诲姞鍒板綋鍓嶅鍣ㄤ腑
     * 
     * @param context context
     * @param refreshableView 鍙互鍒锋柊鐨刅iew
     */
    protected void addRefreshableView(Context context, T refreshableView) {
        int width  = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        
        // 鍒涘缓涓�涓寘瑁呭鍣�
        mRefreshableViewWrapper = new FrameLayout(context);
        mRefreshableViewWrapper.addView(refreshableView, width, height);

        // 杩欓噷鎶奟efresh view鐨勯珮搴﹁缃负涓�涓緢灏忕殑鍊硷紝瀹冪殑楂樺害鏈�缁堜細鍦╫nSizeChanged()鏂规硶涓缃负MATCH_PARENT
        // 杩欐牱鍋氱殑鍘熷洜鏄紝濡傛灉姝ゆ槸瀹冪殑height鏄疢ATCH_PARENT锛岄偅涔坒ooter寰楀埌鐨勯珮搴﹀氨鏄�0锛屾墍浠ワ紝鎴戜滑鍏堣缃珮搴﹀緢灏�
        // 鎴戜滑灏卞彲浠ュ緱鍒癶eader鍜宖ooter鐨勬甯搁珮搴︼紝褰搊nSizeChanged鍚庯紝Refresh view鐨勯珮搴﹀張浼氬彉涓烘甯搞��
        height = 10;
        addView(mRefreshableViewWrapper, new LinearLayout.LayoutParams(width, height));
    }
    
    /**
     * 娣诲姞Header鍜孎ooter
     * 
     * @param context context
     */
    protected void addHeaderAndFooter(Context context) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        
        final LoadingLayout headerLayout = mHeaderLayout;
        final LoadingLayout footerLayout = mFooterLayout;
        
        if (null != headerLayout) {
            if (this == headerLayout.getParent()) {
                removeView(headerLayout);
            }
            
            addView(headerLayout, 0, params);
        }
        
        if (null != footerLayout) {
            if (this == footerLayout.getParent()) {
                removeView(footerLayout);
            }
            
            addView(footerLayout, -1, params);
        }
    }
    
    /**
     * 鎷夊姩Header Layout鏃惰皟鐢�
     * 
     * @param delta 绉诲姩鐨勮窛绂�
     */
    protected void pullHeaderLayout(float delta) {
        // 鍚戜笂婊戝姩锛屽苟涓斿綋鍓峴crollY涓�0鏃讹紝涓嶆粦鍔�
        int oldScrollY = getScrollYValue();
        if (delta < 0 && (oldScrollY - delta) >= 0) {
            setScrollTo(0, 0);
            return;
        }
        
        // 鍚戜笅婊戝姩甯冨眬
        setScrollBy(0, -(int)delta);
        
        if (null != mHeaderLayout && 0 != mHeaderHeight) {
            float scale = Math.abs(getScrollYValue()) / (float) mHeaderHeight;
            mHeaderLayout.onPull(scale);
        }
        
        // 鏈浜庡埛鏂扮姸鎬侊紝鏇存柊绠ご
        int scrollY = Math.abs(getScrollYValue());
        if (isPullRefreshEnabled() && !isPullRefreshing()) { 
            if (scrollY > mHeaderHeight) {
                mPullDownState = State.RELEASE_TO_REFRESH;
            } else {
                mPullDownState = State.PULL_TO_REFRESH;
            }
            
            mHeaderLayout.setState(mPullDownState);
            onStateChanged(mPullDownState, true);
        }
    }

    /**
     * 鎷塅ooter鏃惰皟鐢�
     * 
     * @param delta 绉诲姩鐨勮窛绂�
     */
    protected void pullFooterLayout(float delta) {
        int oldScrollY = getScrollYValue();
        if (delta > 0 && (oldScrollY - delta) <= 0) {
            setScrollTo(0, 0);
            return;
        }
        
        setScrollBy(0, -(int)delta);
        
        if (null != mFooterLayout && 0 != mFooterHeight) {
            float scale = Math.abs(getScrollYValue()) / (float) mFooterHeight;
            mFooterLayout.onPull(scale);
        }
        
        int scrollY = Math.abs(getScrollYValue());
        if (isPullLoadEnabled() && !isPullLoading()) {
            if (scrollY > mFooterHeight) {
                mPullUpState = State.RELEASE_TO_REFRESH;
            } else {
                mPullUpState = State.PULL_TO_REFRESH;
            }
            
            mFooterLayout.setState(mPullUpState);
            onStateChanged(mPullUpState, false);
        }
    }

    /**
     * 寰楃疆header
     */
    protected void resetHeaderLayout() {
        final int scrollY = Math.abs(getScrollYValue());
        final boolean refreshing = isPullRefreshing();
        
        if (refreshing && scrollY <= mHeaderHeight) {
            smoothScrollTo(0);
            return;
        }
        
        if (refreshing) {
            smoothScrollTo(-mHeaderHeight);
        } else {
            smoothScrollTo(0);
        }
    }
    
    /**
     * 閲嶇疆footer
     */
    protected void resetFooterLayout() {
        int scrollY = Math.abs(getScrollYValue());
        boolean isPullLoading = isPullLoading();
        
        if (isPullLoading && scrollY <= mFooterHeight) {
            smoothScrollTo(0);
            return;
        }
        
        if (isPullLoading) {
            smoothScrollTo(mFooterHeight);
        } else {
            smoothScrollTo(0);
        }
    }
    
    /**
     * 鍒ゆ柇鏄惁姝ｅ湪涓嬫媺鍒锋柊
     * 
     * @return true姝ｅ湪鍒锋柊锛屽惁鍒檉alse
     */
    protected boolean isPullRefreshing() {
        return (mPullDownState == State.REFRESHING);
    }
    
    /**
     * 鏄惁姝ｇ殑涓婃媺鍔犺浇鏇村
     * 
     * @return true姝ｅ湪鍔犺浇鏇村锛屽惁鍒檉alse
     */
    protected boolean isPullLoading() {
        return (mPullUpState == State.REFRESHING);
    }
    
    /**
     * 寮�濮嬪埛鏂帮紝褰撲笅鎷夋澗寮�鍚庤璋冪敤
     */
    protected void startRefreshing() {
        // 濡傛灉姝ｅ湪鍒锋柊
        if (isPullRefreshing()) {
            return;
        }
        
        mPullDownState = State.REFRESHING;
        onStateChanged(State.REFRESHING, true);
        
        if (null != mHeaderLayout) {
            mHeaderLayout.setState(State.REFRESHING);
        }
        
        if (null != mRefreshListener) {
            // 鍥犱负婊氬姩鍥炲師濮嬩綅缃殑鏃堕棿鏄�200锛屾垜浠渶瑕佺瓑鍥炴粴瀹屽悗鎵嶆墽琛屽埛鏂板洖璋�
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRefreshListener.onPullDownToRefresh(PullToRefreshBase.this);
                }
            }, getSmoothScrollDuration()); 
        }
    }

    /**
     * 寮�濮嬪姞杞芥洿澶氾紝涓婃媺鏉惧紑鍚庤皟鐢�
     */
    protected void startLoading() {
        // 濡傛灉姝ｅ湪鍔犺浇
        if (isPullLoading()) {
            return;
        }
        
        mPullUpState = State.REFRESHING;
        onStateChanged(State.REFRESHING, false);
        
        if (null != mFooterLayout) {
            mFooterLayout.setState(State.REFRESHING);
        }
        
        if (null != mRefreshListener) {
            // 鍥犱负婊氬姩鍥炲師濮嬩綅缃殑鏃堕棿鏄�200锛屾垜浠渶瑕佺瓑鍥炴粴瀹屽悗鎵嶆墽琛屽姞杞藉洖璋�
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRefreshListener.onPullUpToRefresh(PullToRefreshBase.this);
                }
            }, getSmoothScrollDuration()); 
        }
    }
    
    /**
     * 褰撶姸鎬佸彂鐢熷彉鍖栨椂璋冪敤
     * 
     * @param state 鐘舵��
     * @param isPullDown 鏄惁鍚戜笅
     */
    protected void onStateChanged(State state, boolean isPullDown) {
        
    }
    
    /**
     * 璁剧疆婊氬姩浣嶇疆
     * 
     * @param x 婊氬姩鍒扮殑x浣嶇疆
     * @param y 婊氬姩鍒扮殑y浣嶇疆
     */
    private void setScrollTo(int x, int y) {
        scrollTo(x, y);
    }
    
    /**
     * 璁剧疆婊氬姩鐨勫亸绉�
     * 
     * @param x 婊氬姩x浣嶇疆
     * @param y 婊氬姩y浣嶇疆
     */
    private void setScrollBy(int x, int y) {
        scrollBy(x, y);
    }
    
    /**
     * 寰楀埌褰撳墠Y鐨勬粴鍔ㄥ��
     * 
     * @return 婊氬姩鍊�
     */
    private int getScrollYValue() {
        return getScrollY();
    }
    
    /**
     * 骞虫粦婊氬姩
     * 
     * @param newScrollValue 婊氬姩鐨勫��
     */
    private void smoothScrollTo(int newScrollValue) {
        smoothScrollTo(newScrollValue, getSmoothScrollDuration(), 0);
    }
    
    /**
     * 骞虫粦婊氬姩
     * 
     * @param newScrollValue 婊氬姩鐨勫��
     * @param duration 婊氬姩鏃跺��
     * @param delayMillis 寤惰繜鏃堕棿锛�0浠ｈ〃涓嶅欢杩�
     */
    private void smoothScrollTo(int newScrollValue, long duration, long delayMillis) {
        if (null != mSmoothScrollRunnable) {
            mSmoothScrollRunnable.stop();
        }
        
        int oldScrollValue = this.getScrollYValue();
        boolean post = (oldScrollValue != newScrollValue);
        if (post) {
            mSmoothScrollRunnable = new SmoothScrollRunnable(oldScrollValue, newScrollValue, duration);
        }
        
        if (post) {
            if (delayMillis > 0) {
                postDelayed(mSmoothScrollRunnable, delayMillis);
            } else {
                post(mSmoothScrollRunnable);
            }
        }
    }
    
    /**
     * 璁剧疆鏄惁鎴柇touch浜嬩欢
     * 
     * @param enabled true鎴柇锛宖alse涓嶆埅鏂�
     */
    private void setInterceptTouchEventEnabled(boolean enabled) {
        mInterceptEventEnable = enabled;
    }
    
    /**
     * 鏍囧織鏄惁鎴柇touch浜嬩欢
     * 
     * @return true鎴柇锛宖alse涓嶆埅鏂�
     */
    private boolean isInterceptTouchEventEnabled() {
        return mInterceptEventEnable;
    }
    
    /**
     * 瀹炵幇浜嗗钩婊戞粴鍔ㄧ殑Runnable
     * 
     * @author Li Hong
     * @since 2013-8-22
     */
    final class SmoothScrollRunnable implements Runnable {
        /**鍔ㄧ敾鏁堟灉*/
        private final Interpolator mInterpolator;
        /**缁撴潫Y*/
        private final int mScrollToY;
        /**寮�濮媃*/
        private final int mScrollFromY;
        /**婊戝姩鏃堕棿*/
        private final long mDuration;
        /**鏄惁缁х画杩愯*/
        private boolean mContinueRunning = true;
        /**寮�濮嬫椂鍒�*/
        private long mStartTime = -1;
        /**褰撳墠Y*/
        private int mCurrentY = -1;

        /**
         * 鏋勯�犳柟娉�
         * 
         * @param fromY 寮�濮媃
         * @param toY 缁撴潫Y
         * @param duration 鍔ㄧ敾鏃堕棿
         */
        public SmoothScrollRunnable(int fromY, int toY, long duration) {
            mScrollFromY = fromY;
            mScrollToY = toY;
            mDuration = duration;
            mInterpolator = new DecelerateInterpolator();
        }

        @Override
        public void run() {
            /**
             * If the duration is 0, we scroll the view to target y directly.
             */
            if (mDuration <= 0) {
                setScrollTo(0, mScrollToY);
                return;
            }
            
            /**
             * Only set mStartTime if this is the first time we're starting,
             * else actually calculate the Y delta
             */
            if (mStartTime == -1) {
                mStartTime = System.currentTimeMillis();
            } else {
                
                /**
                 * We do do all calculations in long to reduce software float
                 * calculations. We use 1000 as it gives us good accuracy and
                 * small rounding errors
                 */
                final long oneSecond = 1000;    // SUPPRESS CHECKSTYLE
                long normalizedTime = (oneSecond * (System.currentTimeMillis() - mStartTime)) / mDuration;
                normalizedTime = Math.max(Math.min(normalizedTime, oneSecond), 0);

                final int deltaY = Math.round((mScrollFromY - mScrollToY)
                        * mInterpolator.getInterpolation(normalizedTime / (float) oneSecond));
                mCurrentY = mScrollFromY - deltaY;
                
                setScrollTo(0, mCurrentY);
            }

            // If we're not at the target Y, keep going...
            if (mContinueRunning && mScrollToY != mCurrentY) {
                PullToRefreshBase.this.postDelayed(this, 16);// SUPPRESS CHECKSTYLE
            }
        }

        /**
         * 鍋滄婊戝姩
         */
        public void stop() {
            mContinueRunning = false;
            removeCallbacks(this);
        }
    }
}
