package com.wakemeup.erika.takenoue.fancymemo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class Fragment_5 extends Fragment {
    private final static String BACKGROUND_COLOR = "background_color";
    private ListView mListView;
    private MemoAdapter mMemoAdapter;
    public final static String EXTRA_MEMO = "com.wakemeup.erika.takenoue.fancymemo.MEMO";

    private Realm mRealm;
    private RealmChangeListener mRealmListener = new RealmChangeListener() {
        @Override
        public void onChange(Object element) {
            reloadListView();
        }
    };

    public static Fragment_5 newInstance(@ColorRes int IdRes) {
        Fragment_5 frag = new Fragment_5();
        Bundle b = new Bundle();
        b.putInt(BACKGROUND_COLOR, IdRes);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_main, null);
        mRealm = Realm.getDefaultInstance();
        mRealm.addChangeListener(mRealmListener);
        mMemoAdapter = new MemoAdapter(getActivity());

        mListView = view.findViewById(R.id.listView);
        //リストの項目をクリック
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 入力・編集する画面に遷移させる
                Memo memo = (Memo) parent.getAdapter().getItem(position);

                Intent intent = new Intent(getActivity(), InputActivity.class);
                intent.putExtra(EXTRA_MEMO, memo.getId());
                //Fabが押されたページごとに表示場所を変えるので、ViewPagerのページ数を取得。
                int pages = memo.getPage();
                intent.putExtra("page_number", pages);
                startActivity(intent);
            }
        });

        //リストの項目を長押し
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // 削除する
                final Memo memo = (Memo) parent.getAdapter().getItem(position);
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                final View dialog_view = inflater.inflate(R.layout.delete_alert, null);

                // ダイアログを表示する
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.MyAlertDialogStyle);
                builder.setView(dialog_view);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RealmResults<Memo> results = mRealm.where(Memo.class).equalTo("id", memo.getId()).findAll();
                        mRealm.beginTransaction();
                        results.deleteAllFromRealm();
                        mRealm.commitTransaction();

                        reloadListView();
                    }
                });
                builder.setNegativeButton("CANCEL", null);
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });
        reloadListView();
        return view;
    }

    int sort = 0;//並び順の分岐に使用

    private void reloadListView() {
        //デフォルト
        RealmResults<Memo> memoRealmResults2 = mRealm.where(Memo.class).equalTo("page",4).sort("date", Sort.DESCENDING).findAll();
        mMemoAdapter.setMemoList(mRealm.copyFromRealm(memoRealmResults2));
        mListView.setAdapter(mMemoAdapter);
        mMemoAdapter.notifyDataSetChanged();

        //並び順変更の情報を取得
        SharedPreferences preferences = this.getActivity().getSharedPreferences("hoge", Context.MODE_PRIVATE);
        sort = preferences.getInt("fra5", sort);
        if (sort == 1) {
            RealmResults<Memo> memoRealmResults = mRealm.where(Memo.class).equalTo("page",4).sort("date", Sort.DESCENDING).findAll();
            mMemoAdapter.setMemoList(mRealm.copyFromRealm(memoRealmResults));
            mListView.setAdapter(mMemoAdapter);
            mMemoAdapter.notifyDataSetChanged();
        } else if (sort == 2) {
            RealmResults<Memo> memoRealmResults = mRealm.where(Memo.class).equalTo("page",4).sort("date", Sort.ASCENDING).findAll();
            mMemoAdapter.setMemoList(mRealm.copyFromRealm(memoRealmResults));
            mListView.setAdapter(mMemoAdapter);
            mMemoAdapter.notifyDataSetChanged();
        } else if (sort == 3) {
            RealmResults<Memo> memoRealmResults = mRealm.where(Memo.class).equalTo("page",4).sort("memo", Sort.ASCENDING).findAll();
            mMemoAdapter.setMemoList(mRealm.copyFromRealm(memoRealmResults));
            mListView.setAdapter(mMemoAdapter);
            mMemoAdapter.notifyDataSetChanged();
        }
    }

    //登録日が新しい順に表示
    public void dateDeListView() {
        RealmResults<Memo> memoRealmResults = mRealm.where(Memo.class).equalTo("page",4).sort("date", Sort.DESCENDING).findAll();
        mMemoAdapter.setMemoList(mRealm.copyFromRealm(memoRealmResults));
        mListView.setAdapter(mMemoAdapter);
        mMemoAdapter.notifyDataSetChanged();
    }

    //登録日が古い順に表示
    public void dateAsListView() {
        RealmResults<Memo> memoRealmResults = mRealm.where(Memo.class).equalTo("page",4).sort("date", Sort.ASCENDING).findAll();
        mMemoAdapter.setMemoList(mRealm.copyFromRealm(memoRealmResults));
        mListView.setAdapter(mMemoAdapter);
        mMemoAdapter.notifyDataSetChanged();
    }

    //五十音順に表示
    public void memoListView() {
        RealmResults<Memo> memoRealmResults = mRealm.where(Memo.class).equalTo("page",4).sort("memo", Sort.ASCENDING).findAll();
        mMemoAdapter.setMemoList(mRealm.copyFromRealm(memoRealmResults));
        mListView.setAdapter(mMemoAdapter);
        mMemoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }
}