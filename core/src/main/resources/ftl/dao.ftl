package ${daoIntfPackage};


import java.util.List;

import guda.mvcx.core.helper.PageQuery;
import ${doPackage}.${className}DO;

public interface ${className}DAO {

        Long insert(${className}DO ${doNameLower}DO);

        ${className}DO selectById(Long <#list list as l><#if l_index == 0>${l.propName}</#if></#list>);

        boolean delById(Long <#list list as l><#if l_index == 0>${l.propName}</#if></#list>);

        List<${className}DO> selectByIds(List idsList);

        List<${className}DO> selectForPage(PageQuery pageQuery);

        int countForPage(PageQuery pageQuery);

        void updateByIdSelective(${className}DO  ${doNameLower}DO);
}


