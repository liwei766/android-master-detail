package jp.co.fsi.pushbox.pushbox.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;

import jp.co.fsi.pushbox.pushbox.MainActivityListener;

/**
 * フラグメント基盤クラス
 */
public class BaseFragment extends Fragment {

    /**
     * アクティビティリスナー
     */
    protected MainActivityListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivityListener) {
            listener = (MainActivityListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MainActivityListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
