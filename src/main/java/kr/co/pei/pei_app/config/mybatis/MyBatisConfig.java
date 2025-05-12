package kr.co.pei.pei_app.config.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
//@MapperScan("kr.co.pei.pei_app.domain.repository.schedule.mybatis")
@MapperScan(basePackages = "kr.co.pei.pei_app.domain.repository.schedule.mybatis")
public class MyBatisConfig {
}
