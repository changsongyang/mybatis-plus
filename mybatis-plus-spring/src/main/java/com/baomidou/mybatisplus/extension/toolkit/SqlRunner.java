/*
 * Copyright (c) 2011-2025, baomidou (jobob@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baomidou.mybatisplus.extension.toolkit;

import com.baomidou.mybatisplus.core.assist.ISqlRunner;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.parsing.GenericTokenParser;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.SimpleTypeRegistry;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * SqlRunner 执行 SQL
 * <p>
 * 自3.5.12开始,(当传入的参数是单参数时,支持使用Map,Array,List,JavaBean)
 * <li>当参数为 Map 时可通过{key}进行属性访问
 * <li>当参数为 JavaBean 时可通过{property}进行属性访问
 * <li>当参数为 List 时直接访问索引 {0} </li>
 * </p>
 *
 * @author Caratacus, nieqiurong
 * @since 2016-12-11
 */
public class SqlRunner implements ISqlRunner {

    private static final Log LOG = LogFactory.getLog(SqlRunner.class);

    // 单例Query
    public static final SqlRunner DEFAULT = new SqlRunner();

    /**
     * 实体类 (当未指定时,将使用{@link SqlHelper#FACTORY}进行会话操作)
     */
    private Class<?> clazz;

    public SqlRunner() {
    }

    public SqlRunner(Class<?> clazz) {
        this.clazz = clazz;
    }

    /**
     * 获取默认的SqlQuery(适用于单库)
     *
     * @return this
     */
    public static SqlRunner db() {
        return DEFAULT;
    }

    /**
     * 根据当前class对象获取SqlQuery(适用于多库)
     *
     * @param clazz 实体类
     * @return this
     */
    public static SqlRunner db(Class<?> clazz) {
        return new SqlRunner(clazz);
    }

    /**
     * 执行插入语句
     *
     * @param sql  指定参数的格式: {0}, {1} 或者 {property1}, {property2}
     * @param args 参数
     * @return 插入结果
     */
    @Override
    @Transactional
    public boolean insert(String sql, Object... args) {
        SqlSession sqlSession = sqlSession();
        try {
            return SqlHelper.retBool(sqlSession.insert(INSERT, sqlMap(sql, args)));
        } finally {
            closeSqlSession(sqlSession);
        }
    }

    /**
     * 执行删除语句
     *
     * @param sql  指定参数的格式: {0}, {1} 或者 {property1}, {property2}
     * @param args 参数
     * @return 删除结果
     */
    @Override
    @Transactional
    public boolean delete(String sql, Object... args) {
        SqlSession sqlSession = sqlSession();
        try {
            return SqlHelper.retBool(sqlSession.delete(DELETE, sqlMap(sql, args)));
        } finally {
            closeSqlSession(sqlSession);
        }
    }

    /**
     * 获取sqlMap参数
     * <p>
     * 自3.5.12开始,(当传入的参数是单参数时,支持使用Map,Array,List,JavaBean)
     * <li>当参数为 Map 时可通过{key}进行属性访问
     * <li>当参数为 JavaBean 时可通过{property}进行属性访问
     * <li>当参数为 List 时直接访问索引 {0} </li>
     * </p>
     *
     * @param sql  指定参数的格式: {0}, {1} 或者 {property1}, {property2}
     * @param args 参数
     * @return 参数集合
     */
    private Map<String, Object> sqlMap(String sql, Object... args) {
        Map<String, Object> sqlMap = getParams(args);
        sqlMap.put(SQL, parse(sql));
        return sqlMap;
    }

    /**
     * 获取执行语句
     *
     * @param sql 原始sql
     * @return 执行语句
     * @since 3.5.12
     */
    private String parse(String sql) {
        return new GenericTokenParser("{", "}", content -> "#{" + content + "}").parse(sql);
    }

    /**
     * 获取参数列表
     *
     * @param args 参数(单参数时,支持使用Map,List,JavaBean访问)
     * @return 参数map
     * @since 3.5.12
     */
    private Map<String, Object> getParams(Object... args) {
        if (args != null && args.length > 0) {
            if (args.length == 1) {
                // 暂定支持 Map,Collection,JavaBean
                Object arg = args[0];
                if (arg instanceof Map) {
                    //noinspection unchecked
                    return new HashMap<String, Object>((Map) arg);
                }
                if (arg instanceof Collection) {
                    Collection<?> collection = (Collection<?>) arg;
                    Map<String, Object> params = new HashMap<>(CollectionUtils.newHashMapWithExpectedSize(collection.size()));
                    Iterator<?> iterator = collection.iterator();
                    int index = 0;
                    while (iterator.hasNext()) {
                        params.put(String.valueOf(index), iterator.next());
                        index++;
                    }
                    return params;
                }
                Class<?> cls = arg.getClass();
                if (!(cls.isPrimitive()
                    || SimpleTypeRegistry.isSimpleType(cls)
                    || cls.isArray() || cls.isEnum())
                ) {
                    MetaObject metaObject = SystemMetaObject.forObject(arg);
                    String[] getterNames = metaObject.getGetterNames();
                    Map<String, Object> params = new HashMap<>(CollectionUtils.newHashMapWithExpectedSize(getterNames.length));
                    for (String getterName : getterNames) {
                        params.put(getterName, metaObject.getValue(getterName));
                    }
                    return params;
                }
            }
            Map<String, Object> params = CollectionUtils.newHashMapWithExpectedSize(args.length);
            for (int i = 0; i < args.length; i++) {
                params.put(String.valueOf(i), args[i]);
            }
            return params;
        }
        return new HashMap<>();
    }

