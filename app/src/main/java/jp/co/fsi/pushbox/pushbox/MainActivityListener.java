package jp.co.fsi.pushbox.pushbox;

import android.support.annotation.NonNull;

import jp.co.fsi.pushbox.pushbox.fragment.BaseFragment;
import jp.co.fsi.pushbox.pushbox.fragment.DetailFragment;

/**
 * メインアクティビティリスナ
 */
public interface MainActivityListener {
    /**
     * 画面遷移する
     *
     * @param fragment BaseFragmentを継承しているクラス
     */
    void transition(@NonNull BaseFragment fragment);

    /**
     * 詳細画面を表示する
     *
     * @param fragment 詳細画面のフラグメント
     */
    void showDetail(@NonNull DetailFragment fragment);

    /**
     * 詳細画面を消す
     */
    void hideDetail();

    /**
     * 連続タップを防止する
     *
     * @return 連続タップ防止中
     */
    boolean isBusy();
}
