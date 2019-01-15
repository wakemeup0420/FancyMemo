package com.wakemeup.erika.takenoue.fancymemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;
    private FloatingActionButton floatingActionButton;
    private FragmentPagerAdapter adapter;
    private EditText mInputTitleEdit1, mInputTitleEdit2, mInputTitleEdit3, mInputTitleEdit4, mInputTitleEdit5;
    private ImageView mNoDesign, mDesign1, mDesign2, mDesign3, mDesign4, mDesign5, mDesign6, mDesign7, mDesign8, mDesign9, mDesign10, mDesign11, mDesign12, mDesign13, mDesign14, mDesign15, mDesign16, mDesign17, mDesign18, mDesign19;
    private TextView mNavItem, mNavItem2, mNavItem3, mNavItem4, mNavItem5, mNavItem6;
    private Fragment_1 Fragment1;
    private Fragment_2 Fragment2;
    private Fragment_3 Fragment3;
    private Fragment_4 Fragment4;
    private Fragment_5 Fragment5;

    String CategoryTitle1 = "TODO";
    String CategoryTitle2 = "TASK";
    String CategoryTitle3 = "SHOPPING";
    String CategoryTitle4 = "SCHEDULE";
    String CategoryTitle5 = "IDEA";

    int lockOpen;//ロックを解除するか否か。"1"→ロック解除

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(FabListener);

        toolbar = findViewById(R.id.toolbar);
        setViews();

        //前回設定している画像の値を保持したものを受け取り、セット。
        SharedPreferences sp = getSharedPreferences("hoge", MODE_PRIVATE);
        BackgroundResource = sp.getInt("Design", BackgroundResource);
        viewPager.setBackgroundResource(BackgroundResource);

        //Toolbarのタイトルのフォントを変更
        TextView textView = findViewById(R.id.toolbar_title);
        textView.setTypeface(Typeface.createFromAsset(getAssets(),
                "MarshMallowPopHeartTTFLight-Regular.ttf"));

        //パスワード設定のチェックボックスのチェックの有無
        SharedPreferences sp1 = getSharedPreferences("hoge", MODE_PRIVATE);
        check = sp1.getInt("check", check);

        //保持したパスワードの値を取得
        SharedPreferences sp2 = getSharedPreferences("hoge", MODE_PRIVATE);
        pass = sp2.getString("pass", pass);

        //LockActivityからロックを解除するか否かの値を受け取る。
        Intent intent = getIntent();
        lockOpen = intent.getIntExtra("lockOpen", -1);

        //ロックが解除されていない
        if (lockOpen != 1 && check == 1) {
            intent = new Intent(MainActivity.this, LockActivity.class);
            intent.putExtra("password", pass);//設定しているパスワードを送る
            startActivity(intent);
        }
    }

    //戻るボタン不能
    @Override
    public void onBackPressed() {
    }

    private void setViews() {
        setSupportActionBar(toolbar);
        viewPager = findViewById(R.id.main_viewpager);

        //前回入力したカテゴリーのタイトルを保持していたものを受け取る。
        SharedPreferences sp = getSharedPreferences("hoge", MODE_PRIVATE);
        CategoryTitle1 = sp.getString("Title1", CategoryTitle1);
        SharedPreferences sp2 = getSharedPreferences("hoge", MODE_PRIVATE);
        CategoryTitle2 = sp2.getString("Title2", CategoryTitle2);
        SharedPreferences sp3 = getSharedPreferences("hoge", MODE_PRIVATE);
        CategoryTitle3 = sp3.getString("Title3", CategoryTitle3);
        SharedPreferences sp4 = getSharedPreferences("hoge", MODE_PRIVATE);
        CategoryTitle4 = sp4.getString("Title4", CategoryTitle4);
        SharedPreferences sp5 = getSharedPreferences("hoge", MODE_PRIVATE);
        CategoryTitle5 = sp5.getString("Title5", CategoryTitle5);

        //ViewPagerの設定
        adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        if (Fragment1 == null) {
                            Fragment1 = Fragment_1.newInstance(android.R.color.transparent);
                        }
                        return Fragment1;
                    case 1:
                        if (Fragment2 == null) {
                            Fragment2 = Fragment_2.newInstance(android.R.color.transparent);
                        }
                        return Fragment2;
                    case 2:
                        if (Fragment3 == null) {
                            Fragment3 = Fragment_3.newInstance(android.R.color.transparent);
                        }
                        return Fragment3;
                    case 3:
                        if (Fragment4 == null) {
                            Fragment4 = Fragment_4.newInstance(android.R.color.transparent);
                        }
                        return Fragment4;
                    case 4:
                        if (Fragment5 == null) {
                            Fragment5 = Fragment_5.newInstance(android.R.color.transparent);
                        }
                        return Fragment5;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 5;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return CategoryTitle1;
                    case 1:
                        return CategoryTitle2;
                    case 2:
                        return CategoryTitle3;
                    case 3:
                        return CategoryTitle4;
                    case 4:
                        return CategoryTitle5;
                }
                return null;
            }
        };
        viewPager.setAdapter(adapter);

        setDrawer();
        TabLayout tabLayout = findViewById(R.id.main_tab);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setDrawer() {
        drawerLayout = findViewById(R.id.main_drawer);

        //ナビゲーションドロワーのメニュー項目
        mNavItem = findViewById(R.id.item_title);
        mNavItem.setOnClickListener(CategoryListener);
        mNavItem2 = findViewById(R.id.item_title2);
        mNavItem2.setOnClickListener(DesignsListener);
        mNavItem3 = findViewById(R.id.item_title3);
        mNavItem3.setOnClickListener(FavoriteListener);
        mNavItem4 = findViewById(R.id.item_title4);
        mNavItem4.setOnClickListener(LockListener);
        mNavItem5 = findViewById(R.id.item_title5);
        mNavItem5.setOnClickListener(ContactListener);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        toggle.syncState();

        //他の作成アプリのリンク
        RelativeLayout layout = findViewById(R.id.app);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.wakemeup.erika.takenoue.fancycalculator");
                Intent i = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(i);
                }
        });
    }

    //カテゴリー項目
    View.OnClickListener CategoryListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CategoryTitle();
            drawerLayout.closeDrawers();
        }
    };

    int check;//パスワードの有無
    String pass;//パスワード
    String pass2;//パスワード確認用
    int clicked;

    //お気に入り項目
    View.OnClickListener FavoriteListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
            intent.putExtra("Design", BackgroundResource);//背景のデザインを送る
            startActivity(intent);
        }
    };

    //セキュリティー項目
    View.OnClickListener LockListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
            final View dialog_view = inflater.inflate(R.layout.lock_alert, null);
            final CheckBox checkBox = dialog_view.findViewById(R.id.checkbox);
            final EditText editText = dialog_view.findViewById(R.id.edit);
            final EditText editText2 = dialog_view.findViewById(R.id.edit2);

            //パスワード設定の有無
            SharedPreferences sp0 = getSharedPreferences("hoge", MODE_PRIVATE);
            clicked = sp0.getInt("check", check);

            if (clicked == 1) {
                checkBox.setChecked(true);
                check = 1;
                editText.setVisibility(View.VISIBLE);
                editText2.setVisibility(View.VISIBLE);
            } else {
                check = 0;
                checkBox.setChecked(false);
                editText.setVisibility(View.GONE);
                editText2.setVisibility(View.GONE);
            }

            //チェックボックス
            checkBox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (checkBox.isChecked() == true) {
                        check = 1;
                        editText.setVisibility(View.VISIBLE);
                        editText2.setVisibility(View.VISIBLE);
                    } else {
                        check = 0;
                        editText.setVisibility(View.GONE);
                        editText2.setVisibility(View.GONE);
                    }
                }
            });

            final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this, R.style.MyAlertDialogStyle);
            alert.setView(dialog_view);
            alert.setPositiveButton(android.R.string.ok, null);

            // ここでリスナーを実装しても渡されないので実装しなくても構いません。
            alert.setNegativeButton("CLOSE", null);

            final AlertDialog dialog = alert.show();

            Button button = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            Button button2 = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            // dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            // dialog.getButton(DialogInterface.BUTTON_NEUTRAL);

            // 通常のViewのように実装します。
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 場合によっては自分で明示的に閉じる必要がある
                    dialog.dismiss();
                }
            });

            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //パスワードを設定する(チェック有)場合
                    if (check == 1) {
                        pass = editText.getText().toString();
                        pass2 = editText2.getText().toString();
                        //パスワードが確認用と一致している場合
                        if (pass.length() >= 1 && pass.equals(pass2)) {
                            SharedPreferences sp = getSharedPreferences("hoge", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putInt("check", check);
                            editor.commit();
                            SharedPreferences sp1 = getSharedPreferences("hoge", MODE_PRIVATE);
                            SharedPreferences.Editor editor1 = sp1.edit();
                            editor1.putString("pass", pass);
                            editor1.commit();
                            dialog.dismiss();
                        } else if (pass.isEmpty()) {//パスワードが入力されていない場合
                            final LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                            final View layout = inflater.inflate(R.layout.toast_layout,
                                    (ViewGroup) findViewById(R.id.toast_layout_root));
                            final TextView text = layout.findViewById(R.id.text);
                            text.setText("パスワードが入力されていません");
                            final Toast toast = new Toast(getApplicationContext());
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.setView(layout);
                            toast.setGravity(Gravity.TOP, 0, 200);
                            toast.show();
                        } else {//確認用と一致しない場合
                            final LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                            final View layout = inflater.inflate(R.layout.toast_layout,
                                    (ViewGroup) findViewById(R.id.toast_layout_root));
                            final TextView text = layout.findViewById(R.id.text);
                            text.setText("確認用と一致しません");
                            final Toast toast = new Toast(getApplicationContext());
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.setView(layout);
                            toast.setGravity(Gravity.TOP, 0, 200);
                            toast.show();
                        }
                    } else {
                        //パスワードを設定しない(チェック無)場合
                        SharedPreferences sp = getSharedPreferences("hoge", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("check", check);
                        editor.commit();
                        dialog.dismiss();
                    }
                    // 場合によっては自分で明示的に閉じる必要がある
                }
            });
            drawerLayout.closeDrawers();
        }
    };

    //お問い合わせ項目
    View.OnClickListener ContactListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:xxx.karte@gmail.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "xxAktExx:ご意見・お問い合わせ");
            intent.putExtra(Intent.EXTRA_TEXT, "");
            startActivity(Intent.createChooser(intent, "MAiL SeLeCT"));

            drawerLayout.closeDrawers();
        }
    };

    //デザイン項目
    View.OnClickListener DesignsListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
            final View dialog_view = inflater.inflate(R.layout.design_main, null);

            mNoDesign = dialog_view.findViewById(R.id.no_design);
            mDesign1 = dialog_view.findViewById(R.id.design1);
            mDesign2 = dialog_view.findViewById(R.id.design2);
            mDesign3 = dialog_view.findViewById(R.id.design3);
            mDesign4 = dialog_view.findViewById(R.id.design4);
            mDesign5 = dialog_view.findViewById(R.id.design5);
            mDesign6 = dialog_view.findViewById(R.id.design6);
            mDesign7 = dialog_view.findViewById(R.id.design7);
            mDesign8 = dialog_view.findViewById(R.id.design8);
            mDesign9 = dialog_view.findViewById(R.id.design9);
            mDesign10 = dialog_view.findViewById(R.id.design10);
            mDesign11 = dialog_view.findViewById(R.id.design11);
            mDesign12 = dialog_view.findViewById(R.id.design12);
            mDesign13 = dialog_view.findViewById(R.id.design13);
            mDesign14 = dialog_view.findViewById(R.id.design14);
            mDesign15 = dialog_view.findViewById(R.id.design15);
            mDesign16 = dialog_view.findViewById(R.id.design16);
            mDesign17 = dialog_view.findViewById(R.id.design17);
            mDesign18 = dialog_view.findViewById(R.id.design18);
            mDesign19 = dialog_view.findViewById(R.id.design19);

            SharedPreferences sp = getSharedPreferences("hoge", MODE_PRIVATE);
            ClickNoDesign = sp.getInt("ClickNoDesign", ClickNoDesign);
            SharedPreferences sp1 = getSharedPreferences("hoge", MODE_PRIVATE);
            ClickDesign1 = sp1.getInt("ClickDesign1", ClickDesign1);
            SharedPreferences sp2 = getSharedPreferences("hoge", MODE_PRIVATE);
            ClickDesign2 = sp2.getInt("ClickDesign2", ClickDesign2);
            SharedPreferences sp3 = getSharedPreferences("hoge", MODE_PRIVATE);
            ClickDesign3 = sp3.getInt("ClickDesign3", ClickDesign3);
            SharedPreferences sp4 = getSharedPreferences("hoge", MODE_PRIVATE);
            ClickDesign4 = sp4.getInt("ClickDesign4", ClickDesign4);
            SharedPreferences sp5 = getSharedPreferences("hoge", MODE_PRIVATE);
            ClickDesign5 = sp5.getInt("ClickDesign5", ClickDesign5);
            SharedPreferences sp6 = getSharedPreferences("hoge", MODE_PRIVATE);
            ClickDesign6 = sp6.getInt("ClickDesign6", ClickDesign6);
            SharedPreferences sp7 = getSharedPreferences("hoge", MODE_PRIVATE);
            ClickDesign7 = sp7.getInt("ClickDesign7", ClickDesign7);
            SharedPreferences sp8 = getSharedPreferences("hoge", MODE_PRIVATE);
            ClickDesign8 = sp8.getInt("ClickDesign8", ClickDesign8);
            SharedPreferences sp9 = getSharedPreferences("hoge", MODE_PRIVATE);
            ClickDesign9 = sp9.getInt("ClickDesign9", ClickDesign9);
            SharedPreferences sp10 = getSharedPreferences("hoge", MODE_PRIVATE);
            ClickDesign10 = sp10.getInt("ClickDesign10", ClickDesign10);
            SharedPreferences sp11 = getSharedPreferences("hoge", MODE_PRIVATE);
            ClickDesign11 = sp11.getInt("ClickDesign11", ClickDesign11);
            SharedPreferences sp12 = getSharedPreferences("hoge", MODE_PRIVATE);
            ClickDesign12 = sp12.getInt("ClickDesign12", ClickDesign12);
            SharedPreferences sp13 = getSharedPreferences("hoge", MODE_PRIVATE);
            ClickDesign13 = sp13.getInt("ClickDesign13", ClickDesign13);
            SharedPreferences sp14 = getSharedPreferences("hoge", MODE_PRIVATE);
            ClickDesign14 = sp14.getInt("ClickDesign14", ClickDesign14);
            SharedPreferences sp15 = getSharedPreferences("hoge", MODE_PRIVATE);
            ClickDesign15 = sp15.getInt("ClickDesign15", ClickDesign15);
            SharedPreferences sp16 = getSharedPreferences("hoge", MODE_PRIVATE);
            ClickDesign16 = sp16.getInt("ClickDesign16", ClickDesign16);
            SharedPreferences sp17 = getSharedPreferences("hoge", MODE_PRIVATE);
            ClickDesign17 = sp17.getInt("ClickDesign17", ClickDesign17);
            SharedPreferences sp18 = getSharedPreferences("hoge", MODE_PRIVATE);
            ClickDesign18 = sp18.getInt("ClickDesign18", ClickDesign18);
            SharedPreferences sp19 = getSharedPreferences("hoge", MODE_PRIVATE);
            ClickDesign19 = sp19.getInt("ClickDesign19", ClickDesign19);

            mNoDesign.setImageResource(ClickNoDesign);
            mDesign1.setImageResource(ClickDesign1);
            mDesign2.setImageResource(ClickDesign2);
            mDesign3.setImageResource(ClickDesign3);
            mDesign4.setImageResource(ClickDesign4);
            mDesign5.setImageResource(ClickDesign5);
            mDesign6.setImageResource(ClickDesign6);
            mDesign7.setImageResource(ClickDesign7);
            mDesign8.setImageResource(ClickDesign8);
            mDesign9.setImageResource(ClickDesign9);
            mDesign10.setImageResource(ClickDesign10);
            mDesign11.setImageResource(ClickDesign11);
            mDesign12.setImageResource(ClickDesign12);
            mDesign13.setImageResource(ClickDesign13);
            mDesign14.setImageResource(ClickDesign14);
            mDesign15.setImageResource(ClickDesign15);
            mDesign16.setImageResource(ClickDesign16);
            mDesign17.setImageResource(ClickDesign17);
            mDesign18.setImageResource(ClickDesign18);
            mDesign19.setImageResource(ClickDesign19);

            mNoDesign.setOnClickListener(DesignListener);
            mDesign1.setOnClickListener(DesignListener);
            mDesign2.setOnClickListener(DesignListener);
            mDesign3.setOnClickListener(DesignListener);
            mDesign4.setOnClickListener(DesignListener);
            mDesign5.setOnClickListener(DesignListener);
            mDesign6.setOnClickListener(DesignListener);
            mDesign7.setOnClickListener(DesignListener);
            mDesign8.setOnClickListener(DesignListener);
            mDesign9.setOnClickListener(DesignListener);
            mDesign10.setOnClickListener(DesignListener);
            mDesign11.setOnClickListener(DesignListener);
            mDesign12.setOnClickListener(DesignListener);
            mDesign13.setOnClickListener(DesignListener);
            mDesign14.setOnClickListener(DesignListener);
            mDesign15.setOnClickListener(DesignListener);
            mDesign16.setOnClickListener(DesignListener);
            mDesign17.setOnClickListener(DesignListener);
            mDesign18.setOnClickListener(DesignListener);
            mDesign19.setOnClickListener(DesignListener);

            new AlertDialog.Builder(MainActivity.this, R.style.MyAlertDialogStyle)
                    .setView(dialog_view)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SharedPreferences sp = getSharedPreferences("hoge", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putInt("Design", BackgroundResource);
                            editor.commit();
                            viewPager.setBackgroundResource(BackgroundResource);
                            DesignShared(ClickNoDesign, ClickDesign1, ClickDesign2, ClickDesign3, ClickDesign4, ClickDesign5, ClickDesign6, ClickDesign7, ClickDesign8, ClickDesign9, ClickDesign10, ClickDesign11, ClickDesign12, ClickDesign13, ClickDesign14, ClickDesign15, ClickDesign16, ClickDesign17, ClickDesign18, ClickDesign19);
                        }
                    })
                    .setNegativeButton("Close", null)
                    .show();
            drawerLayout.closeDrawers();
        }
    };

    int ClickNoDesign = R.drawable.no_design;
    int ClickDesign1 = R.drawable.design1;
    int ClickDesign2 = R.drawable.design2_clicked;
    int ClickDesign3 = R.drawable.design3;
    int ClickDesign4 = R.drawable.design4;
    int ClickDesign5 = R.drawable.design5;
    int ClickDesign6 = R.drawable.design6;
    int ClickDesign7 = R.drawable.design7;
    int ClickDesign8 = R.drawable.design8;
    int ClickDesign9 = R.drawable.design9;
    int ClickDesign10 = R.drawable.design10;
    int ClickDesign11 = R.drawable.design11;
    int ClickDesign12 = R.drawable.design12;
    int ClickDesign13 = R.drawable.design13;
    int ClickDesign14 = R.drawable.design14;
    int ClickDesign15 = R.drawable.design15;
    int ClickDesign16 = R.drawable.design16;
    int ClickDesign17 = R.drawable.design17;
    int ClickDesign18 = R.drawable.design18;
    int ClickDesign19 = R.drawable.design19;

    int ClickSelectDesign = 0; //押されたデザインボタン
    int BackgroundResource = R.drawable.star; //画面に表示するデザイン

    //デザイン選択画面処理
    View.OnClickListener DesignListener = new View.OnClickListener() {
        public void onClick(View view) {
            ImageView imageView = (ImageView) view;
            //選択したデザインのIDを取得
            ClickSelectDesign = imageView.getId();

            if (ClickSelectDesign == R.id.no_design) {
                BackgroundResource = 0;
                /*
                選択された項目がわかるように枠線のある画像と無い画像を用意しているので、
                選択されたものが枠線の画像になるように値をセット。
                 */
                ClickNoDesign = R.drawable.no_design_clicked;
                ClickDesign1 = R.drawable.design1;
                ClickDesign2 = R.drawable.design2;
                ClickDesign3 = R.drawable.design3;
                ClickDesign4 = R.drawable.design4;
                ClickDesign5 = R.drawable.design5;
                ClickDesign6 = R.drawable.design6;
                ClickDesign7 = R.drawable.design7;
                ClickDesign8 = R.drawable.design8;
                ClickDesign9 = R.drawable.design9;
                ClickDesign10 = R.drawable.design10;
                ClickDesign11 = R.drawable.design11;
                ClickDesign12 = R.drawable.design12;
                ClickDesign13 = R.drawable.design13;
                ClickDesign14 = R.drawable.design14;
                ClickDesign15 = R.drawable.design15;
                ClickDesign16 = R.drawable.design16;
                ClickDesign17 = R.drawable.design17;
                ClickDesign18 = R.drawable.design18;
                ClickDesign19 = R.drawable.design19;
                //実際に画像を表示するために値を渡す。
                ClickDesign(ClickNoDesign, ClickDesign1, ClickDesign2, ClickDesign3, ClickDesign4, ClickDesign5, ClickDesign6, ClickDesign7, ClickDesign8, ClickDesign9, ClickDesign10, ClickDesign11, ClickDesign12, ClickDesign13, ClickDesign14, ClickDesign15, ClickDesign16, ClickDesign17, ClickDesign18, ClickDesign19);
            } else if (ClickSelectDesign == R.id.design1) {
                BackgroundResource = R.drawable.cosmo;
                ClickNoDesign = R.drawable.no_design;
                ClickDesign1 = R.drawable.design1_clicked;
                ClickDesign2 = R.drawable.design2;
                ClickDesign3 = R.drawable.design3;
                ClickDesign4 = R.drawable.design4;
                ClickDesign5 = R.drawable.design5;
                ClickDesign6 = R.drawable.design6;
                ClickDesign7 = R.drawable.design7;
                ClickDesign8 = R.drawable.design8;
                ClickDesign9 = R.drawable.design9;
                ClickDesign10 = R.drawable.design10;
                ClickDesign11 = R.drawable.design11;
                ClickDesign12 = R.drawable.design12;
                ClickDesign13 = R.drawable.design13;
                ClickDesign14 = R.drawable.design14;
                ClickDesign15 = R.drawable.design15;
                ClickDesign16 = R.drawable.design16;
                ClickDesign17 = R.drawable.design17;
                ClickDesign18 = R.drawable.design18;
                ClickDesign19 = R.drawable.design19;
                ClickDesign(ClickNoDesign, ClickDesign1, ClickDesign2, ClickDesign3, ClickDesign4, ClickDesign5, ClickDesign6, ClickDesign7, ClickDesign8, ClickDesign9, ClickDesign10, ClickDesign11, ClickDesign12, ClickDesign13, ClickDesign14, ClickDesign15, ClickDesign16, ClickDesign17, ClickDesign18, ClickDesign19);
            } else if (ClickSelectDesign == R.id.design2) {
                BackgroundResource = R.drawable.star;
                ClickNoDesign = R.drawable.no_design;
                ClickDesign1 = R.drawable.design1;
                ClickDesign2 = R.drawable.design2_clicked;
                ClickDesign3 = R.drawable.design3;
                ClickDesign4 = R.drawable.design4;
                ClickDesign5 = R.drawable.design5;
                ClickDesign6 = R.drawable.design6;
                ClickDesign7 = R.drawable.design7;
                ClickDesign8 = R.drawable.design8;
                ClickDesign9 = R.drawable.design9;
                ClickDesign10 = R.drawable.design10;
                ClickDesign11 = R.drawable.design11;
                ClickDesign12 = R.drawable.design12;
                ClickDesign13 = R.drawable.design13;
                ClickDesign14 = R.drawable.design14;
                ClickDesign15 = R.drawable.design15;
                ClickDesign16 = R.drawable.design16;
                ClickDesign17 = R.drawable.design17;
                ClickDesign18 = R.drawable.design18;
                ClickDesign19 = R.drawable.design19;
                ClickDesign(ClickNoDesign, ClickDesign1, ClickDesign2, ClickDesign3, ClickDesign4, ClickDesign5, ClickDesign6, ClickDesign7, ClickDesign8, ClickDesign9, ClickDesign10, ClickDesign11, ClickDesign12, ClickDesign13, ClickDesign14, ClickDesign15, ClickDesign16, ClickDesign17, ClickDesign18, ClickDesign19);
            } else if (ClickSelectDesign == R.id.design3) {
                BackgroundResource = R.drawable.cross;
                ClickNoDesign = R.drawable.no_design;
                ClickDesign1 = R.drawable.design1;
                ClickDesign2 = R.drawable.design2;
                ClickDesign3 = R.drawable.design3_clicked;
                ClickDesign4 = R.drawable.design4;
                ClickDesign5 = R.drawable.design5;
                ClickDesign6 = R.drawable.design6;
                ClickDesign7 = R.drawable.design7;
                ClickDesign8 = R.drawable.design8;
                ClickDesign9 = R.drawable.design9;
                ClickDesign10 = R.drawable.design10;
                ClickDesign11 = R.drawable.design11;
                ClickDesign12 = R.drawable.design12;
                ClickDesign13 = R.drawable.design13;
                ClickDesign14 = R.drawable.design14;
                ClickDesign15 = R.drawable.design15;
                ClickDesign16 = R.drawable.design16;
                ClickDesign17 = R.drawable.design17;
                ClickDesign18 = R.drawable.design18;
                ClickDesign19 = R.drawable.design19;
                ClickDesign(ClickNoDesign, ClickDesign1, ClickDesign2, ClickDesign3, ClickDesign4, ClickDesign5, ClickDesign6, ClickDesign7, ClickDesign8, ClickDesign9, ClickDesign10, ClickDesign11, ClickDesign12, ClickDesign13, ClickDesign14, ClickDesign15, ClickDesign16, ClickDesign17, ClickDesign18, ClickDesign19);
            } else if (ClickSelectDesign == R.id.design4) {
                BackgroundResource = R.drawable.heart;
                ClickNoDesign = R.drawable.no_design;
                ClickDesign1 = R.drawable.design1;
                ClickDesign2 = R.drawable.design2;
                ClickDesign3 = R.drawable.design3;
                ClickDesign4 = R.drawable.design4_clicked;
                ClickDesign5 = R.drawable.design5;
                ClickDesign6 = R.drawable.design6;
                ClickDesign7 = R.drawable.design7;
                ClickDesign8 = R.drawable.design8;
                ClickDesign9 = R.drawable.design9;
                ClickDesign10 = R.drawable.design10;
                ClickDesign11 = R.drawable.design11;
                ClickDesign12 = R.drawable.design12;
                ClickDesign13 = R.drawable.design13;
                ClickDesign14 = R.drawable.design14;
                ClickDesign15 = R.drawable.design15;
                ClickDesign16 = R.drawable.design16;
                ClickDesign17 = R.drawable.design17;
                ClickDesign18 = R.drawable.design18;
                ClickDesign19 = R.drawable.design19;
                ClickDesign(ClickNoDesign, ClickDesign1, ClickDesign2, ClickDesign3, ClickDesign4, ClickDesign5, ClickDesign6, ClickDesign7, ClickDesign8, ClickDesign9, ClickDesign10, ClickDesign11, ClickDesign12, ClickDesign13, ClickDesign14, ClickDesign15, ClickDesign16, ClickDesign17, ClickDesign18, ClickDesign19);
            } else if (ClickSelectDesign == R.id.design5) {
                BackgroundResource = R.drawable.curtain;
                ClickNoDesign = R.drawable.no_design;
                ClickDesign1 = R.drawable.design1;
                ClickDesign2 = R.drawable.design2;
                ClickDesign3 = R.drawable.design3;
                ClickDesign4 = R.drawable.design4;
                ClickDesign5 = R.drawable.design5_clicked;
                ClickDesign6 = R.drawable.design6;
                ClickDesign7 = R.drawable.design7;
                ClickDesign8 = R.drawable.design8;
                ClickDesign9 = R.drawable.design9;
                ClickDesign10 = R.drawable.design10;
                ClickDesign11 = R.drawable.design11;
                ClickDesign12 = R.drawable.design12;
                ClickDesign13 = R.drawable.design13;
                ClickDesign14 = R.drawable.design14;
                ClickDesign15 = R.drawable.design15;
                ClickDesign16 = R.drawable.design16;
                ClickDesign17 = R.drawable.design17;
                ClickDesign18 = R.drawable.design18;
                ClickDesign19 = R.drawable.design19;
                ClickDesign(ClickNoDesign, ClickDesign1, ClickDesign2, ClickDesign3, ClickDesign4, ClickDesign5, ClickDesign6, ClickDesign7, ClickDesign8, ClickDesign9, ClickDesign10, ClickDesign11, ClickDesign12, ClickDesign13, ClickDesign14, ClickDesign15, ClickDesign16, ClickDesign17, ClickDesign18, ClickDesign19);
            } else if (ClickSelectDesign == R.id.design6) {
                BackgroundResource = R.drawable.illumination;
                ClickNoDesign = R.drawable.no_design;
                ClickDesign1 = R.drawable.design1;
                ClickDesign2 = R.drawable.design2;
                ClickDesign3 = R.drawable.design3;
                ClickDesign4 = R.drawable.design4;
                ClickDesign5 = R.drawable.design5;
                ClickDesign6 = R.drawable.design6_clicked;
                ClickDesign7 = R.drawable.design7;
                ClickDesign8 = R.drawable.design8;
                ClickDesign9 = R.drawable.design9;
                ClickDesign10 = R.drawable.design10;
                ClickDesign11 = R.drawable.design11;
                ClickDesign12 = R.drawable.design12;
                ClickDesign13 = R.drawable.design13;
                ClickDesign14 = R.drawable.design14;
                ClickDesign15 = R.drawable.design15;
                ClickDesign16 = R.drawable.design16;
                ClickDesign17 = R.drawable.design17;
                ClickDesign18 = R.drawable.design18;
                ClickDesign19 = R.drawable.design19;
                ClickDesign(ClickNoDesign, ClickDesign1, ClickDesign2, ClickDesign3, ClickDesign4, ClickDesign5, ClickDesign6, ClickDesign7, ClickDesign8, ClickDesign9, ClickDesign10, ClickDesign11, ClickDesign12, ClickDesign13, ClickDesign14, ClickDesign15, ClickDesign16, ClickDesign17, ClickDesign18, ClickDesign19);
            } else if (ClickSelectDesign == R.id.design7) {
                BackgroundResource = R.drawable.constellation;
                ClickNoDesign = R.drawable.no_design;
                ClickDesign1 = R.drawable.design1;
                ClickDesign2 = R.drawable.design2;
                ClickDesign3 = R.drawable.design3;
                ClickDesign4 = R.drawable.design4;
                ClickDesign5 = R.drawable.design5;
                ClickDesign6 = R.drawable.design6;
                ClickDesign7 = R.drawable.design7_clicked;
                ClickDesign8 = R.drawable.design8;
                ClickDesign9 = R.drawable.design9;
                ClickDesign10 = R.drawable.design10;
                ClickDesign11 = R.drawable.design11;
                ClickDesign12 = R.drawable.design12;
                ClickDesign13 = R.drawable.design13;
                ClickDesign14 = R.drawable.design14;
                ClickDesign15 = R.drawable.design15;
                ClickDesign16 = R.drawable.design16;
                ClickDesign17 = R.drawable.design17;
                ClickDesign18 = R.drawable.design18;
                ClickDesign19 = R.drawable.design19;
                ClickDesign(ClickNoDesign, ClickDesign1, ClickDesign2, ClickDesign3, ClickDesign4, ClickDesign5, ClickDesign6, ClickDesign7, ClickDesign8, ClickDesign9, ClickDesign10, ClickDesign11, ClickDesign12, ClickDesign13, ClickDesign14, ClickDesign15, ClickDesign16, ClickDesign17, ClickDesign18, ClickDesign19);
            } else if (ClickSelectDesign == R.id.design8) {
                BackgroundResource = R.drawable.ribbonheart;
                ClickNoDesign = R.drawable.no_design;
                ClickDesign1 = R.drawable.design1;
                ClickDesign2 = R.drawable.design2;
                ClickDesign3 = R.drawable.design3;
                ClickDesign4 = R.drawable.design4;
                ClickDesign5 = R.drawable.design5;
                ClickDesign6 = R.drawable.design6;
                ClickDesign7 = R.drawable.design7;
                ClickDesign8 = R.drawable.design8_clicked;
                ClickDesign9 = R.drawable.design9;
                ClickDesign10 = R.drawable.design10;
                ClickDesign11 = R.drawable.design11;
                ClickDesign12 = R.drawable.design12;
                ClickDesign13 = R.drawable.design13;
                ClickDesign14 = R.drawable.design14;
                ClickDesign15 = R.drawable.design15;
                ClickDesign16 = R.drawable.design16;
                ClickDesign17 = R.drawable.design17;
                ClickDesign18 = R.drawable.design18;
                ClickDesign19 = R.drawable.design19;
                ClickDesign(ClickNoDesign, ClickDesign1, ClickDesign2, ClickDesign3, ClickDesign4, ClickDesign5, ClickDesign6, ClickDesign7, ClickDesign8, ClickDesign9, ClickDesign10, ClickDesign11, ClickDesign12, ClickDesign13, ClickDesign14, ClickDesign15, ClickDesign16, ClickDesign17, ClickDesign18, ClickDesign19);
            } else if (ClickSelectDesign == R.id.design9) {
                BackgroundResource = R.drawable.starsky;
                ClickNoDesign = R.drawable.no_design;
                ClickDesign1 = R.drawable.design1;
                ClickDesign2 = R.drawable.design2;
                ClickDesign3 = R.drawable.design3;
                ClickDesign4 = R.drawable.design4;
                ClickDesign5 = R.drawable.design5;
                ClickDesign6 = R.drawable.design6;
                ClickDesign7 = R.drawable.design7;
                ClickDesign8 = R.drawable.design8;
                ClickDesign9 = R.drawable.design9_clicked;
                ClickDesign10 = R.drawable.design10;
                ClickDesign11 = R.drawable.design11;
                ClickDesign12 = R.drawable.design12;
                ClickDesign13 = R.drawable.design13;
                ClickDesign14 = R.drawable.design14;
                ClickDesign15 = R.drawable.design15;
                ClickDesign16 = R.drawable.design16;
                ClickDesign17 = R.drawable.design17;
                ClickDesign18 = R.drawable.design18;
                ClickDesign19 = R.drawable.design19;
                ClickDesign(ClickNoDesign, ClickDesign1, ClickDesign2, ClickDesign3, ClickDesign4, ClickDesign5, ClickDesign6, ClickDesign7, ClickDesign8, ClickDesign9, ClickDesign10, ClickDesign11, ClickDesign12, ClickDesign13, ClickDesign14, ClickDesign15, ClickDesign16, ClickDesign17, ClickDesign18, ClickDesign19);
            } else if (ClickSelectDesign == R.id.design10) {
                BackgroundResource = R.drawable.lace;
                ClickNoDesign = R.drawable.no_design;
                ClickDesign1 = R.drawable.design1;
                ClickDesign2 = R.drawable.design2;
                ClickDesign3 = R.drawable.design3;
                ClickDesign4 = R.drawable.design4;
                ClickDesign5 = R.drawable.design5;
                ClickDesign6 = R.drawable.design6;
                ClickDesign7 = R.drawable.design7;
                ClickDesign8 = R.drawable.design8;
                ClickDesign9 = R.drawable.design9;
                ClickDesign10 = R.drawable.design10_clicked;
                ClickDesign11 = R.drawable.design11;
                ClickDesign12 = R.drawable.design12;
                ClickDesign13 = R.drawable.design13;
                ClickDesign14 = R.drawable.design14;
                ClickDesign15 = R.drawable.design15;
                ClickDesign16 = R.drawable.design16;
                ClickDesign17 = R.drawable.design17;
                ClickDesign18 = R.drawable.design18;
                ClickDesign19 = R.drawable.design19;
                ClickDesign(ClickNoDesign, ClickDesign1, ClickDesign2, ClickDesign3, ClickDesign4, ClickDesign5, ClickDesign6, ClickDesign7, ClickDesign8, ClickDesign9, ClickDesign10, ClickDesign11, ClickDesign12, ClickDesign13, ClickDesign14, ClickDesign15, ClickDesign16, ClickDesign17, ClickDesign18, ClickDesign19);
            } else if (ClickSelectDesign == R.id.design11) {
                BackgroundResource = R.drawable.night;
                ClickNoDesign = R.drawable.no_design;
                ClickDesign1 = R.drawable.design1;
                ClickDesign2 = R.drawable.design2;
                ClickDesign3 = R.drawable.design3;
                ClickDesign4 = R.drawable.design4;
                ClickDesign5 = R.drawable.design5;
                ClickDesign6 = R.drawable.design6;
                ClickDesign7 = R.drawable.design7;
                ClickDesign8 = R.drawable.design8;
                ClickDesign9 = R.drawable.design9;
                ClickDesign10 = R.drawable.design10;
                ClickDesign11 = R.drawable.design11_clicked;
                ClickDesign12 = R.drawable.design12;
                ClickDesign13 = R.drawable.design13;
                ClickDesign14 = R.drawable.design14;
                ClickDesign15 = R.drawable.design15;
                ClickDesign16 = R.drawable.design16;
                ClickDesign17 = R.drawable.design17;
                ClickDesign18 = R.drawable.design18;
                ClickDesign19 = R.drawable.design19;
                ClickDesign(ClickNoDesign, ClickDesign1, ClickDesign2, ClickDesign3, ClickDesign4, ClickDesign5, ClickDesign6, ClickDesign7, ClickDesign8, ClickDesign9, ClickDesign10, ClickDesign11, ClickDesign12, ClickDesign13, ClickDesign14, ClickDesign15, ClickDesign16, ClickDesign17, ClickDesign18, ClickDesign19);
            } else if (ClickSelectDesign == R.id.design12) {
                BackgroundResource = R.drawable.ribbon;
                ClickNoDesign = R.drawable.no_design;
                ClickDesign1 = R.drawable.design1;
                ClickDesign2 = R.drawable.design2;
                ClickDesign3 = R.drawable.design3;
                ClickDesign4 = R.drawable.design4;
                ClickDesign5 = R.drawable.design5;
                ClickDesign6 = R.drawable.design6;
                ClickDesign7 = R.drawable.design7;
                ClickDesign8 = R.drawable.design8;
                ClickDesign9 = R.drawable.design9;
                ClickDesign10 = R.drawable.design10;
                ClickDesign11 = R.drawable.design11;
                ClickDesign12 = R.drawable.design12_clicked;
                ClickDesign13 = R.drawable.design13;
                ClickDesign14 = R.drawable.design14;
                ClickDesign15 = R.drawable.design15;
                ClickDesign16 = R.drawable.design16;
                ClickDesign17 = R.drawable.design17;
                ClickDesign18 = R.drawable.design18;
                ClickDesign19 = R.drawable.design19;
                ClickDesign(ClickNoDesign, ClickDesign1, ClickDesign2, ClickDesign3, ClickDesign4, ClickDesign5, ClickDesign6, ClickDesign7, ClickDesign8, ClickDesign9, ClickDesign10, ClickDesign11, ClickDesign12, ClickDesign13, ClickDesign14, ClickDesign15, ClickDesign16, ClickDesign17, ClickDesign18, ClickDesign19);
            } else if (ClickSelectDesign == R.id.design13) {
                BackgroundResource = R.drawable.unicorn;
                ClickNoDesign = R.drawable.no_design;
                ClickDesign1 = R.drawable.design1;
                ClickDesign2 = R.drawable.design2;
                ClickDesign3 = R.drawable.design3;
                ClickDesign4 = R.drawable.design4;
                ClickDesign5 = R.drawable.design5;
                ClickDesign6 = R.drawable.design6;
                ClickDesign7 = R.drawable.design7;
                ClickDesign8 = R.drawable.design8;
                ClickDesign9 = R.drawable.design9;
                ClickDesign10 = R.drawable.design10;
                ClickDesign11 = R.drawable.design11;
                ClickDesign12 = R.drawable.design12;
                ClickDesign13 = R.drawable.design13_clicked;
                ClickDesign14 = R.drawable.design14;
                ClickDesign15 = R.drawable.design15;
                ClickDesign16 = R.drawable.design16;
                ClickDesign17 = R.drawable.design17;
                ClickDesign18 = R.drawable.design18;
                ClickDesign19 = R.drawable.design19;
                ClickDesign(ClickNoDesign, ClickDesign1, ClickDesign2, ClickDesign3, ClickDesign4, ClickDesign5, ClickDesign6, ClickDesign7, ClickDesign8, ClickDesign9, ClickDesign10, ClickDesign11, ClickDesign12, ClickDesign13, ClickDesign14, ClickDesign15, ClickDesign16, ClickDesign17, ClickDesign18, ClickDesign19);
            } else if (ClickSelectDesign == R.id.design14) {
                BackgroundResource = R.drawable.diamond;
                ClickNoDesign = R.drawable.no_design;
                ClickDesign1 = R.drawable.design1;
                ClickDesign2 = R.drawable.design2;
                ClickDesign3 = R.drawable.design3;
                ClickDesign4 = R.drawable.design4;
                ClickDesign5 = R.drawable.design5;
                ClickDesign6 = R.drawable.design6;
                ClickDesign7 = R.drawable.design7;
                ClickDesign8 = R.drawable.design8;
                ClickDesign9 = R.drawable.design9;
                ClickDesign10 = R.drawable.design10;
                ClickDesign11 = R.drawable.design11;
                ClickDesign12 = R.drawable.design12;
                ClickDesign13 = R.drawable.design13;
                ClickDesign14 = R.drawable.design14_clicked;
                ClickDesign15 = R.drawable.design15;
                ClickDesign16 = R.drawable.design16;
                ClickDesign17 = R.drawable.design17;
                ClickDesign18 = R.drawable.design18;
                ClickDesign19 = R.drawable.design19;
                ClickDesign(ClickNoDesign, ClickDesign1, ClickDesign2, ClickDesign3, ClickDesign4, ClickDesign5, ClickDesign6, ClickDesign7, ClickDesign8, ClickDesign9, ClickDesign10, ClickDesign11, ClickDesign12, ClickDesign13, ClickDesign14, ClickDesign15, ClickDesign16, ClickDesign17, ClickDesign18, ClickDesign19);
            } else if (ClickSelectDesign == R.id.design15) {
                BackgroundResource = R.drawable.starrainbow;
                ClickNoDesign = R.drawable.no_design;
                ClickDesign1 = R.drawable.design1;
                ClickDesign2 = R.drawable.design2;
                ClickDesign3 = R.drawable.design3;
                ClickDesign4 = R.drawable.design4;
                ClickDesign5 = R.drawable.design5;
                ClickDesign6 = R.drawable.design6;
                ClickDesign7 = R.drawable.design7;
                ClickDesign8 = R.drawable.design8;
                ClickDesign9 = R.drawable.design9;
                ClickDesign10 = R.drawable.design10;
                ClickDesign11 = R.drawable.design11;
                ClickDesign12 = R.drawable.design12;
                ClickDesign13 = R.drawable.design13;
                ClickDesign14 = R.drawable.design14;
                ClickDesign15 = R.drawable.design15_clicked;
                ClickDesign16 = R.drawable.design16;
                ClickDesign17 = R.drawable.design17;
                ClickDesign18 = R.drawable.design18;
                ClickDesign19 = R.drawable.design19;
                ClickDesign(ClickNoDesign, ClickDesign1, ClickDesign2, ClickDesign3, ClickDesign4, ClickDesign5, ClickDesign6, ClickDesign7, ClickDesign8, ClickDesign9, ClickDesign10, ClickDesign11, ClickDesign12, ClickDesign13, ClickDesign14, ClickDesign15, ClickDesign16, ClickDesign17, ClickDesign18, ClickDesign19);
            } else if (ClickSelectDesign == R.id.design16) {
                BackgroundResource = R.drawable.angel;
                ClickNoDesign = R.drawable.no_design;
                ClickDesign1 = R.drawable.design1;
                ClickDesign2 = R.drawable.design2;
                ClickDesign3 = R.drawable.design3;
                ClickDesign4 = R.drawable.design4;
                ClickDesign5 = R.drawable.design5;
                ClickDesign6 = R.drawable.design6;
                ClickDesign7 = R.drawable.design7;
                ClickDesign8 = R.drawable.design8;
                ClickDesign9 = R.drawable.design9;
                ClickDesign10 = R.drawable.design10;
                ClickDesign11 = R.drawable.design11;
                ClickDesign12 = R.drawable.design12;
                ClickDesign13 = R.drawable.design13;
                ClickDesign14 = R.drawable.design14;
                ClickDesign15 = R.drawable.design15;
                ClickDesign16 = R.drawable.design16_clicked;
                ClickDesign17 = R.drawable.design17;
                ClickDesign18 = R.drawable.design18;
                ClickDesign19 = R.drawable.design19;
                ClickDesign(ClickNoDesign, ClickDesign1, ClickDesign2, ClickDesign3, ClickDesign4, ClickDesign5, ClickDesign6, ClickDesign7, ClickDesign8, ClickDesign9, ClickDesign10, ClickDesign11, ClickDesign12, ClickDesign13, ClickDesign14, ClickDesign15, ClickDesign16, ClickDesign17, ClickDesign18, ClickDesign19);
            } else if (ClickSelectDesign == R.id.design17) {
                BackgroundResource = R.drawable.ribbon2;
                ClickNoDesign = R.drawable.no_design;
                ClickDesign1 = R.drawable.design1;
                ClickDesign2 = R.drawable.design2;
                ClickDesign3 = R.drawable.design3;
                ClickDesign4 = R.drawable.design4;
                ClickDesign5 = R.drawable.design5;
                ClickDesign6 = R.drawable.design6;
                ClickDesign7 = R.drawable.design7;
                ClickDesign8 = R.drawable.design8;
                ClickDesign9 = R.drawable.design9;
                ClickDesign10 = R.drawable.design10;
                ClickDesign11 = R.drawable.design11;
                ClickDesign12 = R.drawable.design12;
                ClickDesign13 = R.drawable.design13;
                ClickDesign14 = R.drawable.design14;
                ClickDesign15 = R.drawable.design15;
                ClickDesign16 = R.drawable.design16;
                ClickDesign17 = R.drawable.design17_clicked;
                ClickDesign18 = R.drawable.design18;
                ClickDesign19 = R.drawable.design19;
                ClickDesign(ClickNoDesign, ClickDesign1, ClickDesign2, ClickDesign3, ClickDesign4, ClickDesign5, ClickDesign6, ClickDesign7, ClickDesign8, ClickDesign9, ClickDesign10, ClickDesign11, ClickDesign12, ClickDesign13, ClickDesign14, ClickDesign15, ClickDesign16, ClickDesign17, ClickDesign18, ClickDesign19);
            } else if (ClickSelectDesign == R.id.design18) {
                BackgroundResource = R.drawable.rainbow;
                ClickNoDesign = R.drawable.no_design;
                ClickDesign1 = R.drawable.design1;
                ClickDesign2 = R.drawable.design2;
                ClickDesign3 = R.drawable.design3;
                ClickDesign4 = R.drawable.design4;
                ClickDesign5 = R.drawable.design5;
                ClickDesign6 = R.drawable.design6;
                ClickDesign7 = R.drawable.design7;
                ClickDesign8 = R.drawable.design8;
                ClickDesign9 = R.drawable.design9;
                ClickDesign10 = R.drawable.design10;
                ClickDesign11 = R.drawable.design11;
                ClickDesign12 = R.drawable.design12;
                ClickDesign13 = R.drawable.design13;
                ClickDesign14 = R.drawable.design14;
                ClickDesign15 = R.drawable.design15;
                ClickDesign16 = R.drawable.design16;
                ClickDesign17 = R.drawable.design17;
                ClickDesign18 = R.drawable.design18_clicked;
                ClickDesign19 = R.drawable.design19;
                ClickDesign(ClickNoDesign, ClickDesign1, ClickDesign2, ClickDesign3, ClickDesign4, ClickDesign5, ClickDesign6, ClickDesign7, ClickDesign8, ClickDesign9, ClickDesign10, ClickDesign11, ClickDesign12, ClickDesign13, ClickDesign14, ClickDesign15, ClickDesign16, ClickDesign17, ClickDesign18, ClickDesign19);
            } else if (ClickSelectDesign == R.id.design19) {
                BackgroundResource = R.drawable.angel2;
                ClickNoDesign = R.drawable.no_design;
                ClickDesign1 = R.drawable.design1;
                ClickDesign2 = R.drawable.design2;
                ClickDesign3 = R.drawable.design3;
                ClickDesign4 = R.drawable.design4;
                ClickDesign5 = R.drawable.design5;
                ClickDesign6 = R.drawable.design6;
                ClickDesign7 = R.drawable.design7;
                ClickDesign8 = R.drawable.design8;
                ClickDesign9 = R.drawable.design9;
                ClickDesign10 = R.drawable.design10;
                ClickDesign11 = R.drawable.design11;
                ClickDesign12 = R.drawable.design12;
                ClickDesign13 = R.drawable.design13;
                ClickDesign14 = R.drawable.design14;
                ClickDesign15 = R.drawable.design15;
                ClickDesign16 = R.drawable.design16;
                ClickDesign17 = R.drawable.design17;
                ClickDesign18 = R.drawable.design18;
                ClickDesign19 = R.drawable.design19_clicked;
                ClickDesign(ClickNoDesign, ClickDesign1, ClickDesign2, ClickDesign3, ClickDesign4, ClickDesign5, ClickDesign6, ClickDesign7, ClickDesign8, ClickDesign9, ClickDesign10, ClickDesign11, ClickDesign12, ClickDesign13, ClickDesign14, ClickDesign15, ClickDesign16, ClickDesign17, ClickDesign18, ClickDesign19);
            }
        }
    };

    //アプリを閉じても選択されたデザインがわかるように、枠線のあるなしの値を保持。
    public void DesignShared(int ClickNoDesign, int ClickDesign1, int ClickDesign2, int ClickDesign3, int ClickDesign4, int ClickDesign5, int ClickDesign6, int ClickDesign7, int ClickDesign8, int ClickDesign9, int ClickDesign10, int ClickDesign11, int ClickDesign12, int ClickDesign13, int ClickDesign14, int ClickDesign15, int ClickDesign16, int ClickDesign17, int ClickDesign18, int ClickDesign19) {
        SharedPreferences sp = getSharedPreferences("hoge", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("ClickNoDesign", ClickNoDesign);
        editor.commit();
        SharedPreferences sp1 = getSharedPreferences("hoge", MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sp1.edit();
        editor1.putInt("ClickDesign1", ClickDesign1);
        editor1.commit();
        SharedPreferences sp2 = getSharedPreferences("hoge", MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sp2.edit();
        editor2.putInt("ClickDesign2", ClickDesign2);
        editor2.commit();
        SharedPreferences sp3 = getSharedPreferences("hoge", MODE_PRIVATE);
        SharedPreferences.Editor editor3 = sp3.edit();
        editor3.putInt("ClickDesign3", ClickDesign3);
        editor3.commit();
        SharedPreferences sp4 = getSharedPreferences("hoge", MODE_PRIVATE);
        SharedPreferences.Editor editor4 = sp4.edit();
        editor4.putInt("ClickDesign4", ClickDesign4);
        editor4.commit();
        SharedPreferences sp5 = getSharedPreferences("hoge", MODE_PRIVATE);
        SharedPreferences.Editor editor5 = sp5.edit();
        editor5.putInt("ClickDesign5", ClickDesign5);
        editor5.commit();
        SharedPreferences sp6 = getSharedPreferences("hoge", MODE_PRIVATE);
        SharedPreferences.Editor editor6 = sp6.edit();
        editor6.putInt("ClickDesign6", ClickDesign6);
        editor6.commit();
        SharedPreferences sp7 = getSharedPreferences("hoge", MODE_PRIVATE);
        SharedPreferences.Editor editor7 = sp7.edit();
        editor7.putInt("ClickDesign7", ClickDesign7);
        editor7.commit();
        SharedPreferences sp8 = getSharedPreferences("hoge", MODE_PRIVATE);
        SharedPreferences.Editor editor8 = sp8.edit();
        editor8.putInt("ClickDesign8", ClickDesign8);
        editor8.commit();
        SharedPreferences sp9 = getSharedPreferences("hoge", MODE_PRIVATE);
        SharedPreferences.Editor editor9 = sp9.edit();
        editor9.putInt("ClickDesign9", ClickDesign9);
        editor9.commit();
        SharedPreferences sp10 = getSharedPreferences("hoge", MODE_PRIVATE);
        SharedPreferences.Editor editor10 = sp10.edit();
        editor10.putInt("ClickDesign10", ClickDesign10);
        editor10.commit();
        SharedPreferences sp11 = getSharedPreferences("hoge", MODE_PRIVATE);
        SharedPreferences.Editor editor11 = sp11.edit();
        editor11.putInt("ClickDesign11", ClickDesign11);
        editor11.commit();
        SharedPreferences sp12 = getSharedPreferences("hoge", MODE_PRIVATE);
        SharedPreferences.Editor editor12 = sp12.edit();
        editor12.putInt("ClickDesign12", ClickDesign12);
        editor12.commit();
        SharedPreferences sp13 = getSharedPreferences("hoge", MODE_PRIVATE);
        SharedPreferences.Editor editor13 = sp13.edit();
        editor13.putInt("ClickDesign13", ClickDesign13);
        editor13.commit();
        SharedPreferences sp14 = getSharedPreferences("hoge", MODE_PRIVATE);
        SharedPreferences.Editor editor14 = sp14.edit();
        editor14.putInt("ClickDesign14", ClickDesign14);
        editor14.commit();
        SharedPreferences sp15 = getSharedPreferences("hoge", MODE_PRIVATE);
        SharedPreferences.Editor editor15 = sp15.edit();
        editor15.putInt("ClickDesign15", ClickDesign15);
        editor15.commit();
        SharedPreferences sp16 = getSharedPreferences("hoge", MODE_PRIVATE);
        SharedPreferences.Editor editor16 = sp16.edit();
        editor16.putInt("ClickDesign16", ClickDesign16);
        editor16.commit();
        SharedPreferences sp17 = getSharedPreferences("hoge", MODE_PRIVATE);
        SharedPreferences.Editor editor17 = sp17.edit();
        editor17.putInt("ClickDesign17", ClickDesign17);
        editor17.commit();
        SharedPreferences sp18 = getSharedPreferences("hoge", MODE_PRIVATE);
        SharedPreferences.Editor editor18 = sp18.edit();
        editor18.putInt("ClickDesign18", ClickDesign18);
        editor18.commit();
        SharedPreferences sp19 = getSharedPreferences("hoge", MODE_PRIVATE);
        SharedPreferences.Editor editor19 = sp19.edit();
        editor19.putInt("ClickDesign19", ClickDesign19);
        editor19.commit();
    }

    //選択によって枠線のあるなしをそれぞれImageViewにセット。
    public void ClickDesign(int ClickNoDesign, int ClickDesign1, int ClickDesign2, int ClickDesign3, int ClickDesign4, int ClickDesign5, int ClickDesign6, int ClickDesign7, int ClickDesign8, int ClickDesign9, int ClickDesign10, int ClickDesign11, int ClickDesign12, int ClickDesign13, int ClickDesign14, int ClickDesign15, int ClickDesign16, int ClickDesign17, int ClickDesign18, int ClickDesign19) {
        mNoDesign.setImageResource(ClickNoDesign);
        mDesign1.setImageResource(ClickDesign1);
        mDesign2.setImageResource(ClickDesign2);
        mDesign3.setImageResource(ClickDesign3);
        mDesign4.setImageResource(ClickDesign4);
        mDesign5.setImageResource(ClickDesign5);
        mDesign6.setImageResource(ClickDesign6);
        mDesign7.setImageResource(ClickDesign7);
        mDesign8.setImageResource(ClickDesign8);
        mDesign9.setImageResource(ClickDesign9);
        mDesign10.setImageResource(ClickDesign10);
        mDesign11.setImageResource(ClickDesign11);
        mDesign12.setImageResource(ClickDesign12);
        mDesign13.setImageResource(ClickDesign13);
        mDesign14.setImageResource(ClickDesign14);
        mDesign15.setImageResource(ClickDesign15);
        mDesign16.setImageResource(ClickDesign16);
        mDesign17.setImageResource(ClickDesign17);
        mDesign18.setImageResource(ClickDesign18);
        mDesign19.setImageResource(ClickDesign19);
    }

    public void CategoryTitle() {
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        final View dialog_view = inflater.inflate(R.layout.category_main, null);

        mInputTitleEdit1 = dialog_view.findViewById(R.id.title1);
        mInputTitleEdit2 = dialog_view.findViewById(R.id.title2);
        mInputTitleEdit3 = dialog_view.findViewById(R.id.title3);
        mInputTitleEdit4 = dialog_view.findViewById(R.id.title4);
        mInputTitleEdit5 = dialog_view.findViewById(R.id.title5);

        //保持した値(既に設定されている文字or前回入力した文字)を予めセット
        mInputTitleEdit1.setText(CategoryTitle1);
        mInputTitleEdit2.setText(CategoryTitle2);
        mInputTitleEdit3.setText(CategoryTitle3);
        mInputTitleEdit4.setText(CategoryTitle4);
        mInputTitleEdit5.setText(CategoryTitle5);

        new AlertDialog.Builder(MainActivity.this, R.style.MyAlertDialogStyle)
                .setView(dialog_view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //入力されたテキストを取得
                        String InputCategoryTitle1 = mInputTitleEdit1.getText().toString();
                        String InputCategoryTitle2 = mInputTitleEdit2.getText().toString();
                        String InputCategoryTitle3 = mInputTitleEdit3.getText().toString();
                        String InputCategoryTitle4 = mInputTitleEdit4.getText().toString();
                        String InputCategoryTitle5 = mInputTitleEdit5.getText().toString();

                        //アプリを閉じても入力した文字を取得するために保持。
                        SharedPreferences sp = getSharedPreferences("hoge", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("Title1", InputCategoryTitle1);
                        editor.putString("Title2", InputCategoryTitle2);
                        editor.putString("Title3", InputCategoryTitle3);
                        editor.putString("Title4", InputCategoryTitle4);
                        editor.putString("Title5", InputCategoryTitle5);
                        editor.commit();

                        setViews();
                    }
                })
                .setNegativeButton("Close", null)
                .show();
    }

    //FloatingActionButton
    View.OnClickListener FabListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = viewPager.getCurrentItem();//ページの位置の値を取得

            Intent intent = new Intent(MainActivity.this, InputActivity.class);
            intent.putExtra("page_number", position);//ページの位置の値を渡す
            startActivity(intent);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //オプションメニュー
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search://検索
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("Design", BackgroundResource);
                startActivity(intent);
                return true;
            case R.id.action_sort://並び替え
                choosePrefecture();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //並び替えの分岐のため
    int fra1 = 0;
    int fra2 = 0;
    int fra3 = 0;
    int fra4 = 0;
    int fra5 = 0;

    //ListViewの並び替え
    public void choosePrefecture() {
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        final View dialog_view = inflater.inflate(R.layout.sort_alert, null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this, R.style.MyAlertDialogStyle);
        alert.setView(dialog_view);
        alert.setNegativeButton("CLOSE", null);
        final AlertDialog dialog = alert.show();
        Button button = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView item1 = dialog_view.findViewById(R.id.item1);
        TextView item2 = dialog_view.findViewById(R.id.item2);
        TextView item3 = dialog_view.findViewById(R.id.item3);

        //新しい順
        item1.setOnClickListener(new View.OnClickListener() {
            Fragment fragment = adapter.getItem(viewPager.getCurrentItem());
            @Override
            public void onClick(View v) {
                if (fragment instanceof Fragment_1) {
                    Fragment_1 fragment1 = (Fragment_1) fragment;
                    fragment1.dateDeListView();

                    fra1 = 1;

                    //ページを移動したり、アプリを起動したときに設定を反映するため
                    SharedPreferences sp = getSharedPreferences("hoge", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("fra1", fra1);
                    editor.commit();

                    dialog.dismiss();
                } else if (fragment instanceof Fragment_2) {
                    Fragment_2 fragment2 = (Fragment_2) fragment;
                    fragment2.dateDeListView();

                    fra2 = 1;

                    SharedPreferences sp = getSharedPreferences("hoge", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("fra2", fra2);
                    editor.commit();

                    dialog.dismiss();
                } else if (fragment instanceof Fragment_3) {
                    Fragment_3 fragment3 = (Fragment_3) fragment;
                    fragment3.dateDeListView();

                    fra3 = 1;

                    SharedPreferences sp = getSharedPreferences("hoge", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("fra3", fra3);
                    editor.commit();

                    dialog.dismiss();
                } else if (fragment instanceof Fragment_4) {
                    Fragment_4 fragment4 = (Fragment_4) fragment;
                    fragment4.dateDeListView();

                    fra4 = 1;

                    SharedPreferences sp = getSharedPreferences("hoge", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("fra4", fra4);
                    editor.commit();

                    dialog.dismiss();
                } else if (fragment instanceof Fragment_5) {
                    Fragment_5 fragment5 = (Fragment_5) fragment;
                    fragment5.dateDeListView();

                    fra5 = 1;

                    SharedPreferences sp = getSharedPreferences("hoge", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("fra5", fra5);
                    editor.commit();

                    dialog.dismiss();
                }
            }
        });

        //古い順
        item2.setOnClickListener(new View.OnClickListener() {
            Fragment fragment = adapter.getItem(viewPager.getCurrentItem());
            @Override
            public void onClick(View v) {
                if (fragment instanceof Fragment_1) {
                    Fragment_1 fragment1 = (Fragment_1) fragment;
                    fragment1.dateAsListView();

                    fra1 = 2;

                    SharedPreferences sp = getSharedPreferences("hoge", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("fra1", fra1);
                    editor.commit();

                    dialog.dismiss();

                } else if (fragment instanceof Fragment_2) {
                    Fragment_2 fragment2 = (Fragment_2) fragment;
                    fragment2.dateAsListView();

                    fra2 = 2;

                    SharedPreferences sp = getSharedPreferences("hoge", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("fra2", fra2);
                    editor.commit();

                    dialog.dismiss();
                } else if (fragment instanceof Fragment_3) {
                    Fragment_3 fragment3 = (Fragment_3) fragment;
                    fragment3.dateAsListView();

                    fra3 = 2;

                    SharedPreferences sp = getSharedPreferences("hoge", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("fra3", fra3);
                    editor.commit();

                    dialog.dismiss();
                } else if (fragment instanceof Fragment_4) {
                    Fragment_4 fragment4 = (Fragment_4) fragment;
                    fragment4.dateAsListView();

                    fra4 = 2;

                    SharedPreferences sp = getSharedPreferences("hoge", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("fra4", fra4);
                    editor.commit();

                    dialog.dismiss();
                } else if (fragment instanceof Fragment_5) {
                    Fragment_5 fragment5 = (Fragment_5) fragment;
                    fragment5.dateAsListView();

                    fra5 = 2;

                    SharedPreferences sp = getSharedPreferences("hoge", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("fra5", fra5);
                    editor.commit();

                    dialog.dismiss();
                }
            }
        });

        //五十音順
        item3.setOnClickListener(new View.OnClickListener() {
            Fragment fragment = adapter.getItem(viewPager.getCurrentItem());
            @Override
            public void onClick(View v) {
                if (fragment instanceof Fragment_1) {
                    Fragment_1 fragment1 = (Fragment_1) fragment;
                    fragment1.memoListView();

                    fra1 = 3;

                    SharedPreferences sp = getSharedPreferences("hoge", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("fra1", fra1);
                    editor.commit();

                    dialog.dismiss();
                } else if (fragment instanceof Fragment_2) {
                    Fragment_2 fragment2 = (Fragment_2) fragment;
                    fragment2.memoListView();

                    fra2 = 3;

                    SharedPreferences sp = getSharedPreferences("hoge", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("fra2", fra2);
                    editor.commit();

                    dialog.dismiss();
                } else if (fragment instanceof Fragment_3) {
                    Fragment_3 fragment3 = (Fragment_3) fragment;
                    fragment3.memoListView();

                    fra3 = 3;

                    SharedPreferences sp = getSharedPreferences("hoge", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("fra3", fra3);
                    editor.commit();

                    dialog.dismiss();
                } else if (fragment instanceof Fragment_4) {
                    Fragment_4 fragment4 = (Fragment_4) fragment;
                    fragment4.memoListView();

                    fra4 = 3;

                    SharedPreferences sp = getSharedPreferences("hoge", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("fra4", fra4);
                    editor.commit();

                    dialog.dismiss();
                } else if (fragment instanceof Fragment_5) {
                    Fragment_5 fragment5 = (Fragment_5) fragment;
                    fragment5.memoListView();

                    fra5 = 3;

                    SharedPreferences sp = getSharedPreferences("hoge", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("fra5", fra5);
                    editor.commit();

                    dialog.dismiss();
                }
            }
        });
    }
}