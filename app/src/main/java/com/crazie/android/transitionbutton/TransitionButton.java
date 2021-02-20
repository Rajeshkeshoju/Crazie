package com.crazie.android.transitionbutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.crazie.android.R;
import com.crazie.android.utils.ColorUtils;
import com.crazie.android.utils.WindowUtils;

public class TransitionButton extends AppCompatButton {

    private State currentState;

    private boolean isMorphingInProgress;

    private int initialWidth;
    private String initialText;

    private int loaderColor;

    private CircularAnimatedDrawable progressCircularAnimatedDrawable;

    public TransitionButton(Context context) {
        super(context);
        init(context, null);
    }

    public TransitionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TransitionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        currentState = State.IDLE;

        //int errorColor = ContextCompat.getColor(getContext(), R.color.colorError);
        loaderColor = ContextCompat.getColor(getContext(), R.color.colorLoader);

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.colorAccent, typedValue, true);
        int defaultColor = typedValue.data;

        if (attrs != null) {
            TypedArray attrsArray = context.obtainStyledAttributes(attrs, R.styleable.TransitionButton);

            CharSequence dc = attrsArray.getString(R.styleable.TransitionButton_defaultColor);
            if (dc != null)
                defaultColor = ColorUtils.parse(dc.toString());

            CharSequence lc = attrsArray.getString(R.styleable.TransitionButton_loaderColor);
            if (lc != null)
                loaderColor = ColorUtils.parse(lc.toString());

            attrsArray.recycle();
        }

        setBackgroundTintList(ColorStateList.valueOf(defaultColor));

        Drawable background = ContextCompat.getDrawable(context, R.drawable.transition_button_shape_idle);
        setBackground(background);
  }

    public void startAnimation() {
        currentState = State.PROGRESS;
        isMorphingInProgress = true;

        initialWidth = getWidth();
        int initialHeight = getHeight();
        initialText = getText().toString();

        setText(null);
        setClickable(false);

        startWidthAnimation(initialHeight, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationCancel(animation);
                isMorphingInProgress = false;
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (currentState == State.PROGRESS && !isMorphingInProgress) {
            drawIndeterminateProgress(canvas);
        }
    }

    private void drawIndeterminateProgress(Canvas canvas) {
        if (progressCircularAnimatedDrawable == null || !progressCircularAnimatedDrawable.isRunning()) {
            int arcWidth = getHeight() / 18;

            progressCircularAnimatedDrawable = new CircularAnimatedDrawable(loaderColor, arcWidth);

            int offset = (getWidth() - getHeight()) / 2;

            int right = getWidth() - offset;
            int bottom = getHeight();
            int top = 0;

            progressCircularAnimatedDrawable.setBounds(offset, top, right, bottom);
            progressCircularAnimatedDrawable.setCallback(this);
            progressCircularAnimatedDrawable.start();

        } else {
            progressCircularAnimatedDrawable.draw(canvas);
            invalidate();
        }
    }

    public void stopAnimation(StopAnimationStyle stopAnimationStyle, final OnAnimationStopEndListener onAnimationStopEndListener) {
        switch (stopAnimationStyle) {
            case SHAKE:
                currentState = State.ERROR;

                startWidthAnimation(initialWidth, new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        setText(initialText);
                        startShakeAnimation(new AnimationListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animation animation) {
                                currentState = State.IDLE;
                                setClickable(true);
                                if (onAnimationStopEndListener != null)
                                    onAnimationStopEndListener.onAnimationStopEnd();
                            }
                        });
                    }
                });
                break;
            case EXPAND:
                currentState = State.TRANSITION;

                startScaleAnimation(new AnimationListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        super.onAnimationEnd(animation);
                        if (onAnimationStopEndListener != null)
                            onAnimationStopEndListener.onAnimationStopEnd();
                    }
                });
                break;
        }
    }

    private void startWidthAnimation(int to, AnimatorListenerAdapter onAnimationEnd) {
        startWidthAnimation(getWidth(), to, onAnimationEnd);
    }

    private void startWidthAnimation(int from, int to, AnimatorListenerAdapter onAnimationEnd) {
        ValueAnimator widthAnimation = ValueAnimator.ofInt(from, to);
        widthAnimation.addUpdateListener(valueAnimator -> {
            int val = (Integer) valueAnimator.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            layoutParams.width = val;
            setLayoutParams(layoutParams);
        });

        AnimatorSet animatorSet = new AnimatorSet();
        int WIDTH_ANIMATION_DURATION = 200;
        animatorSet.setDuration(WIDTH_ANIMATION_DURATION);
        animatorSet.playTogether(widthAnimation);
        if (onAnimationEnd != null)
            animatorSet.addListener(onAnimationEnd);

        animatorSet.start();
    }

    private void startShakeAnimation(Animation.AnimationListener animationListener) {
        TranslateAnimation shake = new TranslateAnimation(0, 15, 0, 0);
        int SHAKE_ANIMATION_DURATION = 500;
        shake.setDuration(SHAKE_ANIMATION_DURATION);
        shake.setInterpolator(new CycleInterpolator(4));
        shake.setAnimationListener(animationListener);
        startAnimation(shake);
    }

    private void startScaleAnimation(Animation.AnimationListener animationListener) {
        float ts = (float) (WindowUtils.getHeight(getContext()) / getHeight() * 2.1);
        Animation anim = new ScaleAnimation(1f, ts,
            1, ts,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f);
        int SCALE_ANIMATION_DURATION = 300;
        anim.setDuration(SCALE_ANIMATION_DURATION);
        anim.setFillAfter(true);
        anim.setAnimationListener(animationListener);
        startAnimation(anim);
    }

    public static class AnimationListenerAdapter implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

    }

    public interface OnAnimationStopEndListener {
        void onAnimationStopEnd();
    }

    private enum State {
        PROGRESS, IDLE, ERROR, TRANSITION
    }

    public enum StopAnimationStyle {
        EXPAND, SHAKE
    }

}
