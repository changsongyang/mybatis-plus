package com.baomidou.mybatisplus.extension.conditions.update;

import com.baomidou.mybatisplus.core.conditions.SelfChildren;
import com.baomidou.mybatisplus.extension.conditions.AbstractChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;

/**
 * 具有更新方法的定义
 *
 * @author miemie
 * @since 2018-12-19
 */
public interface ChainUpdate<T, Children extends AbstractChainWrapper<T, ?, ?, ?>> extends SelfChildren<Children> {

    /**
     * 更新数据
     * <p>此方法无法进行自动填充,如需自动填充请使用{@link #update(Object)}</p>
     *
     * @return 是否成功
     */
    default boolean update() {
        return update(null);
    }

    /**
     * 更新数据
     *
     * @param entity 实体类(当entity为空时无法进行自动填充)
     * @return 是否成功
     */
    default boolean update(T entity) {
        return selfOrChildren().execute(mapper -> SqlHelper.retBool(mapper.update(entity, selfOrChildren().delegate())));
    }

    /**
     * 删除数据
     *
     * @return 是否成功
     */
    default boolean remove() {
        return selfOrChildren().execute(mapper -> SqlHelper.retBool(mapper.delete(selfOrChildren().delegate())));
    }
}
