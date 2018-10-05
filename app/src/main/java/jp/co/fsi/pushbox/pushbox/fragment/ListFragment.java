package jp.co.fsi.pushbox.pushbox.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.exceptions.RealmException;
import jp.co.fsi.pushbox.pushbox.CustomApplication;
import jp.co.fsi.pushbox.pushbox.R;
import jp.co.fsi.pushbox.pushbox.data.PushItem;
import jp.co.fsi.pushbox.pushbox.realmmodel.PushModel;
import jp.co.fsi.pushbox.pushbox.recyclerview.RecyclerViewAdapter;

/**
 * プッシュ一覧画面
 */
public class ListFragment extends BaseFragment {

    /**
     * スプラッシュ画面のインスタンスを取得する
     *
     * @return スプラッシュ画面
     */
    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.list_fragment, container, false);
        final RecyclerView recyclerView = layout.findViewById(R.id.recycler);

        // お知らせリストの取得
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final List<PushItem> items = new ArrayList<>();
                try (Realm realm = CustomApplication.getRealmInstance()) {
                    items.addAll(PushModel.select(getContext(),realm));
                } catch (RealmException e) {
                    e.printStackTrace();
                }
                //  お知らせリストをRecyclerViewに登録する
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        final RecyclerViewAdapter adapter = new RecyclerViewAdapter(listener, items);
                        adapter.setMyRecyclerView(recyclerView);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return layout;
    }
}
