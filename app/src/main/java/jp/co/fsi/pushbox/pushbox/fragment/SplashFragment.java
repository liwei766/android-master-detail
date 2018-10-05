package jp.co.fsi.pushbox.pushbox.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.co.fsi.pushbox.pushbox.R;

/**
 * スプラッシュ画面
 */
public class SplashFragment extends BaseFragment {

    /**
     * 自動遷移待機時間5秒
     */
    private static final int TIME = 2000;

    /**
     * スプラッシュ画面のインスタンスを取得する
     *
     * @return スプラッシュ画面
     */
    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.splash_fragment, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // ユーザ種別が設定済ならエリア設定画面へ遷移する
                listener.transition(ListFragment.newInstance());
            }
        }, TIME);
    }
}
