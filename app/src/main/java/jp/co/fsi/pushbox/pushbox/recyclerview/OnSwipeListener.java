package jp.co.fsi.pushbox.pushbox.recyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

/**
 * スワイプ操作リスナ
 */
public class OnSwipeListener {

    /**
     * スワイプリスナ
     */
    private SwipeCallBack callback;
    /**
     * タッチイベント処理用インタフェース
     */
    private GestureDetector mGestureDetector;
    /**
     * 横方向最低スワイプ距離
     */
    private static final int SWIPE_MIN_DISTANCE_X = 50;
    /**
     * 縦方向最大スワイプ距離
     * これを超える場合は横方向スワイプを判定しない
     */
    private static final int SWIPE_MAX_DISTANCE_Y = 250;
    /**
     * 横方向最低スワイプ速度
     */
    private static final int SWIPE_THRESHOLD_VELOCITY = 100;
    /**
     * スワイプ方向
     */
    private SwipeDirection mSwipeDirection = null;
    /**
     * タッチイベントのリスナ
     */
    private final SimpleOnGestureListener mOnGestureListener = new SimpleOnGestureListener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_DISTANCE_Y) {
                // 縦にスワイプされていたら無視(いらないかも)
                return false;
            }

            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE_X && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                // 右から左へスワイプ
                mSwipeDirection = SwipeDirection.LEFT;
            }

            if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE_X && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                // 左から右へスワイプ
                mSwipeDirection = SwipeDirection.RIGHT;
            }

            return false;
        }
    };
    /**
     * タッチイベント
     */
    private final View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mSwipeDirection = null;
            boolean handled = mGestureDetector.onTouchEvent(event);
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                callback.onTouch(v);
            } else if (action == MotionEvent.ACTION_UP && mSwipeDirection != null) {
                // 指を離したアクション後にスワイプ方向があるならコールバックする
                callback.onSwipe(v, mSwipeDirection);
            } else if (action == MotionEvent.ACTION_UP) {
                // スワイプしないACTION_UP
                callback.onClick(v);
            }
            return handled;
        }
    };
    /**
     * クリックイベント（処理を行わない）
     */
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //callback.onSwipe(v);
        }
    };

    /**
     * コンストラクタ
     *
     * @param context  コンテキスト
     * @param callback コールバック
     */
    OnSwipeListener(Context context, SwipeCallBack callback) {
        this.callback = callback;

        mGestureDetector = new GestureDetector(context, mOnGestureListener);
    }

    /**
     * 引数のViewにタッチイベントとクリックイベントを設定する
     *
     * @param view イベントを設定するビュー
     */
    public void setOnSwipeListener(View view, int position) {
        if (view == null) {
            throw new NullPointerException();
        }
        view.setOnTouchListener(mOnTouchListener);
        view.setOnClickListener(mOnClickListener);
        view.setTag(position);
    }

    /**
     * コールバックメソッド
     */
    public interface SwipeCallBack {
        /**
         * スワイプイベントを返す
         *
         * @param view スワイプ操作したビュー
         * @param sd   スワイプ方向
         */
        void onSwipe(View view, SwipeDirection sd);

        /**
         * クリックイベントを返す
         *
         * @param view クリック操作したビュー
         */
        void onClick(View view);

        /**
         * タッチイベント（ACTION_DOWN）を返す
         *
         * @param view タッチしたビュー
         */
        void onTouch(View view);
    }

    /**
     * スワイプ方向の列挙型
     */
    public enum SwipeDirection {
        RIGHT, LEFT
    }
}
