package guda.mvcx.demo.biz.enums;

/**
 * Created by well on 2017/3/24.
 */
public enum BooleanEnum {


    TRUE(1,"是"),FALSE(0,"否");


    private int val;
    private String desc;



    BooleanEnum(int status, String desc){
        this.val = status;
        this.desc=desc;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static BooleanEnum  getByVal(int val){
        BooleanEnum[] values = BooleanEnum.values();
        for(BooleanEnum booleanEnum:values){
            if(booleanEnum.getVal() == val){
                return booleanEnum;
            }
        }
        return null;
    }
}