    /**
     * 获取sqlMap参数
     *
     * @param sql  指定参数的格式: {0}, {1} 或者 {property1}, {property2}
     * @param page 分页模型
     * @param args 参数
     * @return 参数集合
     */
    private Map<String, Object> sqlMap(String sql, IPage<?> page, Object... args) {
        Map<String, Object> sqlMap = getParams(args);
        sqlMap.put(PAGE, page);
        sqlMap.put(SQL, parse(sql));
        return sqlMap;
    }

    /**
     * 执行更新语句
     *
     * @param sql  指定参数的格式: {0}, {1} 或者 {property1}, {property2}
     * @param args 参数
     * @return 更新结果
     */
    @Override
    @Transactional
    public boolean update(String sql, Object... args) {
        SqlSession sqlSession = sqlSession();
        try {
            return SqlHelper.retBool(sqlSession.update(UPDATE, sqlMap(sql, args)));
        } finally {
            closeSqlSession(sqlSession);
        }
    }

    /**
     * 根据sql查询Map结果集
     * <p>SqlRunner.db().selectList("select * from tbl_user where name={0}", "Caratacus")</p>
     *
     * @param sql  sql语句，可添加参数，指定参数的格式: {0}, {1} 或者 {property1}, {property2}
     * @param args 参数列表
     * @return 结果集
     */
    @Override
    public List<Map<String, Object>> selectList(String sql, Object... args) {
        SqlSession sqlSession = sqlSession();
        try {
            return sqlSession.selectList(SELECT_LIST, sqlMap(sql, args));
        } finally {
            closeSqlSession(sqlSession);
        }
    }

    /**
     * 根据sql查询一个字段值的结果集
     * <p>注意：该方法只会返回一个字段的值， 如果需要多字段，请参考{@link #selectList(String, Object...)}</p>
     *
     * @param sql  sql语句，可添加参数，指定参数的格式: {0}, {1} 或者 {property1}, {property2}
     * @param args 参数
     * @return 结果集
     */
    @Override
    public List<Object> selectObjs(String sql, Object... args) {
        SqlSession sqlSession = sqlSession();
        try {
            return sqlSession.selectList(SELECT_OBJS, sqlMap(sql, args));
        } finally {
            closeSqlSession(sqlSession);
        }
    }

    /**
     * 根据sql查询一个字段值的一条结果
     * <p>注意：该方法只会返回一个字段的值， 如果需要多字段，请参考{@link #selectOne(String, Object...)}</p>
     *
     * @param sql  sql语句，可添加参数，指定参数的格式: {0}, {1} 或者 {property1}, {property2}
     * @param args 参数
     * @return 结果
     */
    @Override
    public Object selectObj(String sql, Object... args) {
        return SqlHelper.getObject(LOG, selectObjs(sql, args));
    }

    /**
     * 查询总数
     *
     * @param sql  sql语句，可添加参数，指定参数的格式: {0}, {1} 或者 {property1}, {property2}
     * @param args 参数
     * @return 总记录数
     */
    @Override
    public long selectCount(String sql, Object... args) {
        SqlSession sqlSession = sqlSession();
        try {
            return SqlHelper.retCount(sqlSession.<Long>selectOne(COUNT, sqlMap(sql, args)));
        } finally {
            closeSqlSession(sqlSession);
        }
    }

    /**
     * 获取单条记录
     *
     * @param sql  sql语句，可添加参数，指定参数的格式: {0}, {1} 或者 {property1}, {property2}
     * @param args 参数
     * @return 单行结果集 (当执行语句返回多条记录时,只会选取第一条记录)
     */
    @Override
    public Map<String, Object> selectOne(String sql, Object... args) {
        return SqlHelper.getObject(LOG, selectList(sql, args));
    }

    /**
     * 分页查询
     *
     * @param page 分页对象
     * @param sql  sql语句，可添加参数，指定参数的格式: {0}, {1} 或者 {property1}, {property2}
     * @param args 参数
     * @param <E>  E
     * @return 分页数据
     */
    @Override
    public <E extends IPage<Map<String, Object>>> E selectPage(E page, String sql, Object... args) {
        if (null == page) {
            return null;
        }
        SqlSession sqlSession = sqlSession();
        try {
            page.setRecords(sqlSession.selectList(SELECT_LIST, sqlMap(sql, page, args)));
        } finally {
            closeSqlSession(sqlSession);
        }
        return page;
    }

    /**
     * 获取Session 默认自动提交
     */
    private SqlSession sqlSession() {
        return SqlSessionUtils.getSqlSession(getSqlSessionFactory());
    }

    /**
     * 释放sqlSession
     *
     * @param sqlSession session
     */
    private void closeSqlSession(SqlSession sqlSession) {
        SqlSessionUtils.closeSqlSession(sqlSession, getSqlSessionFactory());
    }

    /**
     * 获取SqlSessionFactory
     */
    private SqlSessionFactory getSqlSessionFactory() {
        return Optional.ofNullable(clazz).map(GlobalConfigUtils::currentSessionFactory).orElse(SqlHelper.FACTORY);
    }

    /**
     * @deprecated 3.5.3.2
     */
    @Deprecated
    public void close() {

    }

}
