package ${parentPackageName}.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import guda.mvcx.core.helper.BizResult;
import guda.mvcx.core.helper.PageQuery;
import ${parentPackageName}.${doName}Biz;
import ${daoClassName};
import ${doClassName};
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Singleton
public class ${doName}BizImpl implements ${doName}Biz{

    private final  Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    private ${doName}DAO ${doNameLower}DAO;

    @Override
    public BizResult detail(long id) {
        BizResult bizResult = new BizResult();
        try{
            ${doName}DO ${doNameLower}DO = ${doNameLower}DAO.selectById(id);
            bizResult.data.put("${doNameLower}DO", ${doNameLower}DO);
            bizResult.success = true;
        }catch(Exception e){
            logger.error("query ${doNameLower} error",e);
        }
        return bizResult;
    }

    @Override
    public BizResult list(PageQuery pageQuery) {
        BizResult bizResult = new BizResult();
        try {
            int totalCount = ${doNameLower}DAO.countForPage(pageQuery);
            pageQuery.setTotalCount(totalCount);
            List<${doName}DO> ${doNameLower}List = ${doNameLower}DAO.selectForPage(pageQuery);
            bizResult.data.put("${doNameLower}List", ${doNameLower}List);
            bizResult.data.put("query", pageQuery);
            bizResult.success = true;
        } catch (Exception e) {
            logger.error("view ${doNameLower} list error", e);
        }
        return bizResult;
    }

    @Override
    public BizResult delete(long id) {
        BizResult bizResult = new BizResult();
        try {
            ${doNameLower}DAO.delById(id);
            bizResult.success = true;
        } catch (Exception e) {
            logger.error("delete ${doNameLower} error", e);
        }
        return bizResult;
    }

    @Override
    public BizResult create(${doName}DO ${doNameLower}DO) {
        BizResult bizResult = new BizResult();
        try {
            long id = ${doNameLower}DAO.insert(${doNameLower}DO);
            bizResult.data.put("id", id);
            bizResult.success = true;
        } catch (Exception e) {
            logger.error("create ${doNameLower} error", e);
        }
        return bizResult;
    }

    @Override
    public BizResult update(${doName}DO ${doNameLower}DO) {
        BizResult bizResult = new BizResult();
        try {
            ${doNameLower}DAO.updateByIdSelective(${doNameLower}DO);
            bizResult.success = true;
        } catch (Exception e) {
            logger.error("update ${doNameLower} error", e);
        }
        return bizResult;
    }

}

