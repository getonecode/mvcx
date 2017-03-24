package guda.mvcx.gen;

import guda.mvcx.gen.annotation.GenField;
import guda.mvcx.gen.common.DOField;
import guda.mvcx.gen.common.GenConstants;
import guda.mvcx.gen.helper.CamelCaseUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by well on 2017/3/24.
 */
public class GenContext {

    private String sqlDriver;
    private String sqlUser;
    private String sqlPassword;
    private String sqlUrl;

    private String tableNamePrefix;
    private String tableName;
    private String parentPackageName;

    private String doName;

    private String doNameLower;

    private String bizFile;

    private String bizImplFile;

    private String daoFile;

    private String mybatisXmlFile;

    private String relativeDaoMapperXmlFile;


    private String formFile;

    private String editFormFile;

    private String doFile;
    private String daoInfFile;
    private String daoImplFile;
    private String sqlmapFile;

    private String baseDir;

    private String doClassName;

    private String formXMLFile;
    private String baseVMDir;


    public GenContext(String sqlDriver, String sqlUrl, String sqlUser, String sqlPassword, String baseDir, String parentPackageName) {
        this.sqlDriver = sqlDriver;
        this.sqlUser = sqlUser;
        this.sqlUrl = sqlUrl;
        this.sqlPassword = sqlPassword;
        this.parentPackageName = parentPackageName;
        this.baseDir = baseDir;

    }

    public GenContext(String baseDir, String clazz, String parentPackageName) throws ClassNotFoundException {

        this(baseDir, Class.forName(clazz), parentPackageName);

    }

    public GenContext(String baseDir, Class clazz, String parentPackageName) {
        this.baseDir = baseDir;
        doName = clazz.getSimpleName();
        this.doClassName = clazz.getName();
        this.parentPackageName = parentPackageName;
        this.tableName = CamelCaseUtils.toUnderlineName(doName.substring(0, doName.length() - 2));
        String packageDir = parentPackageName.replaceAll("\\.", File.separator + File.separator);
        doName = doName.substring(0, doName.length() - 2);
        doNameLower = doName.substring(0, 1).toLowerCase() + doName.substring(1);
        bizFile = baseDir + File.separator + GenConstants.javaDir + File.separator + packageDir
                + File.separator + doName + "Biz.java";
        bizImplFile = baseDir + File.separator + GenConstants.javaDir + File.separator + packageDir
                + File.separator + "impl" + File.separator + doName + "BizImpl.java";


        daoFile = baseDir + File.separator + GenConstants.javaDir + File.separator + packageDir
                + File.separator + File.separator + "dao" + File.separator + doName + "DAO";


        mybatisXmlFile = baseDir + File.separator + GenConstants.resourceDir + File.separator + "mybatis" + File.separator + GenConstants.mybatisConfigXML;

        relativeDaoMapperXmlFile = "mapper/" + tableName + ".xml";


        formFile = baseDir + File.separator + GenConstants.javaDir + File.separator + packageDir
                + File.separator + "form" + File.separator + doName + "Form.java";
        editFormFile = baseDir + File.separator + GenConstants.javaDir + File.separator + packageDir
                + File.separator + "form" + File.separator + doName + "EditForm.java";

        doFile = baseDir + File.separator + GenConstants.javaDir + File.separator + packageDir
                + File.separator + "model" + File.separator + doName + "DO.java";
        daoInfFile = baseDir + File.separator + GenConstants.javaDir + File.separator + packageDir
                + File.separator + "dao" + File.separator + doName + "DAO.java";
        daoImplFile = baseDir + File.separator + GenConstants.javaDir + File.separator + packageDir
                + File.separator + "dao" + File.separator + "impl" + File.separator + doName + "DAOImpl.java";
        sqlmapFile = baseDir + File.separator + GenConstants.resourceDir + File.separator + "mapper" + File.separator;


        System.out.println("doNameLower:" + doNameLower);
        System.out.println("bizFile:" + bizFile);
        System.out.println("bizImplFile:" + bizImplFile);

        System.out.println("daoFile:" + daoFile);

        System.out.println("mybatisXmlFile:" + mybatisXmlFile);
        System.out.println("relativeDaoMapperXmlFile:" + relativeDaoMapperXmlFile);

        System.out.println("formFile:" + formFile);
        System.out.println("editFormFile:" + editFormFile);

        Field[] fieldArray = clazz.getDeclaredFields();

        for (Field field : fieldArray) {
            if ("serialVersionUID".equals(field.getName())) {
                continue;
            }
            DOField dofield = new DOField();
            String name = field.getName();
            Class<?> type = field.getType();
            dofield.name = name;
            dofield.type = type;
            dofield.typeName = type.getSimpleName();
            dofield.upperName = name.substring(0, 1).toUpperCase() + name.substring(1);
            GenField annotation = field.getAnnotation(GenField.class);
            if (annotation != null) {
                if (annotation.ignore()) {
                    continue;
                }
                dofield.cnName = annotation.cn();
                dofield.inSearchForm = annotation.inSearchForm();
                dofield.order = annotation.order();
                dofield.canNull = annotation.canNull();
            } else {
                continue;
            }
            doFieldList.add(dofield);

        }
        Collections.sort(doFieldList, new Comparator<DOField>() {
            public int compare(DOField o1, DOField o2) {
                if (o1 == null || o2 == null)
                    return 0;
                if (o1.order > o2.order) {
                    return 1;
                }
                return -1;
            }
        });
        for (DOField doField : doFieldList) {
            System.out.println(doField);
        }
    }


