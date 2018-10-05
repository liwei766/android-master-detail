package jp.co.fsi.pushbox.pushbox.recyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import jp.co.fsi.pushbox.pushbox.CustomApplication;
import jp.co.fsi.pushbox.pushbox.MainActivity;
import jp.co.fsi.pushbox.pushbox.MainActivityListener;
import jp.co.fsi.pushbox.pushbox.R;
import jp.co.fsi.pushbox.pushbox.TimeUtil;
import jp.co.fsi.pushbox.pushbox.data.PushItem;
import jp.co.fsi.pushbox.pushbox.fragment.DetailFragment;
import jp.co.fsi.pushbox.pushbox.realmmodel.PushModel;

/**
 * リサイクラービューのアダプタ
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder>
        implements OnSwipeListener.SwipeCallBack {

    /**
     * アクティビティリスナ
     */
    private MainActivityListener listener;
    /**
     * リストに表示するアイテム
     */
    private List<PushItem> items;
    /**
     * ビュー間マージン（スワイプ操作のアニメーションで使用）
     */
    private static final int MARGIN_DEFAULT = 5;
    private final int margin;
    /**
     * このアダプターをセットしているリサイクラービュー
     */
    private RecyclerView mRecyclerView = null;
    /**
     * スワイプ操作リスナ
     */
    private OnSwipeListener mOnSwipeListener;
    /**
     * 日付表示フォーマット
     */
    private static final String format = "%d月%d日 %s曜日";

    /**
     * コンストラクタ
     *
     * @param listener アクティビティリスナ
     * @param items    一覧表示するアイテムリスト
     */
    public RecyclerViewAdapter(@NonNull MainActivityListener listener, @NonNull List<PushItem> items) {
        this.listener = listener;
        this.items = items;

        Context context = (MainActivity) listener;
        float density = context.getResources().getDisplayMetrics().density;
        margin = (int) (MARGIN_DEFAULT * density);
        mOnSwipeListener = new OnSwipeListener(context, this);
    }

    /**
     * リサイクラービューにアクセスできるように設定する
     *
     * @param recyclerView このアダプターをセットするリサイクラービュー
     */
    public void setMyRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_cell_view, parent, false);
        return new RecyclerViewHolder(inflate);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final PushItem item = items.get(position);

        final TextView textDate = holder.date;
        final ConstraintLayout cellPush = holder.cellPush;
        final ConstraintLayout cellButton = holder.cellButton;
        if (item.getId() == null) {
            Date date = item.getDate();
            if (date != null) {
                String dayOfWeek;
                Calendar cal = TimeUtil.getCalendar();
                cal.setTime(item.getDate());
                switch (cal.get(Calendar.DAY_OF_WEEK)) {
                    case Calendar.MONDAY:
                        dayOfWeek = "月";
                        break;
                    case Calendar.TUESDAY:
                        dayOfWeek = "火";
                        break;
                    case Calendar.WEDNESDAY:
                        dayOfWeek = "水";
                        break;
                    case Calendar.THURSDAY:
                        dayOfWeek = "木";
                        break;
                    case Calendar.FRIDAY:
                        dayOfWeek = "金";
                        break;
                    case Calendar.SATURDAY:
                        dayOfWeek = "土";
                        break;
                    default:
                        dayOfWeek = "日";
                        break;
                }

                textDate.setText(String.format(Locale.JAPAN, format,
                        cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), dayOfWeek));
                textDate.setVisibility(View.VISIBLE);
                cellPush.setVisibility(View.GONE);
                cellButton.setVisibility(View.GONE);

                textDate.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            if (listener.isBusy()) {
                                return false;
                            }
                            otherItemButtonVisibilityChange(position);
                        }
                        return false;
                    }
                });
            } else {
                holder.itemView.setVisibility(View.GONE);
            }
            return;
        } else {
            textDate.setVisibility(View.GONE);
            cellPush.setVisibility(View.VISIBLE);
        }

        // ここで表示を行う
        String uriString = item.getUriString();
        if (!TextUtils.isEmpty(uriString)) {
            Picasso.get().load(uriString).into(holder.icon);
        } else {
            holder.icon.setImageResource(R.drawable.smart_coco);
        }
        holder.type.setText(item.getType());
        holder.title.setText(item.getTitle());
        holder.content.setText(item.getContent());

        // スワイプリスナをセットする
        mOnSwipeListener.setOnSwipeListener(cellPush, position);
        mOnSwipeListener.setOnSwipeListener(holder.buttonShow, position);
        mOnSwipeListener.setOnSwipeListener(holder.buttonDelete, position);

        // 表示/消去ボタンの表示状態を設定
        boolean isButtonVisible = item.isButtonVisible();
        cellButton.setVisibility(isButtonVisible ? View.VISIBLE : View.INVISIBLE);
        cellPush.setLayoutParams(new LinearLayout.LayoutParams(
                isButtonVisible ? item.getCellPushWidth() : ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onSwipe(View view, OnSwipeListener.SwipeDirection sd) {
        if (listener.isBusy() || mRecyclerView == null) {
            return;
        }

        final int position = (int) view.getTag();
        RecyclerViewHolder holder = (RecyclerViewHolder) mRecyclerView.findViewHolderForAdapterPosition(position);
        if (holder == null) {
            return;
        }
        final View itemView = holder.itemView;
        final View cellPush = itemView.findViewById(R.id.cellPush);
        final View cellButton = itemView.findViewById(R.id.cellButton);
        PushItem item = items.get(position);
        int cellPushWidth;

        if (cellPush == null || cellButton == null) {
            return;
        }

        switch (sd) {
            case LEFT:
                if (cellButton.getVisibility() == View.VISIBLE) {
                    return;
                }
                cellPushWidth = resizeCellPush(cellPush, cellButton, sd);
                item.setButtonVisible(true);
                item.setCellPushWidth(cellPushWidth);

                // 他のアイテムのボタン表示状態を非表示にする
//                otherItemButtonVisibilityChange(position);
                break;

            case RIGHT:
                if (cellButton.getVisibility() == View.INVISIBLE) {
                    return;
                }
                cellPushWidth = resizeCellPush(cellPush, cellButton, sd);
                item.setButtonVisible(false);
                item.setCellPushWidth(cellPushWidth);
                break;
        }
        items.set(position, item);
    }

    @Override
    public void onClick(View view) {
        if (listener.isBusy() || mRecyclerView == null) {
            return;
        }

        final int position = (int) view.getTag();
        RecyclerViewHolder holder = (RecyclerViewHolder) mRecyclerView.findViewHolderForAdapterPosition(position);
        if (holder == null) {
            return;
        }
        final View itemView = holder.itemView;
        final View cellPush = itemView.findViewById(R.id.cellPush);
        final View cellButton = itemView.findViewById(R.id.cellButton);
        final PushItem item = items.get(position);

        switch (view.getId()) {
            case R.id.cellPush:
                // プッシュ押下処理
                if (cellPush == null || cellButton == null) {
                    return;
                }

                if (items.get(position).isButtonVisible()) {
                    // 表示/消去ボタン表示中はボタン非表示に切り替える
                    int cellPushWidth = resizeCellPush(cellPush, cellButton,
                            OnSwipeListener.SwipeDirection.RIGHT);
                    item.setCellPushWidth(cellPushWidth);
                    item.setButtonVisible(false);
                    items.set(position, item);
                } else {
                    // 表示/消去ボタン非表示中は詳細画面表示する
                    listener.showDetail(DetailFragment.newInstance(item));
                    // クリックだが、他のアイテムのボタン表示状態を非表示にする
//                    otherItemButtonVisibilityChange(position);
                }
                break;

            case R.id.buttonShow:
                // 表示ボタン押下処理
                // 表示/消去ボタンを非表示に切り替える
                int cellPushWidth = resizeCellPush(cellPush, cellButton,
                        OnSwipeListener.SwipeDirection.RIGHT);
                item.setCellPushWidth(cellPushWidth);
                item.setButtonVisible(false);
                items.set(position, item);

                // 詳細画面を表示する
                listener.showDetail(DetailFragment.newInstance(item));
                break;

            case R.id.buttonDelete:
                // 消去ボタン押下処理
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Long id = item.getId();
                        if (id == null) {
                            // ありえないが、IDがnullなら処理を終了する
                            return;
                        }
                        // プッシュ通知をDBから削除する
                        try (Realm realm = CustomApplication.getRealmInstance()) {
                            realm.beginTransaction();
                            PushModel.delete(realm, item.getId());
                            realm.commitTransaction();
                        }
                    }
                });
                thread.start();

                try {
                    thread.join();

                    // 日付表示の削除判定用に日付を取得
                    Date date = item.getDate();

                    // リストから削除する
                    items.remove(position);
                    notifyItemRemoved(position);

                    // 日付表示削除判定
                    if (hasNotSameDateItem(date, position)) {
                        items.remove(position - 1);
                        notifyItemRemoved(position - 1);
                    }

                    for (int i = 0; i < items.size(); i++) {
                        notifyItemChanged(i);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;

            default:
                // 処理なし
        }
    }

    @Override
    public void onTouch(View view) {
        final int position = (int) view.getTag();
        otherItemButtonVisibilityChange(position);
    }

    /**
     * あるアイテムの表示/消去ボタンが表示状態なったとき
     * 他のアイテムのボタン表示状態を非表示に変更する
     *
     * @param position 表示状態に変更があったアイテムのポジション
     */
    private void otherItemButtonVisibilityChange(int position) {
        if (mRecyclerView != null) {
            for (int i = 0; i < items.size(); i++) {
                // 表示状態を変更したアイテムは無視する
                if (i == position) {
                    continue;
                }

                // ボタン非表示のアイテムは無視する
                PushItem otherItem = items.get(i);
                if (!otherItem.isButtonVisible()) {
                    continue;
                }
                // ボタン表示状態を非表示に変更する
                otherItem.setButtonVisible(false);
                otherItem.setCellPushWidth(0);
                items.set(i, otherItem);

                // 画面上に表示されていないアイテムはアニメーションしない
                RecyclerViewHolder holder = (RecyclerViewHolder) mRecyclerView.findViewHolderForAdapterPosition(i);
                if (holder == null) {
                    continue;
                }

                // 画面上に表示されているアイテムは非表示アニメーションを行う
                resizeCellPush(holder.cellPush, holder.cellButton, OnSwipeListener.SwipeDirection.RIGHT);
            }
        }
    }

    /**
     * プッシュ情報表示ビューのサイズを変更する
     *
     * @param cellPush   プッシュ情報表示ビュー
     * @param cellButton 表示/消去ボタンビュー
     * @param direction  スワイプ方向
     * @return リサイズ後のプッシュ情報表示ビューの横幅
     */
    private int resizeCellPush(final View cellPush, final View cellButton,
                               OnSwipeListener.SwipeDirection direction) {
        final boolean isLeftSwipe = direction.equals(OnSwipeListener.SwipeDirection.LEFT);

        // 現在の横幅
        int startWidth = cellPush.getWidth();
        // 変更後に追加する横幅
        int endWidth = cellButton.getWidth() + margin;
        if (isLeftSwipe) {
            // 左スワイプなら負数に変える
            endWidth = -endWidth;
        }

        // ビューにアニメーションを設定する
        RecyclerViewAnimation animation = new RecyclerViewAnimation(cellPush, startWidth, endWidth);
        animation.setDuration(100);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (isLeftSwipe) {
                    cellButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!isLeftSwipe) {
                    cellButton.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // NOP
            }
        });
        cellPush.clearAnimation();
        cellPush.startAnimation(animation);

        return startWidth + endWidth;
    }

    /**
     * 消去したプッシュと同じ日付のプッシュがないか判定する
     * なければ真を返す
     *
     * @param date     消去したプッシュの日付
     * @param position 消去したプッシュのポジション
     * @return 同じ日付の有無
     */
    private boolean hasNotSameDateItem(Date date, int position) {
        boolean isDelete = true;
        if (date == null) {
            isDelete = false;
        } else {
            if (position > 0) {
                // 削除したアイテムの前のアイテムを確認する
                isDelete = !compareToPositionDate(date, position - 1);
            }
            if (position < items.size() && isDelete) {
                // 削除したアイテムの次のアイテムを確認する
                isDelete = !compareToPositionDate(date, position);
            }
        }

        return isDelete;
    }

    /**
     * 引数に指定したアイテムの日付と
     * 別のアイテムの日付を比較する
     *
     * @param date     プッシュの日付
     * @param position 比較対象のポジション
     * @return 比較結果
     */
    private boolean compareToPositionDate(Date date, int position) {
        boolean isMatch = false;

        Calendar cal = TimeUtil.getCalendar();
        cal.setTime(date);
        cal = TimeUtil.timeCut(cal);

        PushItem item = items.get(position);
        Date compDate = item.getDate();
        if (item.getId() != null && compDate != null) {
            Calendar compCal = TimeUtil.getCalendar();
            compCal.setTime(compDate);
            compCal = TimeUtil.timeCut(compCal);
            isMatch = cal.compareTo(compCal) == 0;
        }

        return isMatch;
    }
}
