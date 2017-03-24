package ${parentPackageName};

import com.google.inject.ImplementedBy;
import guda.mvcx.core.annotation.biz.Biz;
import guda.mvcx.core.helper.BizResult;
import guda.mvcx.core.helper.PageQuery;
import ${parentPackageName}.impl.${doName}BizImpl;
import ${doClassName};

@Biz
@ImplementedBy(${doName}BizImpl.class)
public interface ${doName}Biz {

    BizResult detail(long id);

    BizResult list(PageQuery pageQuery);

    BizResult delete(long id);

    BizResult create(${doName}DO ${doNameLower}DO);

    BizResult update(${doName}DO ${doNameLower}DO);

}
