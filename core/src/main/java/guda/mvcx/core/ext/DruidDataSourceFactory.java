package guda.mvcx.core.ext;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;

/**
 * Created by well on 2017/3/23.
 */
public class DruidDataSourceFactory extends UnpooledDataSourceFactory {

    public DruidDataSourceFactory() {
        this.dataSource = new DruidDataSource();
    }
}
