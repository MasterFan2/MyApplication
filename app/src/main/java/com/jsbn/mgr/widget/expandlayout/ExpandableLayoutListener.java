package com.jsbn.mgr.widget.expandlayout;

public interface ExpandableLayoutListener {
    /**
     * Notifies the start of the animation.
     * Sync from android.animation.Animator.AnimatorListener.onAnimationStart(Animator animation)
     */
    void onAnimationStart();

    /**
     * Notifies the end of the animation.
     * Sync from android.animation.Animator.AnimatorListener.onAnimationEnd(Animator animation)
     */
    void onAnimationEnd();

    /**
     * Notifies the layout opened.
     */
    void onOpened();

    /**
     * Notifies the layout size equal closed size.
     */
    void onClosed();
}