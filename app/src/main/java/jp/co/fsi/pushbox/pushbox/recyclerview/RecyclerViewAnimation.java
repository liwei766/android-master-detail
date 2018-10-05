package jp.co.fsi.pushbox.pushbox.recyclerview;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * リサイクラービューのアイテムに設定するアニメーション
 */
public class RecyclerViewAnimation extends Animation {

    /**
     * アニメーションを設定するビュー
     */
    private View targetView;
    /**
     * 変更前のビューの横幅
     */
    private int startWidth;
    /**
     * 変更後のビューに追加する横幅
     */
    private int endWidth;

    /**
     * コンストラクタ
     *
     * @param targetView アニメーションを設定するビュー
     * @param startWidth 変更前のビューの横幅
     * @param endWidth   変更後のビューに追加する横幅
     */
    RecyclerViewAnimation(View targetView, int startWidth, int endWidth) {
        this.startWidth = startWidth;
        this.endWidth = endWidth;
        this.targetView = targetView;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        targetView.getLayoutParams().width = (int) (startWidth + endWidth * interpolatedTime);
        targetView.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
