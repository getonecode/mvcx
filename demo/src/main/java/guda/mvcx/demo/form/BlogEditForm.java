package guda.mvcx.demo.form;

import guda.mvcx.demo.dao.model.BlogDO;

import javax.validation.constraints.NotNull;

/**
 * Created by well on 2017/3/24.
 */
public class BlogEditForm extends BlogForm{

    @NotNull(message = "不能为空")
    private Long blogId;

    public void init(BlogDO blogDO){
        if(blogDO==null){
            return;
        }
        setBlogId(blogDO.getBlogId());
        setBlogContent(blogDO.getBlogContent());
        setBlogStatus(blogDO.getBlogStatus());
        setBlogTitle(blogDO.getBlogTitle());
    }

    public BlogDO toDO(){
        BlogDO blogDO=super.toDO();
        blogDO.setBlogId(blogId);
        return blogDO;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }
}
