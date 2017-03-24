package guda.mvcx.demo.form;

import guda.mvcx.core.helper.Form;
import guda.mvcx.demo.dao.model.BlogDO;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by well on 2017/3/24.
 */
public class BlogForm extends Form{

    @NotEmpty(message = "标题不能为空")
    private String blogTitle;
    @NotEmpty(message = "内容不能为空")
    private Integer blogContent;
    @NotNull(message = "状态不能为空")
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
