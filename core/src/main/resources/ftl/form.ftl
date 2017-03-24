package ${parentPackageName}.form;

import guda.mvcx.core.helper.Form;
import ${doClassName};
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class ${doName}Form extends Form{

<#list doFieldList as f>
    private ${f.typeName} ${f.name};
</#list>


    public ${doName}DO toDO(){
        ${doName}DO ${doNameLower}DO=new ${doName}DO();
<#assign id=doNameLower + "Id"/>
<#list doFieldList as v>
        <#if v.name != id> ${doNameLower}DO.set${v.upperName}(this.${v.name});</#if>
</#list>
        return ${doNameLower}DO;
    }


<#list doFieldList as v>
    public ${v.typeName} get${v.upperName}() {
        return ${v.name};
    }

    public void set${v.upperName}(${v.typeName} ${v.name}) {
        this.${v.name} = ${v.name};
    }
</#list>

}


