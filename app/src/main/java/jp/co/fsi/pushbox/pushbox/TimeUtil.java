package jp.co.fsi.pushbox.pushbox;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.annotation.Nonnull;

/**
 * Date、Calendar操作用Utilityクラス
 */
public class TimeUtil {

    /**
     * タイムゾーンが日本のカレンダーを取得する
     *
     * @return タイムゾーンが日本のカレンダー
     */
    @Nonnull
    public static Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance(Locale.JAPAN);
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
        return calendar;
    }

    /**
     * 時分秒を0にする
     *
     * @param calendar カレンダー
     * @return 時分秒を整えたカレンダー
     */
    @Nonnull
    public static Calendar timeCut(@Nonnull Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    /**
     * タイムゾーンが日本の現在日時を取得する
     *
     * @return 現在日時
     */
    public static Date getDate() {
        return getCalendar().getTime();
    }
}
