package com.baomidou.mybatisplus.core.conditions;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * @author miemie
 * @since 2025/9/25
 */
@NoArgsConstructor
public class WrapperNestedContext<T> {

    /**
     * 实体类型(主要用于确定泛型以及取TableInfo缓存)
     */
    @Setter
    protected Class<T> entityClass;
    /**
     * 数据库表映射实体类
     */
    @Setter
    @Getter
    protected T entity;
    protected String paramAlias = Constants.WRAPPER;
    protected AtomicInteger paramNameSeq = new AtomicInteger(0);
    @Getter
    protected Map<String, Object> paramNameValuePairs = new HashMap<>(16);
    /**
     * string类型的 column 检查器, 用于防止 SQL 注入, 检查不通过需要自己抛出异常
     */
    @Setter
    protected Consumer<String> stringColumnChecker = s -> {
    };
    /**
     * SFunction<T> 类型的column 是否自动使用 mapping
     */
    @Setter
    protected boolean autoSFuncMapping = false;
    /**
     * 缓存相关
     */
    private Map<String, ColumnCache> columnLambdaCacheMap = null;

    @SuppressWarnings("unused")
    public WrapperNestedContext(String paramAlias) {
        this.paramAlias = paramAlias;
    }

    @SuppressWarnings("unchecked")
    public Class<T> getEntityClass() {
        if (entityClass == null && entity != null) {
            entityClass = (Class<T>) entity.getClass();
        }
        return entityClass;
    }

    public String putParam(Object param) {
        final String genParamName = Constants.WRAPPER_PARAM + paramNameSeq.incrementAndGet();
        final String paramStr = paramAlias + Constants.WRAPPER_PARAM_MIDDLE + genParamName;
        paramNameValuePairs.put(genParamName, param);
        return paramStr;
    }

    /**
     * 获取 SerializedLambda 对应的列信息，从 lambda 表达式中推测实体类
     * <p>
     * 如果获取不到列信息，那么本次条件组装将会失败
     *
     * @return 列
     * @throws com.baomidou.mybatisplus.core.exceptions.MybatisPlusException 获取不到列信息时抛出异常
     */
    public ColumnCache getColumnCache(SFunction<T, ?> column) {
        LambdaMeta meta = LambdaUtils.extract(column);
        String fieldName = PropertyNamer.methodToProperty(meta.getImplMethodName());
        Class<?> instantiatedClass = meta.getInstantiatedClass();
        tryInitCache(instantiatedClass);
        return getColumnCache(fieldName, instantiatedClass);
    }

    public ColumnCache getColumnCache(String fieldName) {
        tryInitCache(entityClass);
        return getColumnCache(fieldName, entityClass);
    }

    public void tryInitCache(Class<?> lambdaClass) {
        if (Objects.isNull(columnLambdaCacheMap)) {
            final Class<T> entityClass = getEntityClass();
            if (entityClass != null) {
                lambdaClass = entityClass;
            }
            columnLambdaCacheMap = LambdaUtils.getColumnMap(lambdaClass);
            Assert.notNull(columnLambdaCacheMap, "can not find lambda cache for this entity [%s]", lambdaClass.getName());
        }
    }

    private ColumnCache getColumnCache(String fieldName, Class<?> entityClass) {
        ColumnCache columnCache = columnLambdaCacheMap.get(LambdaUtils.formatKey(fieldName));
        Assert.notNull(columnCache, "can not find lambda cache for this property [%s] of entity [%s]",
            fieldName, entityClass.getName());
        return columnCache;
    }

    public void checkStringColumn(String column) {
        stringColumnChecker.accept(column);
    }

    public void clear() {
        this.entity = null;
        this.paramNameSeq.set(0);
        this.paramNameValuePairs.clear();
    }
}
