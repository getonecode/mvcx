package guda.mvcx.demo.biz;

import com.google.inject.ImplementedBy;
import guda.mvcx.core.annotation.biz.Biz;
import guda.mvcx.core.helper.BizResult;
import guda.mvcx.core.helper.PageQuery;
import guda.mvcx.demo.biz.impl.BlogBizImpl;
import guda.mvcx.demo.dao.model.BlogDO;

/**
 * Created by well on 2017/3/24.
 */
@Biz
@ImplementedBy(BlogBizImpl.class)
public interface BlogBiz {

    BizResult detail(long id);

    BizResult list(PageQuery pageQuery);

    BizResult delete(long id);

    BizResult create(BlogDO blogDO);

    BizResult update(BlogDO blogDO);
}
