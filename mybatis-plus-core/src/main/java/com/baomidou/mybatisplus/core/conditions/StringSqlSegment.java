package com.baomidou.mybatisplus.core.conditions;

import lombok.RequiredArgsConstructor;

/**
 * @author miemie
 * @since 2025/9/27
 */
@RequiredArgsConstructor
public class StringSqlSegment implements ISqlSegment {
    private static final long serialVersionUID = -1L;
    private final String str;

    @Override
    public String getSqlSegment() {
        return str;
    }

    public static StringSqlSegment of(String str) {
        return new StringSqlSegment(str);
    }
}
