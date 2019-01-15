package com.wakemeup.erika.takenoue.fancymemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class InputActivity extends AppCompatActivity {
    private Memo mMemo;
    private EditText mMemoEdit;
    int fav;//"1"→登録、"0"→解除
    int page;
    MenuItem menuFav;
    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mMemoEdit = findViewById(R.id.memoText);

        Intent intent = getIntent();
        //ViewPagerのページ数の値を取得
        page = intent.getIntExtra("page_number", -1);
        int memoId = intent.getIntExtra(Fragment_1.EXTRA_MEMO, -1);
        mRealm = Realm.getDefaultInstance();
        mMemo = mRealm.where(Memo.class).equalTo("id", memoId).findFirst();
        mRealm.close();

        if (mMemo == null) {
            // 新規作成の場合
        } else {
            // 更新の場合
            mMemoEdit.setText(mMemo.getMemo());
            fav = mMemo.getFav();
        }
    }

    //Toolbarのメニュー
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_input, menu);

        menuFav = menu.findItem(R.id.action_favorite);
        if (fav == 1) {
            //お気に入りに登録されている場合
            menuFav.setIcon(R.drawable.favorited);
        } else {
            //登録されていない場合
            menuFav.setIcon(R.drawable.favorite);
        }
        return true;
    }

    //Toolbarのメニュークリック時
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean result = true;

        switch (id) {
            case android.R.id.home:
                LayoutInflater inflater = LayoutInflater.from(InputActivity.this);
                final View dialog_view = inflater.inflate(R.layout.back_alert,null);
                //編集が破棄されることの注意アラート
                new AlertDialog.Builder(InputActivity.this, R.style.MyAlertDialogStyle)
                        .setView(dialog_view)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        })
                        .setNegativeButton("Close", null)
                        .show();
                break;
            case R.id.action_share:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, mMemoEdit.getText().toString());
                startActivity(Intent.createChooser(intent, "Select App"));
                break;
            case R.id.action_done:
                addMemo(page);
                finish();
                break;
            case R.id.action_favorite:
                if (fav == 1) {
                    menuFav.setIcon(R.drawable.favorite);
                    fav = 0;
                } else {
                    menuFav.setIcon(R.drawable.favorited);
                    fav = 1;
                }
                break;
            default:
                break;
        }
        return result;
    }

    private void addMemo(int page) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        if (mMemo == null) {
            // 新規作成の場合
            mMemo = new Memo();

            RealmResults<Memo> memoRealmResults = realm.where(Memo.class).findAll();
            int identifier;

            if (memoRealmResults.max("id") != null) {
                identifier = memoRealmResults.max("id").intValue() + 1;
            } else {
                identifier = 0;
            }
            mMemo.setId(identifier);
        }

        String memo = mMemoEdit.getText().toString();

        mMemo.setFav(fav);
        mMemo.setPage(page);
        mMemo.setMemo(memo);
        mMemo.setDate(new Date());

        realm.copyToRealmOrUpdate(mMemo);
        realm.commitTransaction();

        realm.close();
    }
}