package com.healthdesktop.app.filechangeapp.bean;

import org.litepal.crud.LitePalSupport;

public class VideoBean extends LitePalSupport {


    private Long id; // 主键,int类型,数据库建表时此字段会设为自增长

    private String videoid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVideoid() {
        return videoid;
    }

    public void setVideoid(String videoid) {
        this.videoid = videoid;
    }
}
