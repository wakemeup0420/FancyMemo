package com.wakemeup.erika.takenoue.fancymemo;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Memo  extends RealmObject implements Serializable {
    private String memo; // 内容
    private Date date; // 日時
    int page;//ViewPagerのページ数
    int fav;//お気に入りに登録されたか否か

    // id をプライマリーキーとして設定
    @PrimaryKey
    private int id;

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getFav() {
        return fav;
    }

    public void setFav(int fav) {
        this.fav = fav;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}