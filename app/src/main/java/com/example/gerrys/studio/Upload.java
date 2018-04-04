package com.example.gerrys.studio;

/**
 * Created by Belal on 8/25/2017.
 */
public class Upload {

    public String uploader;
    public String url;
    public String classs;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Upload() {
    }

    public Upload(String uploader, String url,String classs) {
        this.uploader = uploader;
        this.url = url;
        this.classs=classs;
    }

  public String getUploader() {
      return uploader;
  }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getClasss() {
        return classs;
    }

    public void setClasss(String classs) {
        this.classs = classs;
    }
}