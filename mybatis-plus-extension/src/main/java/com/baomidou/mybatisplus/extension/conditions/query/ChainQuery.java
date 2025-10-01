package com.baomidou.mybatisplus.extension.conditions.query;

import com.baomidou.mybatisplus.core.conditions.SelfChildren;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.AbstractChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;

import java.util.List;
import java.util.Optional;

/**
 * 具有查询方法的定义
 *
 * @author miemie
 * @since 2018-12-19
 */
public interface ChainQuery<T, Children extends AbstractChainWrapper<T, ?, ?, ?>> extends SelfChildren<Children> {

    /**
     * 获取集合
     *
     * @return 集合
     */
    default List<T> list() {
        return selfOrChildren().execute(mapper -> mapper.selectList(selfOrChildren().delegate()));
    }

    /**
     * 获取集合
     *
     * @param page 分页条件
     * @return 集合记录
     * @since 3.5.3.2
     */
    default List<T> list(IPage<T> page) {
        return selfOrChildren().execute(mapper -> mapper.selectList(page, selfOrChildren().delegate()));
    }

    /**
     * 获取单个
     *
     * @return 单个
     */
    default T one() {
        return selfOrChildren().execute(mapper -> mapper.selectOne(selfOrChildren().delegate()));
    }

    /**
     * 获取单个
     *
     * @return 单个
     * @since 3.3.0
     */
    default Optional<T> oneOpt() {
        return Optional.ofNullable(one());
    }

    /**
     * 获取 count
     *
     * @return count
     */
    default Long count() {
        return selfOrChildren().execute(mapper -> SqlHelper.retCount(mapper.selectCount(selfOrChildren().delegate())));
    }

    /**
     * 判断数据是否存在
     *
     * @return true 存在 false 不存在
     */
    default boolean exists() {
        return this.count() > 0;
    }

    /**
     * 获取分页数据
     *
     * @param page 分页条件
     * @return 分页数据
     */
    default <E extends IPage<T>> E page(E page) {
        return selfOrChildren().execute(mapper -> mapper.selectPage(page, selfOrChildren().delegate()));
    }
}
