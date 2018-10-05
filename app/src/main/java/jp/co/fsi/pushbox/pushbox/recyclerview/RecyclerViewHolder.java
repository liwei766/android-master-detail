package jp.co.fsi.pushbox.pushbox.recyclerview;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import jp.co.fsi.pushbox.pushbox.R;

/**
 * リサイクラービューのビューホルダ
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    /**
     * お知らせ表示部分
     */
    public ConstraintLayout cellPush;
    /**
     * 表示/消去ボタン表示部分
     */
    public ConstraintLayout cellButton;
    /**
     * お知らせ画像
     */
    public ImageView icon;
    /**
     * 受信日付
     */
    public TextView date;
    /**
     * お知らせ種別
     */
    public TextView type;
    /**
     * お知らせタイトル
     */
    public TextView title;
    /**
     * お知らせ内容
     */
    public TextView content;
    /**
     * 表示ボタン
     */
    public Button buttonShow;
    /**
     * 消去ボタン
     */
    public Button buttonDelete;

    /**
     * コンストラクタ
     *
     * @param itemView RecyclerViewで扱うビュー
     */
    public RecyclerViewHolder(View itemView) {
        super(itemView);
        cellPush = itemView.findViewById(R.id.cellPush);
        cellButton = itemView.findViewById(R.id.cellButton);
        icon = itemView.findViewById(R.id.icon);
        date = itemView.findViewById(R.id.date);
        type = itemView.findViewById(R.id.type);
        title = itemView.findViewById(R.id.title);
        content = itemView.findViewById(R.id.content);
        buttonShow = itemView.findViewById(R.id.buttonShow);
        buttonDelete = itemView.findViewById(R.id.buttonDelete);
    }
}
