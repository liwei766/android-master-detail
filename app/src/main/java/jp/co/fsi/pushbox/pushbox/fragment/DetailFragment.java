package jp.co.fsi.pushbox.pushbox.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.transition.Slide;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import jp.co.fsi.pushbox.pushbox.R;
import jp.co.fsi.pushbox.pushbox.data.PushItem;

import static android.view.Gravity.BOTTOM;

/**
 * プッシュ詳細画面
 */
public class DetailFragment extends BaseFragment {

    /**
     * バンドルキー
     */
    private static final String KEY_URI_STRING = "uriString";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DETAIL = "detail";

    /**
     * コンストラクタ
     */
    public DetailFragment() {
        // 画面下からせり上がるアニメーションを設定
        setEnterTransition(new Slide(BOTTOM));
        setExitTransition(new Slide(BOTTOM));
    }

    /**
     * スプラッシュ画面のインスタンスを取得する
     *
     * @return スプラッシュ画面
     */
    public static DetailFragment newInstance(PushItem item) {
        DetailFragment fragment = new DetailFragment();

        String title = item.getTitle();
        String detail = item.getDetail();
        if (TextUtils.isEmpty(detail)) {
            detail = item.getContent();
        }

        Bundle args = new Bundle();
        args.putString(KEY_URI_STRING, item.getUriString());
        args.putString(KEY_TITLE, title);
        args.putString(KEY_DETAIL, detail);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.detail_fragment, container, false);
        final ImageView icon = layout.findViewById(R.id.icon);
        final TextView title = layout.findViewById(R.id.title);
        final TextView detail = layout.findViewById(R.id.detail);

        Bundle args = getArguments();
        assert args != null;

        layout.findViewById(R.id.buttonClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener.isBusy()) {
                    return;
                }

                // 詳細画面を閉じる
                listener.hideDetail();
            }
        });

        // 画像を設定する
        String uriString = args.getString(KEY_URI_STRING);
        if (!TextUtils.isEmpty(uriString)) {
            Picasso.get().load(uriString).into(icon);
        } else {
            icon.setImageResource(R.drawable.smart_coco);
        }

        // タイトルを設定する
        String titleString = args.getString(KEY_TITLE);
        if (!TextUtils.isEmpty(titleString)) {
            title.setText(titleString);
        } else {
            title.setText(R.string.ex_title);
        }

        // 詳細情報を表示する
        String detailString = args.getString(KEY_DETAIL);
        if (!TextUtils.isEmpty(detailString)) {
            detail.setText(detailString);
        } else {
            detail.setText(R.string.ex_title);
        }

        // 端末戻るボタン設定
        layout.setFocusableInTouchMode(true);
        layout.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    listener.hideDetail();
                    return true;
                }
                return false;
            }
        });
        layout.requestFocus();

        return layout;
    }
}