    public String getFormXMLFile() {
        return formXMLFile;
    }

    public void setFormXMLFile(String formXMLFile) {
        this.formXMLFile = formXMLFile;
    }

    public String getFormFile() {
        return formFile;
    }

    public void setFormFile(String formFile) {
        this.formFile = formFile;
    }

    public String getEditFormFile() {
        return editFormFile;
    }

    public void setEditFormFile(String editFormFile) {
        this.editFormFile = editFormFile;
    }


    public List<DOField> getDoFieldList() {
        return doFieldList;
    }

    public void setDoFieldList(List<DOField> doFieldList) {
        this.doFieldList = doFieldList;
    }

    public String getRelativeDaoMapperXmlFile() {
        return relativeDaoMapperXmlFile;
    }

    public void setRelativeDaoMapperXmlFile(String relativeDaoMapperXmlFile) {
        this.relativeDaoMapperXmlFile = relativeDaoMapperXmlFile;
    }


    public String getDoName() {
        return doName;
    }

    public void setDoName(String doName) {
        this.doName = doName;
    }

    public String getDoNameLower() {
        return doNameLower;
    }

    public void setDoNameLower(String doNameLower) {
        this.doNameLower = doNameLower;
    }

    public String getBizFile() {
        return bizFile;
    }

    public void setBizFile(String bizFile) {
        this.bizFile = bizFile;
    }

    public String getBizImplFile() {
        return bizImplFile;
    }

    public void setBizImplFile(String bizImplFile) {
        this.bizImplFile = bizImplFile;
    }


    public String getDaoFile() {
        return daoFile;
    }

    public void setDaoFile(String daoFile) {
        this.daoFile = daoFile;
    }


    public String getMybatisXmlFile() {
        return mybatisXmlFile;
    }

    public void setMybatisXmlFile(String mybatisXmlFile) {
        this.mybatisXmlFile = mybatisXmlFile;
    }


    public String getDoClassName() {
        return doClassName;
    }

    public void setDoClassName(String doClassName) {
        this.doClassName = doClassName;
    }

    public String getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public String getParentPackageName() {
        return parentPackageName;
    }

    public void setParentPackageName(String parentPackageName) {
        this.parentPackageName = parentPackageName;
    }

    public String getDoFile() {
        return doFile;
    }

    public void setDoFile(String doFile) {
        this.doFile = doFile;
    }

    public String getDaoInfFile() {
        return daoInfFile;
    }

    public void setDaoInfFile(String daoInfFile) {
        this.daoInfFile = daoInfFile;
    }

    public String getDaoImplFile() {
        return daoImplFile;
    }

    public void setDaoImplFile(String daoImplFile) {
        this.daoImplFile = daoImplFile;
    }

    public String getSqlmapFile() {
        return sqlmapFile;
    }

    public void setSqlmapFile(String sqlmapFile) {
        this.sqlmapFile = sqlmapFile;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableNamePrefix() {
        return tableNamePrefix;
    }

    public void setTableNamePrefix(String tableNamePrefix) {
        this.tableNamePrefix = tableNamePrefix;
    }

    public String getSqlDriver() {
        return sqlDriver;
    }

    public void setSqlDriver(String sqlDriver) {
        this.sqlDriver = sqlDriver;
    }

    public String getSqlUser() {
        return sqlUser;
    }

    public void setSqlUser(String sqlUser) {
        this.sqlUser = sqlUser;
    }

    public String getSqlPassword() {
        return sqlPassword;
    }

    public void setSqlPassword(String sqlPassword) {
        this.sqlPassword = sqlPassword;
    }

    public String getSqlUrl() {
        return sqlUrl;
    }

    public void setSqlUrl(String sqlUrl) {
        this.sqlUrl = sqlUrl;
    }

    private List<DOField> doFieldList = new ArrayList<DOField>();

    public String getBaseVMDir() {
        return baseVMDir;
    }

    public void setBaseVMDir(String baseVMDir) {
        this.baseVMDir = baseVMDir;
    }
}
