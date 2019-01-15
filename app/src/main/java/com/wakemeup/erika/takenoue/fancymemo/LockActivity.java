package com.wakemeup.erika.takenoue.fancymemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LockActivity extends AppCompatActivity {
    String pass;
    TextView textView;
    private Button mButton1, mButton2, mButton3, mButton4, mButton5, mButton6, mButton7, mButton8, mButton9, mButton0;
    private Button mDone, mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock_layout);

        mButton1 = findViewById(R.id.button1);
        mButton2 = findViewById(R.id.button2);
        mButton3 = findViewById(R.id.button3);
        mButton4 = findViewById(R.id.button4);
        mButton5 = findViewById(R.id.button5);
        mButton6 = findViewById(R.id.button6);
        mButton7 = findViewById(R.id.button7);
        mButton8 = findViewById(R.id.button8);
        mButton9 = findViewById(R.id.button9);
        mButton0 = findViewById(R.id.button0);

        mDone = findViewById(R.id.done);
        mBack = findViewById(R.id.back);

        //キーパッドのフォントを設定
        mButton1.setTypeface(Typeface.createFromAsset(getAssets(),
                "MarshMallowPopHeartTTFLight-Regular.ttf"));
        mButton2.setTypeface(Typeface.createFromAsset(getAssets(),
                "MarshMallowPopHeartTTFLight-Regular.ttf"));
        mButton3.setTypeface(Typeface.createFromAsset(getAssets(),
                "MarshMallowPopHeartTTFLight-Regular.ttf"));
        mButton4.setTypeface(Typeface.createFromAsset(getAssets(),
                "MarshMallowPopHeartTTFLight-Regular.ttf"));
        mButton5.setTypeface(Typeface.createFromAsset(getAssets(),
                "MarshMallowPopHeartTTFLight-Regular.ttf"));
        mButton6.setTypeface(Typeface.createFromAsset(getAssets(),
                "MarshMallowPopHeartTTFLight-Regular.ttf"));
        mButton7.setTypeface(Typeface.createFromAsset(getAssets(),
                "MarshMallowPopHeartTTFLight-Regular.ttf"));
        mButton8.setTypeface(Typeface.createFromAsset(getAssets(),
                "MarshMallowPopHeartTTFLight-Regular.ttf"));
        mButton9.setTypeface(Typeface.createFromAsset(getAssets(),
                "MarshMallowPopHeartTTFLight-Regular.ttf"));
        mButton0.setTypeface(Typeface.createFromAsset(getAssets(),
                "MarshMallowPopHeartTTFLight-Regular.ttf"));
        mDone.setTypeface(Typeface.createFromAsset(getAssets(),
                "MarshMallowPopHeartTTFLight-Regular.ttf"));
        mBack.setTypeface(Typeface.createFromAsset(getAssets(),
                "MarshMallowPopHeartTTFLight-Regular.ttf"));

        mButton1.setOnClickListener(NumberListener);
        mButton2.setOnClickListener(NumberListener);
        mButton3.setOnClickListener(NumberListener);
        mButton4.setOnClickListener(NumberListener);
        mButton5.setOnClickListener(NumberListener);
        mButton6.setOnClickListener(NumberListener);
        mButton7.setOnClickListener(NumberListener);
        mButton8.setOnClickListener(NumberListener);
        mButton9.setOnClickListener(NumberListener);
        mButton0.setOnClickListener(NumberListener);

        mDone.setOnClickListener(DoneListener);
        mBack.setOnClickListener(BackListener);

        Intent intent = getIntent();
        pass = intent.getStringExtra("password");//パスワードの文字列を取得
        textView = findViewById(R.id.passText);
    }

    //戻るボタン不能(MainActivityに移動してしまい(LockActivityが終了する)、ロックが解除されてしまうため)
    @Override
    public void onBackPressed() {
    }

    //OKボタン投下
    View.OnClickListener DoneListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String password = textView.getText().toString();

            if (pass.equals(password)) {
                //パスワードがあっている場合
                Intent intent = new Intent(LockActivity.this, MainActivity.class);
                intent.putExtra("lockOpen", 1);//「1」→ロック解除
                startActivity(intent);
            } else {
                LayoutInflater inflater = LayoutInflater.from(LockActivity.this);
                final View dialog_view = inflater.inflate(R.layout.lock_different, null);
                //ロックが間違っている旨のアラート表示
                new AlertDialog.Builder(LockActivity.this, R.style.MyAlertDialogStyle)
                        .setView(dialog_view)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //パスワード表示部分をクリア
                                textView.getEditableText().clear();
                            }
                        })
                        .show();
            }
        }
    };

    View.OnClickListener NumberListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button button = (Button) view;
            //数字を後ろに追加していく
            textView.append(button.getText());
        }
    };

    //「←」投下
    View.OnClickListener BackListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String pass = textView.getText().toString();
            //文字が入力されている場合
            if(pass != null && pass.length() > 0){
                pass = pass.substring(0, pass.length()-1); //文字列を一つ削除
                textView.setText(pass);
            }
        }
    };
}