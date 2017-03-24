package guda.mvcx.demo.dao.model;

import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * Created by well on 2017/3/24.
 */
@Alias("blogDO")
public class BlogDO {

    private Long blogId;
    private String blogTitle;
    private Integer blogContent;
    private Integer blogStatus;
    private Date gmtCreate;
    private Date gmtUpdate;

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }

    public Integer getBlogContent() {
        return blogContent;
    }

    public void setBlogContent(Integer blogContent) {
        this.blogContent = blogContent;
    }

    public Integer getBlogStatus() {
        return blogStatus;
    }

    public void setBlogStatus(Integer blogStatus) {
        this.blogStatus = blogStatus;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtUpdate() {
        return gmtUpdate;
    }

    public void setGmtUpdate(Date gmtUpdate) {
        this.gmtUpdate = gmtUpdate;
    }
}
