package com.baomidou.mybatisplus.test.chainwrapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

/**
 * @author miemie
 * @since 2020-06-23
 */
public interface EntityMapper extends BaseMapper<Entity> {

    default QueryChainWrapper<Entity> queryChain() {
        return Wrappers.queryChain(this);
    }
}
