package com.wakemeup.erika.takenoue.fancymemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class FavoriteActivity extends AppCompatActivity {
    private Realm mRealm;
    private ListView mListView;
    private MemoAdapter mMemoAdapter;
    public final static String EXTRA_MEMO = "com.wakemeup.erika.takenoue.fancymemo.MEMO";

    private RealmChangeListener mRealmListener = new RealmChangeListener() {
        @Override
        public void onChange(Object element) {
            reloadListView();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //ViewPagerと同じ画像をMainActivityから受け取り、設定
        Intent intent = getIntent();
        int Background = intent.getIntExtra("Design",-1);
        CoordinatorLayout layout = findViewById(R.id.overview_coordinator_layout);
        layout.setBackgroundResource(Background);

        setViews();
    }

    //Toolbarメニュー
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean result = true;
        switch (id) {
            case android.R.id.home:
                Intent intent = new Intent(FavoriteActivity.this, MainActivity.class);
                intent.putExtra("lockOpen", 1);//ロック画面に移行しないようにロック解除の値を渡す。
                startActivity(intent);
                break;
            default:
                result = super.onOptionsItemSelected(item);
        }
        return result;
    }

    private void setViews() {
        mRealm = Realm.getDefaultInstance();
        mRealm.addChangeListener(mRealmListener);
        mMemoAdapter = new MemoAdapter(FavoriteActivity.this);

        mListView = findViewById(R.id.favoriteList).findViewById(R.id.listView);
        //リストの項目をクリック
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 入力・編集する画面に遷移させる
                Memo memo = (Memo) parent.getAdapter().getItem(position);

                Intent intent = new Intent(FavoriteActivity.this, InputActivity.class);
                intent.putExtra(EXTRA_MEMO, memo.getId());
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
                LayoutInflater inflater = LayoutInflater.from(FavoriteActivity.this);
                final View dialog_view = inflater.inflate(R.layout.delete_alert, null);

                // ダイアログを表示する
                AlertDialog.Builder builder = new AlertDialog.Builder(FavoriteActivity.this,R.style.MyAlertDialogStyle);
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
    }

    //お気に入りボタンが押されたものだけを表示
    private void reloadListView() {
        RealmResults<Memo> memoRealmResults = mRealm.where(Memo.class).equalTo("fav",1).sort("date", Sort.DESCENDING).findAll();
        mMemoAdapter.setMemoList(mRealm.copyFromRealm(memoRealmResults));
        mListView.setAdapter(mMemoAdapter);
        mMemoAdapter.notifyDataSetChanged();
    }
}