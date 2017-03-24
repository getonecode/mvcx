package ${parentPackageName}.form;

import ${doClassName};

import javax.validation.constraints.NotNull;


public class ${doName}EditForm extends ${doName}Form{

    @NotNull(message = "不能为空")
    private Long ${doNameLower}Id;

    public void init(${doName}DO ${doNameLower}DO){
        if(${doNameLower}DO==null){
            return;
        }
    <#list doFieldList as v >
        set${v.upperName}(${doNameLower}DO.get${v.upperName}());
    </#list>
    }

    public ${doName}DO toDO(){
        ${doName}DO ${doNameLower}DO=super.toDO();
        set${doName}Id(${doNameLower}Id);
        return ${doNameLower}DO;
    }

    public Long get${doName}Id() {
        return ${doNameLower}Id;
    }

    public void set${doName}Id(Long ${doNameLower}Id) {
        this.${doNameLower}Id = ${doNameLower}Id;
    }
}

