package guda.mvcx.gen;



import guda.mvcx.gen.common.GenConstants;
import guda.mvcx.gen.helper.FreemakerHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by foodoon on 15/8/6.
 */
public class GenDAO {

    public static final String COMMON_COLUMN_STR = "gmt_create,gmt_update,";

    // jdbc result set metadata collumn name
    public static final String RSMD_COLUMN_NAME = "rsmdColumnName";
    public static final String RSMD_COLUMN_CLASS_NAME = "columnClassName";
    public static final String RSMD_COLUMN_TYPE_NAME = "columnTypeName";
    public static final String RSMD_COLUMN_PRECISION = "Precision";
    public static final String RSMD_COLUMN_SCALE = "Scale";

    // velocity param
    public static final String VP_LIST = "list";
    public static final String VP_CLASS_NAME = "className";
    public static final String VP_MAIN_PACKAGE = "mainPackage";
    public static final String VP_DO_PACKAGE = "doPackage";
    public static final String VP_DAO_INTF_PACKAGE = "daoIntfPackage";
    public static final String VP_DAO_IMPL_PACKAGE = "daoImplPackage";
    public static final String VP_JAVA_TYPE = "javaType";
    public static final String VP_PROP_NAME = "propName";
    public static final String VP_GET_METHOD = "getMethod";
    public static final String VP_SET_METHOD = "setMethod";

    public static final String VP_COLUMN_NAME = "columnName";
    public static final String VP_TABLE_NAME = "tableName";
    public static final String VP_JDBC_TYPE = "jdbcType";
    public static final String VP_COLS = "cols";
    public static final String VP_COL_ID = "colId";
    public static final String VP_COLS_WITHOUT_COMMON_COLUMNS = "colsWithoutCommColumns";
    public static final String VP_SERIAL_VERSION_UID = "serialVersionUID";
    public static final String VP_SERIAL_VERSION_UID2 = "serialVersionUID2";

    //分库分表 表后缀regex
    public static final String SHARDING_SUFIX_REG = "_[\\d]{4}";


    private String doVmName = "do.ftl";

    private String daoVmName = "dao.ftl";
    private String sqlmapVmName = "sqlmap.ftl";

    private GenContext genContext;

    public GenDAO(GenContext genContext){
        this.genContext = genContext;
    }

