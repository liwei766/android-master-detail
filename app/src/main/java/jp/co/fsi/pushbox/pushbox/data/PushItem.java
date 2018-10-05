package jp.co.fsi.pushbox.pushbox.data;

import android.support.annotation.NonNull;

import java.util.Date;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

/**
 * プッシュ一覧に表示するアイテム
 */
public class PushItem {
    /**
     * ID
     */
    private Long id;
    /**
     * アイコン画像のURI文字列
     */
    private String uriString;
    /**
     * お知らせ種別
     */
    private String type;
    /**
     * お知らせタイトル
     */
    private String title;
    /**
     * お知らせ内容
     */
    private String content;
    /**
     * お知らせ詳細内容
     */
    private String detail;
    /**
     * お知らせ受信時刻
     */
    private Date date;
    /**
     * 表示/消去ボタン表示可否
     */
    private boolean isButtonVisible;
    /**
     * お知らせ内容表示部分の横幅
     */
    private int cellPushWidth;

    /**
     * コンストラクタ
     * プッシュ一覧に表示するプッシュ通知アイテム
     *
     * @param id        ID
     * @param uriString アイコン画像のURI文字列
     * @param type      お知らせ種別
     * @param title     お知らせタイトル
     * @param content   お知らせ内容
     * @param detail    お知らせ詳細内容
     * @param date      お知らせ受信時刻
     */
    public PushItem(@Nullable Long id, @Nullable String uriString, @Nullable String type,
                    @Nullable String title, @Nullable String content, @Nullable String detail, @Nullable Date date) {
        this.id = id;
        this.uriString = uriString;
        this.type = type;
        this.title = title;
        this.content = content;
        this.detail = detail;
        this.date = date;
        isButtonVisible = false;
        cellPushWidth = 0;
    }

    /**
     * コンストラクタ
     * プッシュ一覧に表示する日付アイテム
     *
     * @param date お知らせ受信時刻
     */
    public PushItem(@NonNull Date date) {
        this(null, null, null, null, null, null, date);
    }

    @CheckForNull
    public Long getId() {
        return id;
    }

    @CheckForNull
    public String getUriString() {
        return uriString;
    }

    @CheckForNull
    public String getType() {
        return type;
    }

    @CheckForNull
    public String getTitle() {
        return title;
    }

    @CheckForNull
    public String getContent() {
        return content;
    }

    @CheckForNull
    public String getDetail() {
        return detail;
    }

    @CheckForNull
    public Date getDate() {
        return date;
    }

    public boolean isButtonVisible() {
        return isButtonVisible;
    }

    public void setButtonVisible(boolean buttonVisible) {
        isButtonVisible = buttonVisible;
    }

    public int getCellPushWidth() {
        return cellPushWidth;
    }

    public void setCellPushWidth(int cellPushWidth) {
        this.cellPushWidth = cellPushWidth;
    }
}
