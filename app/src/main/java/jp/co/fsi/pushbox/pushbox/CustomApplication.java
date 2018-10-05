package jp.co.fsi.pushbox.pushbox;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmFileException;

/**
 * Realm使用可能カスタムアプリケーション
 */
public class CustomApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
    }

    /**
     * Realmのインスタンスを取得する
     *
     * @return Realmインスタンス
     */
    public static Realm getRealmInstance() {
        Realm realm = null;
        try {
            RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                    .deleteRealmIfMigrationNeeded()
                    .build();
            realm = Realm.getInstance(realmConfiguration);
        } catch (IllegalStateException | RealmFileException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return realm;
    }
}
