package com.example.tmnt.uploadphoto;

/**
 * Created by tmnt on 2016/8/23.
 */
public class VersionInfo {

    private String id;
    private String version;
    private String url;
    private String update_time;

    public VersionInfo() {
    }

    public VersionInfo(String id, String version, String url, String update_time) {
        this.id = id;
        this.version = version;
        this.url = url;
        this.update_time = update_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
}
