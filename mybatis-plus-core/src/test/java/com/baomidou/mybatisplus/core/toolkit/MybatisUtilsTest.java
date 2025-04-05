package com.baomidou.mybatisplus.core.toolkit;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.override.MybatisMapperProxy;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionManager;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.HashMap;

/**
 * @author nieqiurong
 */
public class MybatisUtilsTest {

    interface MyMapper {

    }

    @Test
    void testGetSqlSessionFactoryByDefaultSqlSession() {
        var configuration = getMybatisConfiguration();
        var globalConfig = GlobalConfigUtils.getGlobalConfig(configuration);
        globalConfig.setSqlSessionFactory(Mockito.mock(SqlSessionFactory.class));
        GlobalConfigUtils.setGlobalConfig(configuration, globalConfig);
        var sqlSession = new DefaultSqlSession(configuration, Mockito.mock(Executor.class));
        var mybatisMapperProxy = new MybatisMapperProxy<>(sqlSession, MyMapper.class, new HashMap<>());
        SqlSessionFactory sqlSessionFactory = MybatisUtils.getSqlSessionFactory(mybatisMapperProxy);
        Assertions.assertNotNull(sqlSessionFactory);
    }

    @Test
    void testGetSqlSessionFactoryBySqlSessionManager() {
        var sqlSession = SqlSessionManager.newInstance(Mockito.mock(SqlSessionFactory.class));
        var mybatisMapperProxy = new MybatisMapperProxy<>(sqlSession, MyMapper.class, new HashMap<>());
        SqlSessionFactory sqlSessionFactory = MybatisUtils.getSqlSessionFactory(mybatisMapperProxy);
        Assertions.assertNotNull(sqlSessionFactory);
    }

    @Test
    void testGetSqlSessionFactoryBySqlSessionTemplate() {
        var sqlSession = new SqlSessionTemplate(getDefaultSqlSessionFactory());
        var mybatisMapperProxy = new MybatisMapperProxy<>(sqlSession, MyMapper.class, new HashMap<>());
        Assertions.assertNotNull(MybatisUtils.getSqlSessionFactory(mybatisMapperProxy));
    }

    static class MySqlSessionTemplate extends SqlSessionTemplate {

        public MySqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
            super(sqlSessionFactory);
        }
    }

    @Test
    void testGetSqlSessionFactoryByExtendSqlSessionTemplate() {
        var sqlSession = new MySqlSessionTemplate(getDefaultSqlSessionFactory());
        var mybatisMapperProxy = new MybatisMapperProxy<>(sqlSession, MyMapper.class, new HashMap<>());
        Assertions.assertNotNull(MybatisUtils.getSqlSessionFactory(mybatisMapperProxy));
    }

    private SqlSessionFactory getDefaultSqlSessionFactory() {
        return new DefaultSqlSessionFactory(getMybatisConfiguration());
    }

    private MybatisConfiguration getMybatisConfiguration() {
        MybatisConfiguration mybatisConfiguration = new MybatisConfiguration(Mockito.mock(Environment.class));
        mybatisConfiguration.addMapper(MyMapper.class);
        return mybatisConfiguration;
    }

}