    private Connection getConnection() {
        try {
            Class.forName(genContext.getSqlDriver());
            String url = genContext.getSqlUrl();
            String user = genContext.getSqlUser();
            String psw = genContext.getSqlPassword();
            return DriverManager.getConnection(url, user, psw);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }


    private String cutPrefix(String str,String prefix){
        if(str == null || prefix == null || prefix.trim().length() ==0){
            return str;
        }
        if(str.startsWith(prefix)){
            return str.substring(prefix.length());
        }
        return str;
    }

    public void genBatch(String tableName,String tablePrefix)  {
        if(tableName == null){
            return;
        }
        String[] split = tableName.split(",");
        for(String tab:split){
            gen(tab,tablePrefix);
        }
    }

    public void gen(String tableName,String tablePrefix)  {
        String parentPackageName  = genContext.getParentPackageName();
        genContext.setTableName(cutPrefix(tableName,tablePrefix));
        genContext.setTableNamePrefix(tablePrefix);

        genContext.setDoName(getClassName(genContext.getTableName()));
        genContext.setDoNameLower(genContext.getDoName().substring(0, 1).toLowerCase() + genContext.getDoName().substring(1));
        String packageDir = parentPackageName.replaceAll("\\.", File.separator + File.separator);
        String doFile = genContext.getBaseDir() + File.separator + GenConstants.javaDir + File.separator + packageDir
                + File.separator + "dao" + File.separator+ "model" + File.separator + genContext.getDoName() + "DO.java";
        String daoInfFile = genContext.getBaseDir() + File.separator + GenConstants.javaDir + File.separator + packageDir
                + File.separator + "dao" + File.separator + genContext.getDoName() + "DAO.java";
        String daoImplFile = genContext.getBaseDir() + File.separator + GenConstants.javaDir + File.separator + packageDir
                + File.separator + "dao" + File.separator + "impl" + File.separator + genContext.getDoName() + "DAOImpl.java";
        String sqlmapFile = genContext.getBaseDir() + File.separator + GenConstants.resourceDir + File.separator + "mapper" + File.separator+genContext.getTableName()+".xml";

        genContext.setDoFile(doFile);
        genContext.setDaoInfFile(daoInfFile);
        genContext.setDaoImplFile(daoImplFile);
        genContext.setSqlmapFile(sqlmapFile);

        Map<String,Object> params = new HashMap<String,Object>();
        params.put("parentPackageName",parentPackageName);
        params.put("doName",genContext.getDoName());

        params.put("doNameLower",genContext.getDoNameLower());
        params.put("tableNamePrefix",tablePrefix);

        params.put(VP_MAIN_PACKAGE,parentPackageName);
        params.put(VP_DO_PACKAGE, parentPackageName+".dao.model");
        params.put(VP_DAO_INTF_PACKAGE, parentPackageName+".dao");
        params.put(VP_DAO_IMPL_PACKAGE, parentPackageName+".dao.impl");
        params.put(VP_CLASS_NAME, getClassName(genContext.getTableName()));

        List<Map<String, String>> colInfoList = getColInfoList(genContext.getTableNamePrefix()+genContext.getTableName());
        List<Map<String, String>> paramList = makeParamList(colInfoList);

        params.put(VP_LIST, paramList);

        try{
            String render = FreemakerHelper.render(doVmName, params);
            String fileName = genContext.getDoFile();
            File file = new File(fileName);
            file.getParentFile().mkdirs();
            if (file.exists()) {
                fileName += ".c";
            }
            file = new File(fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(render.getBytes("UTF-8"));
            fileOutputStream.close();
        }catch(Exception e){
            throw new RuntimeException(e);
        }


        String vpTableName = genContext.getTableName().toLowerCase();
        boolean isSharding = Pattern.compile(SHARDING_SUFIX_REG).matcher(genContext.getTableName()).find();
        if (isSharding) {
            vpTableName += "_$tabNum$";
        }

        params.put(VP_TABLE_NAME, vpTableName);
        params.put(VP_SERIAL_VERSION_UID, "" + (long) (Math.random() * 1000000000000000000L));

        params.put(VP_SERIAL_VERSION_UID2, "" + (long) (Math.random() * 1000000000000000000L));


        List<Map<String, String>> sqlmapParamList = getSqlmapParamList(paramList);
        // 获取字段名不包含 id gmt_create gmt_modified version
        params.put(VP_LIST, sqlmapParamList);

        params.put(VP_COL_ID,getColId(sqlmapParamList));

        String colsWithoutCommColumns = getColsStr(sqlmapParamList);
        params.put(VP_COLS_WITHOUT_COMMON_COLUMNS, colsWithoutCommColumns);
        String cols = COMMON_COLUMN_STR + colsWithoutCommColumns;
        params.put(VP_COLS, cols);

        try {
            String render = FreemakerHelper.render(daoVmName, params);
            String fileName = genContext.getDaoInfFile();
            File file = new File(fileName);
            file.getParentFile().mkdirs();
            if (file.exists()) {
                fileName += ".c";
            }
            file = new File(fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(render.getBytes("UTF-8"));
            fileOutputStream.close();



            render = FreemakerHelper.render(sqlmapVmName, params);
            fileName = genContext.getSqlmapFile();
            file = new File(fileName);
            file.getParentFile().mkdirs();
            if (file.exists()) {
                fileName += ".c";
            }
            file = new File(fileName);
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(render.getBytes("UTF-8"));
            fileOutputStream.close();
        }catch(Exception e){
            throw new RuntimeException(e);
        }

    }



    public static String getClassName(String tableName) {
        String t = tableName.toLowerCase();
        String[] arr = t.split("_");
        int num = arr.length;
        String s = "";
        for (int i = 0; i < num; i++) {
            s = s + makeFisrtCharUpperCase(arr[i]);
        }
        return s;
    }


    public static List<Map<String, String>> getColInfoList(Connection cn, String table) {
        String sql = "select * from " + table + " where 1>2";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = cn.createStatement();
            rs = stmt.executeQuery(sql);
            // 获取结果集元数据信息
            ResultSetMetaData rsmd = rs.getMetaData();
            int num = rsmd.getColumnCount();
            Map<String, String> map = null;
            List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            for (int i = 1; i <= num; i++) {
                map = new HashMap<String, String>();
                map.put(RSMD_COLUMN_NAME, rsmd.getColumnName(i));
                map.put(RSMD_COLUMN_CLASS_NAME, rsmd.getColumnClassName(i));
                map.put(RSMD_COLUMN_TYPE_NAME, rsmd.getColumnTypeName(i));
                map.put(RSMD_COLUMN_PRECISION, rsmd.getPrecision(i) + "");
                map.put(RSMD_COLUMN_SCALE, rsmd.getScale(i) + "");
                list.add(map);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e + ",table=" + table, e);
        } finally {
            try {
                stmt.close();
            } catch (Exception e2) {
            }
            try {
                rs.close();
            } catch (Exception e2) {
            }
        }
    }

    public List<Map<String, String>> getColInfoList(String table)  {
        Connection cn = getConnection();
        try {
            return getColInfoList(cn, table);
        } finally {
            try {
                cn.close();
            } catch (Exception e2) {
            }
        }
    }

    /**
     * 获取参数列表
     *
     * @param colInfoList
     * @return
     * @throws Exception
     */
    public List<Map<String, String>> makeParamList(List<Map<String, String>> colInfoList)  {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> map = null;
        int num = colInfoList.size();
        Map<String, String> mapNew = null;
        for (int i = 0; i < num; i++) {
            map = colInfoList.get(i);
            mapNew = new HashMap<String, String>();
            String columnName = (String) map.get(RSMD_COLUMN_NAME);
            String columnClassName = (String) map.get(RSMD_COLUMN_CLASS_NAME);
            String columnTypeName = (String) map.get(RSMD_COLUMN_TYPE_NAME);
            String scaleStr = (String) map.get(RSMD_COLUMN_SCALE);
            int scale = NumberUtils.toInt(scaleStr);
            String javaType = getJavaType(columnClassName, columnTypeName, scale);
            String jdbcType = getJdbcType(columnClassName, columnTypeName);
            String propName = getPropName(columnName);
            String setMethod = getSetMethod(propName);
            String getMethod = getGetMethod(propName);
            mapNew.put(VP_COLUMN_NAME, columnName.toLowerCase());
            mapNew.put(VP_PROP_NAME, propName);
            mapNew.put(VP_JAVA_TYPE, javaType);
            mapNew.put(VP_JDBC_TYPE, jdbcType);
            mapNew.put(VP_GET_METHOD, getMethod);
            mapNew.put(VP_SET_METHOD, setMethod);
            list.add(mapNew);
        }
        return list;
    }


    public String getJavaType(String columnClassName, String columnTypeName, int scale) {
        //System.out.println(columnClassName);
        if (columnClassName.equals("java.sql.Timestamp")) {
            return "Date";
        }
        if (columnClassName.equals("java.lang.String")) {
            return "String";
        }
        if (columnTypeName.equals("DECIMAL") && scale < 1) {
            return "Integer";
        }
        if (columnTypeName.equals("DECIMAL") && scale > 0) {
            return "Double";
        }
        if (columnTypeName.startsWith("BIGINT")) {
            return "Long";
        }
        if (columnTypeName.startsWith("INT")) {
            return "Integer";
        }
        if (columnTypeName.startsWith("TINYINT")) {
            return "Integer";
        }
        if (columnTypeName.startsWith("SMALLINT")) {
            return "Integer";
        }
        return columnClassName;
    }


    public String getJdbcType(String columnClassName, String columnTypeName) {
        if (columnClassName.equals("java.lang.String")) {
            return "VARCHAR";
        }
        if (columnClassName.startsWith("java.sql.")) {
            return "TIMESTAMP";
        }
        if (columnTypeName.startsWith("NUMBER")) {
            return "DECIMAL";
        }
        return columnTypeName;
    }

    public String getPropName(String columnName) {
        String t = columnName.toLowerCase();
        String[] arr = t.split("_");
        int num = arr.length;
        String s = "";
        for (int i = 0; i < num; i++) {
            if (i > 0) {
                s = s + makeFisrtCharUpperCase(arr[i]);
            } else {
                s = s + arr[i];
            }
        }
        return s;
    }

    public String getSetMethod(String propName) {
        return "set" + makeFisrtCharUpperCase(propName);
    }

    public String getGetMethod(String propName) {
        return "get" + makeFisrtCharUpperCase(propName);
    }


    public static String makeFisrtCharUpperCase(String str) {
        if (StringUtils.isBlank(str)) {
            throw new RuntimeException("str is blank");
        }
        String firstCharStr = str.charAt(0) + "";
        return firstCharStr.toUpperCase() + str.substring(1);
    }

    public static String getColId(List<Map<String, String>> list) {
        String s = "";
        int num = list.size();
        Map<String, String> map = null;
        String colName = null;
        for (int i = 0; i < num; i++) {
            map = list.get(i);
            colName = (String) map.get(VP_PROP_NAME);
            if( colName.indexOf("Id")>-1 ){
                return colName;
            }
        }
        return s;
    }

    public static String getColsStr(List<Map<String, String>> list) {
        String s = "";
        int num = list.size();
        Map<String, String> map = null;
        String colName = null;
        for (int i = 1; i < num; i++) {
            map = list.get(i);
            colName = (String) map.get(VP_COLUMN_NAME);
            if (i > 1) {
                s = s + ",";
            }
            s = s + colName;
        }
        return s;
    }

    public static List<Map<String, String>> getSqlmapParamList(List<Map<String, String>> paramList) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> tmp = null;
        Map<String, String> map = null;
        int num = paramList.size();
        for (int i = 0; i < num; i++) {
            tmp = paramList.get(i);
            String columnName = (String) tmp.get(VP_COLUMN_NAME);
            if (columnName.equalsIgnoreCase("ID")) {
                continue;
            }
            if (columnName.equalsIgnoreCase("GMT_CREATE")) {
                continue;
            }
            if (columnName.equalsIgnoreCase("GMT_UPDATE")) {
                continue;
            }
            if (columnName.equalsIgnoreCase("VERSION")) {
                continue;
            }
            map = new HashMap<String, String>();
            map.putAll(tmp);
            list.add(map);
        }
        return list;
    }

}
