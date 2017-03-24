package ${doPackage};

import java.util.Date;

public class ${className}DO {


<#list list as item>
    private ${item.javaType} ${item.propName};
</#list>


<#list list as item>
    public ${item.javaType} ${item.getMethod}() {
      return ${item.propName};
    }

    public void ${item.setMethod}(${item.javaType} ${item.propName}) {
      this.${item.propName} = ${item.propName};
    }

</#list>

}

