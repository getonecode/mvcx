package guda.mvcx.gen;




import guda.mvcx.gen.helper.FreemakerHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by foodoon on 2014/6/27.
 */
public class GenBiz {

    private String bizVmName = "biz.ftl";

    private String bizImplVmName = "biz-impl.ftl";

    private String formVmName = "form.ftl";

    private String formEditVmName = "edit-form.ftl";

    private GenContext genContext;

    public GenBiz(GenContext genContext) {
        this.genContext = genContext;
    }

    public void gen() throws Exception {
        String parentPackageName = genContext.getParentPackageName();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("parentPackageName", parentPackageName);
        params.put("doName", genContext.getDoName());
        params.put("doClassName", genContext.getDoClassName());
        String daoClass = genContext.getDoClassName().substring(0, genContext.getDoClassName().length() - 2) + "DAO";
        daoClass = daoClass.replace(".model", "");
        daoClass = daoClass.replace("\\.biz", ".dao");
        params.put("daoClassName", daoClass);
        params.put("doNameLower", genContext.getDoNameLower());

        createVM(params, bizVmName, genContext.getBizFile());
        createVM(params, bizImplVmName, genContext.getBizImplFile());

        params.put("doFieldList", genContext.getDoFieldList());
        createVM(params, formVmName, genContext.getFormFile());

        createVM(params, formEditVmName, genContext.getEditFormFile());
    }

    public void createVM(Map<String, Object> params, String vm, String filePath) {
        try {
            String render = FreemakerHelper.render(vm, params);
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            if (file.exists()) {
                filePath += ".c";
            }

            file = new File(filePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(render.getBytes("UTF-8"));
            fileOutputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
