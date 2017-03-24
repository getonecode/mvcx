package guda.mvcx.demo.test.gen;

import guda.mvcx.gen.GenContext;
import guda.mvcx.gen.GenDAO;

import java.io.File;

/**
 * Created by well on 2017/3/24.
 */
public class GenDAOTest {


    private static String jdbUrl = "jdbc:mysql://127.0.0.1:3306/demo?useUnicode=true&characterEncoding=utf8";

    public static void main(String[] args){
        String baseDir = System.getProperties().get("user.dir") + File.separator + "demo";
        GenContext genContext = new GenContext("com.mysql.jdbc.Driver", jdbUrl, "demo_user", "demo_pwd", baseDir, "guda.mvcx.demo");

        System.out.println(baseDir);
        GenDAO genDAO = new GenDAO(genContext);
        genDAO.genBatch("b_abc_desc", "b_");
    }
}
