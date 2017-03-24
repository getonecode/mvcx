package guda.mvcx.gen.common;

/**
 * Created by well on 2017/3/28.
 */
public class DOField {

    public String name;

    public String cnName;

    public String upperName;

    public Class type;

    public String typeName;

    public int order;

    public boolean inSearchForm;

    public boolean canNull = true;

    public boolean isCanNull() {
        return canNull;
    }

    public String getUpperName() {
        return upperName;
    }

    public void setUpperName(String upperName) {
        this.upperName = upperName;
    }

    public void setCanNull(boolean canNull) {
        this.canNull = canNull;
    }

    public String getName() {
        return name;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isInSearchForm() {
        return inSearchForm;
    }

    public void setInSearchForm(boolean inSearchForm) {
        this.inSearchForm = inSearchForm;
    }


}
