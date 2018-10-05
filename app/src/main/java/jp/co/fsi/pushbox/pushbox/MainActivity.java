package jp.co.fsi.pushbox.pushbox;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import jp.co.fsi.pushbox.pushbox.fragment.BaseFragment;
import jp.co.fsi.pushbox.pushbox.fragment.DetailFragment;
import jp.co.fsi.pushbox.pushbox.fragment.SplashFragment;

/**
 * メインアクティビティ
 */
public class MainActivity extends AppCompatActivity implements MainActivityListener {

    /**
     * フラグメントタグ
     */
    public static final String TAG_CONTENT = "content";
    public static final String TAG_DETAIL = "detail";
    /**
     * 連続タップ防止の待機時間[ms]
     */
    private static final int GUARD_DELAY = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        transition(SplashFragment.newInstance());
    }

    @Override
    public void transition(@NonNull BaseFragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.contentFragment, fragment, TAG_CONTENT);
        ft.commit();
    }

    @Override
    public void showDetail(@NonNull DetailFragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.detailFragment, fragment, TAG_DETAIL);
        ft.commit();
    }

    @Override
    public void hideDetail() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(TAG_DETAIL);
        if (!(fragment instanceof BaseFragment)) {
            return;
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }

    @Override
    public boolean isBusy() {
        final View guard = findViewById(R.id.guard);

        if (guard.getVisibility() == View.VISIBLE) {
            // 画面ガード表示中
            return true;
        }

        // ガードを表示する
        guard.setVisibility(View.VISIBLE);
        guard.postDelayed(new Runnable() {
            @Override
            public void run() {
                guard.setVisibility(View.INVISIBLE);
            }
        }, GUARD_DELAY);
        return false;
    }
}
