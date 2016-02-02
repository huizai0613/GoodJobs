package cn.goodjobs.applyjobs.bean;

public class JobFairChild {
    private String newsID;
    private String newstitle;
    private String newsdate;
    private String typeName;
    private String newsStartDate;
    private String newsCityName;

    public String getNewsID() {
        return newsID;
    }

    public void setNewsID(String newsID) {
        this.newsID = newsID;
    }

    public void setNewsStartDate(String newsID) {
        this.newsStartDate = newsID;
    }

    public void setNewsCityName(String newsID) {
        this.newsCityName = newsID;
    }

    public String getNewsStartDate() {
        return newsStartDate;
    }

    public String getNewsCityName() {
        return newsCityName;
    }

    public String getNewstitle() {
        return newstitle;
    }

    public void setNewstitle(String newstitle) {
        this.newstitle = newstitle;
    }

    public String getNewsdate() {
        return newsdate;
    }

    public void setNewsdate(String newsdate) {
        this.newsdate = newsdate;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
