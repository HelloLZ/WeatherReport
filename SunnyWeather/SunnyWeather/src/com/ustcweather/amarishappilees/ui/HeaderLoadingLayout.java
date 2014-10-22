package com.ustcweather.amarishappilees.ui;



import com.ustcweather.amarishappilees.R;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 杩欎釜绫诲皝瑁呬簡涓嬫媺鍒锋柊鐨勫竷灞�
 * 
 * @author Li Hong
 * @since 2013-7-30
 */
public class HeaderLoadingLayout extends LoadingLayout {
    /** 鏃嬭浆鍔ㄧ敾鏃堕棿 */
    private static final int ROTATE_ANIM_DURATION = 150;
    /**Header鐨勫鍣�*/
    private RelativeLayout mHeaderContainer;
    /**绠ご鍥剧墖*/
    private ImageView mArrowImageView;
    /**杩涘害鏉�*/
    private ProgressBar mProgressBar;
    /**鐘舵�佹彁绀篢extView*/
    private TextView mHintTextView;
    /**鏈�鍚庢洿鏂版椂闂寸殑TextView*/
    private TextView mHeaderTimeView;
    /**鏈�鍚庢洿鏂版椂闂寸殑鏍囬*/
    private TextView mHeaderTimeViewTitle;
    /**鍚戜笂鐨勫姩鐢�*/
    private Animation mRotateUpAnim;
    /**鍚戜笅鐨勫姩鐢�*/
    private Animation mRotateDownAnim;
    
    /**
     * 鏋勯�犳柟娉�
     * 
     * @param context context
     */
    public HeaderLoadingLayout(Context context) {
        super(context);
        init(context);
    }

    /**
     * 鏋勯�犳柟娉�
     * 
     * @param context context
     * @param attrs attrs
     */
    public HeaderLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 鍒濆鍖�
     * 
     * @param context context
     */
    private void init(Context context) {
        mHeaderContainer = (RelativeLayout) findViewById(R.id.pull_to_refresh_header_content);
        mArrowImageView = (ImageView) findViewById(R.id.pull_to_refresh_header_arrow);
        mHintTextView = (TextView) findViewById(R.id.pull_to_refresh_header_hint_textview);
        mProgressBar = (ProgressBar) findViewById(R.id.pull_to_refresh_header_progressbar);
        mHeaderTimeView = (TextView) findViewById(R.id.pull_to_refresh_header_time);
        mHeaderTimeViewTitle = (TextView) findViewById(R.id.pull_to_refresh_last_update_time_text);
        
        float pivotValue = 0.5f;    // SUPPRESS CHECKSTYLE
        float toDegree = -180f;     // SUPPRESS CHECKSTYLE
        // 鍒濆鍖栨棆杞姩鐢�
        mRotateUpAnim = new RotateAnimation(0.0f, toDegree, Animation.RELATIVE_TO_SELF, pivotValue,
                Animation.RELATIVE_TO_SELF, pivotValue);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(toDegree, 0.0f, Animation.RELATIVE_TO_SELF, pivotValue,
                Animation.RELATIVE_TO_SELF, pivotValue);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);
    }

    @Override
    public void setLastUpdatedLabel(CharSequence label) {
        // 濡傛灉鏈�鍚庢洿鏂扮殑鏃堕棿鐨勬枃鏈槸绌虹殑璇濓紝闅愯棌鍓嶉潰鐨勬爣棰�
        mHeaderTimeViewTitle.setVisibility(TextUtils.isEmpty(label) ? View.INVISIBLE : View.VISIBLE);
        mHeaderTimeView.setText(label);
    }

    @Override
    public int getContentSize() {
        if (null != mHeaderContainer) {
            return mHeaderContainer.getHeight();
        }
        
        return (int) (getResources().getDisplayMetrics().density * 60);
    }
    
    @Override
    protected View createLoadingView(Context context, AttributeSet attrs) {
        View container = LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_header, null);
        return container;
    }
    
    @Override
    protected void onStateChanged(State curState, State oldState) {
        mArrowImageView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        
        super.onStateChanged(curState, oldState);
    }

    @Override
    protected void onReset() {
        mArrowImageView.clearAnimation();
        mHintTextView.setText(R.string.pull_to_refresh_header_hint_normal);
    }

    @Override
    protected void onPullToRefresh() {
        if (State.RELEASE_TO_REFRESH == getPreState()) {
            mArrowImageView.clearAnimation();
            mArrowImageView.startAnimation(mRotateDownAnim);
        }
        
        mHintTextView.setText(R.string.pull_to_refresh_header_hint_normal);
    }

    @Override
    protected void onReleaseToRefresh() {
        mArrowImageView.clearAnimation();
        mArrowImageView.startAnimation(mRotateUpAnim);
        mHintTextView.setText(R.string.pull_to_refresh_header_hint_ready);
    }

    @Override
    protected void onRefreshing() {
        mArrowImageView.clearAnimation();
        mArrowImageView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        mHintTextView.setText(R.string.pull_to_refresh_header_hint_loading);
    }
}
