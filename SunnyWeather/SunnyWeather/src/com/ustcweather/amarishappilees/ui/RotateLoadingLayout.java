package com.ustcweather.amarishappilees.ui;



import com.ustcweather.amarishappilees.R;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 杩欎釜绫诲皝瑁呬簡涓嬫媺鍒锋柊鐨勫竷灞�
 * 
 * @author Li Hong
 * @since 2013-7-30
 */
public class RotateLoadingLayout extends LoadingLayout {
    /**鏃嬭浆鍔ㄧ敾鐨勬椂闂�*/
    static final int ROTATION_ANIMATION_DURATION = 1200;
    /**鍔ㄧ敾鎻掑��*/
    static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();
    /**Header鐨勫鍣�*/
    private RelativeLayout mHeaderContainer;
    /**绠ご鍥剧墖*/
    private ImageView mArrowImageView;
    /**鐘舵�佹彁绀篢extView*/
    private TextView mHintTextView;
    /**鏈�鍚庢洿鏂版椂闂寸殑TextView*/
    private TextView mHeaderTimeView;
    /**鏈�鍚庢洿鏂版椂闂寸殑鏍囬*/
    private TextView mHeaderTimeViewTitle;
    /**鏃嬭浆鐨勫姩鐢�*/
    private Animation mRotateAnimation;
    
    /**
     * 鏋勯�犳柟娉�
     * 
     * @param context context
     */
    public RotateLoadingLayout(Context context) {
        super(context);
        init(context);
    }

    /**
     * 鏋勯�犳柟娉�
     * 
     * @param context context
     * @param attrs attrs
     */
    public RotateLoadingLayout(Context context, AttributeSet attrs) {
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
        mHeaderTimeView = (TextView) findViewById(R.id.pull_to_refresh_header_time);
        mHeaderTimeViewTitle = (TextView) findViewById(R.id.pull_to_refresh_last_update_time_text);
        
        mArrowImageView.setScaleType(ScaleType.CENTER);
        mArrowImageView.setImageResource(R.drawable.default_ptr_rotate);
        
        float pivotValue = 0.5f;    // SUPPRESS CHECKSTYLE
        float toDegree = 720.0f;    // SUPPRESS CHECKSTYLE
        mRotateAnimation = new RotateAnimation(0.0f, toDegree, Animation.RELATIVE_TO_SELF, pivotValue,
                Animation.RELATIVE_TO_SELF, pivotValue);
        mRotateAnimation.setFillAfter(true);
        mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
        mRotateAnimation.setDuration(ROTATION_ANIMATION_DURATION);
        mRotateAnimation.setRepeatCount(Animation.INFINITE);
        mRotateAnimation.setRepeatMode(Animation.RESTART);
    }
    
    @Override
    protected View createLoadingView(Context context, AttributeSet attrs) {
        View container = LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_header2, null);
        return container;
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
    protected void onStateChanged(State curState, State oldState) {
        super.onStateChanged(curState, oldState);
    }

    @Override
    protected void onReset() {
        resetRotation();
        mHintTextView.setText(R.string.pull_to_refresh_header_hint_normal);
    }

    @Override
    protected void onReleaseToRefresh() {
        mHintTextView.setText(R.string.pull_to_refresh_header_hint_ready);
    }
    
    @Override
    protected void onPullToRefresh() {
        mHintTextView.setText(R.string.pull_to_refresh_header_hint_normal);
    }
    
    @Override
    protected void onRefreshing() {
        resetRotation();
        mArrowImageView.startAnimation(mRotateAnimation);
        mHintTextView.setText(R.string.pull_to_refresh_header_hint_loading);
    }
    
    @Override
    public void onPull(float scale) {
        float angle = scale * 180f; // SUPPRESS CHECKSTYLE
        mArrowImageView.setRotation(angle);
    }
    
    /**
     * 閲嶇疆鍔ㄧ敾
     */
    private void resetRotation() {
        mArrowImageView.clearAnimation();
        mArrowImageView.setRotation(0);
    }
}
