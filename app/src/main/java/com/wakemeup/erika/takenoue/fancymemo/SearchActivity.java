package com.wakemeup.erika.takenoue.fancymemo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class SearchActivity extends AppCompatActivity {
    private Realm mRealm;
    private ListView mListView;
    private MemoAdapter mMemoAdapter;
    String searchText = " ";

    public final static String EXTRA_MEMO = "com.wakemeup.erika.takenoue.fancymemo.MEMO";

    private RealmChangeListener mRealmListener = new RealmChangeListener() {
        @Override
        public void onChange(Object element) {
            reloadListView();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //ViewPagerと同じ画像をMainActivityから受け取り、設定
        Intent intent = getIntent();
        int Background = intent.getIntExtra("Design",-1);
        ImageView backgroundImage = findViewById(R.id.background);
        backgroundImage.setBackgroundResource(Background);

        setViews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean result = true;

        switch (id) {
            case android.R.id.home:
                finish();
                break;
            default:
                result = super.onOptionsItemSelected(item);
        }
        return result;
    }

   private void setViews() {
        final EditText editText = findViewById(R.id.edit_search);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
           @Override
           public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

               if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                   searchText = editText.getText().toString();// search処理
                   InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                   inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                   reloadListView();
               }
               return true; // falseを返すと, IMEがSearch→Doneへと切り替わる
           }
       });

        mRealm = Realm.getDefaultInstance();
        mRealm.addChangeListener(mRealmListener);
        mMemoAdapter = new MemoAdapter(SearchActivity.this);

        mListView = findViewById(R.id.list).findViewById(R.id.listView);
        //リストの項目をクリック
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 入力・編集する画面に遷移させる
                Memo memo = (Memo) parent.getAdapter().getItem(position);

                Intent intent = new Intent(SearchActivity.this, InputActivity.class);
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
                LayoutInflater inflater = LayoutInflater.from(SearchActivity.this);
                final View dialog_view = inflater.inflate(R.layout.delete_alert, null);

                // ダイアログを表示する
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this,R.style.MyAlertDialogStyle);
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

    private void reloadListView() {
        //EditTextに入力された文字列をcontainsで絞りこんで表示
        RealmResults<Memo> memoRealmResults = mRealm.where(Memo.class).contains("memo",searchText).sort("date", Sort.DESCENDING).findAll();
        mMemoAdapter.setMemoList(mRealm.copyFromRealm(memoRealmResults));
        mListView.setAdapter(mMemoAdapter);
        mMemoAdapter.notifyDataSetChanged();
    }
}