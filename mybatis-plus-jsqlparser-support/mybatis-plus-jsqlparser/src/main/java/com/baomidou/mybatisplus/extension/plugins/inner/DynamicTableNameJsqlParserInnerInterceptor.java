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
package com.baomidou.mybatisplus.extension.plugins.inner;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.extension.DynamicTableNameHandler;
import com.baomidou.mybatisplus.extension.parser.JsqlParserGlobal;
import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import lombok.Setter;
import net.sf.jsqlparser.statement.Statement;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

/**
 * 动态表处理器 (基于JsqlParser解析器)
 * <p>默认情况下,如果JsqlParser解析不了,则调用父类的解析进行处理</p>
 * <p>默认情况下,如果无法处理此sql语句将忽略异常打印日志继续执行</p>
 *
 * @author nieqiurong
 * @see DynamicTableNameHandler
 * @since 3.5.11
 */
@Setter
public class DynamicTableNameJsqlParserInnerInterceptor extends DynamicTableNameInnerInterceptor {

    /**
     * 日志实例
     */
    private static final Log LOG = LogFactory.getLog(DynamicTableNameJsqlParserInnerInterceptor.class);

    /**
     * 是否忽略解析异常
     */
    private boolean ignoreException = false;

    /**
     * 当JsqlParser无法解析语句时是否进行调用父类继续解析处理
     *
     * @see com.baomidou.mybatisplus.core.toolkit.TableNameParser
     */
    private boolean shouldFallback = true;

    /**
     * 是否打印解析错误日志
     */
    private boolean printlnErrorLog = true;

    @Deprecated
    public DynamicTableNameJsqlParserInnerInterceptor() {
    }

    public DynamicTableNameJsqlParserInnerInterceptor(TableNameHandler tableNameHandler) {
        super(tableNameHandler);
    }

    @Override
    protected String processTableName(String sql) {
        try {
            Statement statement = JsqlParserGlobal.parse(sql);
            statement.accept(new DynamicTableNameHandler(sql, super.getTableNameHandler()));
            return statement.toString();
        } catch (Exception exception) {
            printlnErrorLog("Ignoring table name processing exception: " + exception.getMessage());
            return handleFallback(sql, exception);
        }
    }

    protected void printlnErrorLog(String log) {
        if (printlnErrorLog) {
            LOG.error(log);
        }
    }

    private String handleFallback(String sql, Exception originalException) {
        if (shouldFallback) {
            try {
                return super.processTableName(sql);
            } catch (Exception e) {
                printlnErrorLog("Fallback to parent process failed, ignoring exception : " + e.getMessage());
            }
        }
        if (ignoreException) {
            return sql;
        }
        throw new MybatisPlusException("Table name processing failed and fallback not allowed", originalException);
    }

}
