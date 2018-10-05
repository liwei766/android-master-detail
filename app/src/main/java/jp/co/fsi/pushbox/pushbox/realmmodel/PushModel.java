package jp.co.fsi.pushbox.pushbox.realmmodel;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import jp.co.fsi.pushbox.pushbox.TimeUtil;
import jp.co.fsi.pushbox.pushbox.data.PushItem;

/**
 * プッシュ通知保存テーブル
 */
public class PushModel extends RealmObject {
    /**
     * ID
     */
    @PrimaryKey
    private Long id;
    /**
     * 画像データ
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
     * お知らせ取得時刻
     */
    @Required
    private Date date;
    /**
     * 論理削除フラグ
     */
    @Required
    private Boolean isDelete;

    /**
     * アクセスキー
     */
    private static final String ID = "id";
    private static final String URI_STRING = "uriString";
    private static final String TYPE = "type";
    private static final String TITLE = "title";
    private static final String CONTENT = "content";
    private static final String DETAIL = "detail";
    private static final String DATE = "date";
    private static final String IS_DELETE = "isDelete";

    /**
     * プッシュ通知の内容を保存する
     *
     * @param realm    Realmのインスタンス
     * @param pushItem 受信したプッシュ通知
     */
    public static void insert(@NonNull Realm realm, @NonNull PushItem pushItem) {

        Number maxId = realm.where(PushModel.class).max(ID);
        Long id;
        if (maxId == null) {
            id = 0L;
        } else {
            id = (Long) maxId + 1;
        }

        PushModel newModel = new PushModel();
        newModel.id = id;
        newModel.uriString = pushItem.getUriString();
        newModel.type = pushItem.getType();
        newModel.title = pushItem.getTitle();
        newModel.content = pushItem.getContent();
        newModel.detail = pushItem.getDetail();
        newModel.date = TimeUtil.getDate();
        newModel.isDelete = false;
        realm.copyToRealmOrUpdate(newModel);
    }

    /**
     * IDを指定して保存しているプッシュ通知を削除する
     *
     * @param realm Realmのインスタンス
     * @param id    削除対象ID
     */
    public static void delete(@NonNull Realm realm, @NonNull Long id) {
        // 引数のIDと一致するデータを検索する
        PushModel model = realm.where(PushModel.class).equalTo(ID, id).findFirst();
        if (model != null) {
            // データがあった場合、削除フラグを真にする
            model.isDelete = true;
            realm.copyToRealmOrUpdate(model);
        }
    }

    /**
     * プッシュ通知リストを取得する
     *
     * @param realm Realmのインスタンス
     * @return プッシュ通知リスト
     */
    public static List<PushItem> select(Context context, @NonNull Realm realm) {
        List<PushItem> items = new ArrayList<>();

        // 削除されていないプッシュ通知を全て取得する
        RealmResults<PushModel> results = realm.where(PushModel.class)
                .equalTo(IS_DELETE, false).findAll().sort(DATE, Sort.DESCENDING);

//        Calendar tempCalendar = TimeUtil.getCalendar();
//        for (PushModel model : results) {
//            if (model == null) break;
//            Date date = model.date;
//
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(date);
//            cal.set(Calendar.HOUR, 0);
//            cal.set(Calendar.MINUTE, 0);
//            cal.set(Calendar.SECOND, 0);
//            cal.set(Calendar.MILLISECOND, 0);
//            if (cal.compareTo(tempCalendar) != 0) {
//                items.add(new PushItem(date));
//                tempCalendar.setTime(cal.getTime());
//            }
//
//            items.add(new PushItem(model.id, model.uriString, model.type,
//                    model.title, model.content, model.detail, model.date));
//        }

        // CSVを読み込んで、一覧を生成
        AssetManager assetManager = context.getResources().getAssets();
        try {
            // CSVファイルの読み込み
            InputStream inputStream = assetManager.open("InitData.csv");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferReader = new BufferedReader(inputStreamReader);
            String line;
            long id = 0L;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.JAPAN);
            Calendar tempCalendar = TimeUtil.getCalendar();
            while ((line = bufferReader.readLine()) != null) {

                //カンマ区切りで１つづつ配列に入れる
                String[] RowData = line.split(",");

                //日付変換
                String dateString = RowData[5];
                Date convertedDate = TimeUtil.getDate();
                try {
                    convertedDate = dateFormat.parse(dateString);

                    Calendar cal = TimeUtil.getCalendar();
                    cal.setTime(convertedDate);
                    cal = TimeUtil.timeCut(cal);
                    if (cal.compareTo(tempCalendar) != 0) {
                        items.add(new PushItem(convertedDate));
                        tempCalendar.setTime(cal.getTime());
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //CSVの左([0]番目)から順番にセット
                items.add(new PushItem(id++, RowData[0], RowData[1], RowData[2], RowData[3], RowData[4], convertedDate));
            }
            bufferReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return items;
    }
}
