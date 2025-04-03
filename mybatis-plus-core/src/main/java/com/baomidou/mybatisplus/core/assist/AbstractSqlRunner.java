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
package com.baomidou.mybatisplus.core.assist;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.apache.ibatis.parsing.GenericTokenParser;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author nieqiurong
 * @since 3.5.12
 */
public abstract class AbstractSqlRunner implements ISqlRunner {

    /**
     * 默认分词处理器
     *
     * @since 3.5.12
     */
    private static final GenericTokenParser DEFAULT_TOKEN_PARSER = new GenericTokenParser("{", "}", content -> "#{" + content + "}");

    /**
     * 校验索引正则
     *
     * @since 3.5.12
     */
    private final Pattern pattern = Pattern.compile("^\\d+$");

    /**
     * 获取执行语句
     *
     * @param sql  原始sql
     * @param args 参数
     * @return 执行语句
     * @since 3.5.12
     */
    protected String parse(String sql, Object... args) {
        if (args != null && args.length == 1) {
            Object arg = args[0];
            Class<?> clazz = arg.getClass();
            return new GenericTokenParser("{", "}", content -> {
                if (pattern.matcher(content).matches()) {
                    if (arg instanceof Collection || clazz.isArray()) {
                        return "#{arg0[" + content + "]}";
                    }
                    return "#{" + content + "}";
                } else {
                    return "#{arg0." + content + "}";
                }
            }).parse(sql);
        }
        return DEFAULT_TOKEN_PARSER.parse(sql);
    }

    /**
     * 获取参数列表
     *
     * @param args 参数(单参数时,支持使用Map,List,Array,JavaBean访问)
     * @return 参数map
     * @since 3.5.12
     */
    protected Map<String, Object> getParams(Object... args) {
        if (args != null && args.length > 0) {
            Map<String, Object> params = CollectionUtils.newHashMapWithExpectedSize(args.length);
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (i == 0) {
                    params.put("arg0", arg);
                }
                params.put(String.valueOf(i), arg);
            }
            return params;
        }
        return new HashMap<>();
    }

}
