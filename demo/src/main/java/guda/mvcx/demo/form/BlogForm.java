package guda.mvcx.demo.form;

import guda.mvcx.core.helper.Form;
import guda.mvcx.demo.dao.model.BlogDO;

/**
 * Created by well on 2017/3/24.
 */
public class BlogForm extends Form{

    private String blogTitle;
    private Integer blogContent;
    private Integer blogStatus;

    public BlogDO toDO(){
        BlogDO blogDO=new BlogDO();
        blogDO.setBlogContent(blogContent);
        blogDO.setBlogTitle(blogTitle);
        blogDO.setBlogStatus(blogStatus);
        return blogDO;
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
}
