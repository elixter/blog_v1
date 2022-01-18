package elixter.blog;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;


@Configuration
@ComponentScan
//@AutoConfigureBefore(DataSourceAutoConfiguration.class)
//@ComponentScan(
//        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
//)
public class AppConfig {
    public AppConfig() {

    }
}
