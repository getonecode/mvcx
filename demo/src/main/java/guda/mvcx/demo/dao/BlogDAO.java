package guda.mvcx.demo.dao;

import java.util.List;

import guda.mvcx.core.helper.PageQuery;
import guda.mvcx.demo.dao.model.BlogDO;
/**
* Created by well on 2017/3/24.
*/
public interface BlogDAO {

    Long insert(BlogDO admin);


    BlogDO selectById(Long adminId);


    boolean delById(Long blogId);

    List<BlogDO> selectByIds(List idsList);


    List<BlogDO> selectForPage(PageQuery pageQuery);

    int countForPage(PageQuery pageQuery);

    void updateByIdSelective(BlogDO blogDO);
}
