package com.baomidou.mybatisplus.test.extension.plugins.inner;

import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameJsqlParserInnerInterceptor;
import com.baomidou.mybatisplus.extension.toolkit.SqlParserUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author nieqiurong
 */
class DynamicTableNameJsqlParserInnerInterceptorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicTableNameJsqlParserInnerInterceptor.class);

    private static final DynamicTableNameJsqlParserInnerInterceptor interceptor;

    static {
        interceptor = new DynamicTableNameJsqlParserInnerInterceptor((sql, tableName) -> {
            LOGGER.info("process table : {}", tableName);
            if (tableName.endsWith("`") || tableName.endsWith("]")) {
                char first = tableName.charAt(0);
                char last = tableName.charAt(tableName.length()-1);
                return first + SqlParserUtils.removeWrapperSymbol(tableName) + "_r" + last;
            }
            return tableName + "_r";
        });
        interceptor.setShouldFallback(true);
    }

    @Test
    @SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection"})
    void test() {
        // 表名相互包含
        String origin = "SELECT * FROM t_user, t_user_role";
        assertEquals("SELECT * FROM t_user_r, t_user_role_r", interceptor.changeTable(origin));

        // 表名在末尾
        origin = "SELECT * FROM t_user";
        assertEquals("SELECT * FROM t_user_r", interceptor.changeTable(origin));

        // 表名前后有注释
        origin = "SELECT * FROM /**/t_user/* t_user */";
        assertEquals("SELECT * FROM t_user_r", interceptor.changeTable(origin));

        // 值中带有表名
        origin = "SELECT * FROM t_user WHERE name = 't_user'";
        assertEquals("SELECT * FROM t_user_r WHERE name = 't_user'", interceptor.changeTable(origin));

        // 别名被声明要替换
        origin = "SELECT t_user.* FROM t_user_real t_user";
        assertEquals("SELECT t_user.* FROM t_user_real_r t_user", interceptor.changeTable(origin));

        // 别名被声明要替换
        origin = "SELECT t.* FROM t_user_real t left join entity e on e.id = t.id";
        assertEquals("SELECT t.* FROM t_user_real_r t LEFT JOIN entity_r e ON e.id = t.id", interceptor.changeTable(origin));
    }

    @Test
    void testCreateTable() {
        var sql = """
            CREATE TABLE `tag`  (
              `id` int(11) NOT NULL AUTO_INCREMENT,
              `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '标签名字',
              `type` int(11) NULL DEFAULT NULL COMMENT '所属类别：0文章，1类别',
              PRIMARY KEY (`id`) USING BTREE
            ) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '标签' ROW_FORMAT = Dynamic;
            """;
        assertEquals("CREATE TABLE `tag_r` (`id` int (11) NOT NULL AUTO_INCREMENT, `name` varchar (50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '标签名字', `type` int (11) NULL DEFAULT NULL COMMENT '所属类别：0文章，1类别', PRIMARY KEY (`id`) USING BTREE) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '标签' ROW_FORMAT = Dynamic", interceptor.changeTable(sql));
    }

    @Test
    void testCreateTableIfNotExists() {
        var sql = """
            CREATE TABLE IF NOT EXISTS `user_info` (
                `id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                `username` VARCHAR(50) NOT NULL UNIQUE,
                `email` VARCHAR(100) NOT NULL UNIQUE,
                `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
            """;
        assertEquals("CREATE TABLE IF NOT EXISTS `user_info_r` (`id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY, `username` VARCHAR (50) NOT NULL UNIQUE, `email` VARCHAR (100) NOT NULL UNIQUE, `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4", interceptor.changeTable(sql));
    }

    @Test
    void testDropTableIfExists() {
        var sql = "DROP TABLE IF EXISTS `tag`";
        assertEquals("DROP TABLE IF EXISTS `tag_r`", interceptor.changeTable(sql));
    }

    @Test
    void testIssues6730() {
        // https://github.com/baomidou/mybatis-plus/issues/6730
        var sql = "select * from user order by top_bottom_sort desc, 0- EXTRACT(EPOCH FROM req_delivery_time) desc";
        assertEquals("SELECT * FROM user_r ORDER BY top_bottom_sort DESC, 0 - EXTRACT(EPOCH FROM req_delivery_time) DESC", interceptor.changeTable(sql));
    }

    @Test
    void testSelectJoin() {
        var sql = "SELECT * FROM entity e join entity1 e1 on e1.id = e.id WHERE e.id = ? OR e.name = ?";
        assertEquals("SELECT * FROM entity_r e JOIN entity1_r e1 ON e1.id = e.id WHERE e.id = ? OR e.name = ?", interceptor.changeTable(sql));
    }

    @Test
    void testSelectWithAs() {
        var sql = "with with_as_A as (select * from entity) select * from with_as_A";
        assertEquals("WITH with_as_A AS (SELECT * FROM entity_r) SELECT * FROM with_as_A_r", interceptor.changeTable(sql));
    }

    @Test
    void testDuplicateKeyUpdate() {
        var sql = "INSERT INTO entity (name,age) VALUES ('秋秋',18),('秋秋','22') ON DUPLICATE KEY UPDATE age=18";
        assertEquals("INSERT INTO entity_r (name, age) VALUES ('秋秋', 18), ('秋秋', '22') ON DUPLICATE KEY UPDATE age = 18", interceptor.changeTable(sql));
    }

    @Test
    void testDelete() {
        var sql = "delete from entity where id = ?";
        assertEquals("DELETE FROM entity_r WHERE id = ?", interceptor.changeTable(sql));
    }

    @Test
    void testUpdate() {
        var sql = "update entity set name = ? where id = ?";
        assertEquals("UPDATE entity_r SET name = ? WHERE id = ?", interceptor.changeTable(sql));
    }

    @Test
    void testPartition() {
        // 这种jsql解析不了
        var sql = """
            -- 查询2023年Q2分区数据
            SELECT\s
                region,
                SUM(gross_profit) AS 区域总利润,
                AVG(order_value) AS 平均订单金额
            FROM\s
                sales_data
            PARTITION BY\s
                (TO_DATE(order_date, 'YYYY-MM-DD'))
            INTERVAL MONTHLY
            FOR PARTITION BETWEEN '2023-04-01' AND '2023-06-30'
            GROUP BY\s
                region;
            """;
        assertEquals("-- 查询2023年Q2分区数据\n" +
            "SELECT \n" +
            "    region,\n" +
            "    SUM(gross_profit) AS 区域总利润,\n" +
            "    AVG(order_value) AS 平均订单金额\n" +
            "FROM \n" +
            "    sales_data_r\n" +
            "PARTITION BY \n" +
            "    (TO_DATE(order_date, 'YYYY-MM-DD'))\n" +
            "INTERVAL MONTHLY\n" +
            "FOR PARTITION BETWEEN '2023-04-01' AND '2023-06-30'\n" +
            "GROUP BY \n" +
            "    region;\n", interceptor.changeTable(sql));
    }

    @Test
    void test2() {
        // TODO 这里Jsql解析失败了....
        var sql = """
            SELECT\s
                COUNT(*) AS 订单总数,
                SUM(o.order_total) AS 总销售额,
                SUM(CASE WHEN o.status = 'completed' THEN 1 ELSE 0 END) AS 完成订单数
            FROM\s
                orders o
            JOIN\s
                customers c ON o.customer_id = c.customer_id
            JOIN\s
                order_items oi ON o.order_id = oi.order_id
            WHERE\s
                c.region = 'North America'
                AND o.order_date BETWEEN '2023-04-01' AND '2023-04-30'
            GROUP BY\s
                o.customer_id
            HAVING\s
                COUNT(*) > 10;
            ORDER BY\s
                total_sales DESC;
            """;
        assertEquals("SELECT \n" +
            "    COUNT(*) AS 订单总数,\n" +
            "    SUM(o.order_total) AS 总销售额,\n" +
            "    SUM(CASE WHEN o.status = 'completed' THEN 1 ELSE 0 END) AS 完成订单数\n" +
            "FROM \n" +
            "    orders_r o\n" +
            "JOIN \n" +
            "    customers_r c ON o.customer_id = c.customer_id\n" +
            "JOIN \n" +
            "    order_items_r oi ON o.order_id = oi.order_id\n" +
            "WHERE \n" +
            "    c.region = 'North America'\n" +
            "    AND o.order_date BETWEEN '2023-04-01' AND '2023-04-30'\n" +
            "GROUP BY \n" +
            "    o.customer_id\n" +
            "HAVING \n" +
            "    COUNT(*) > 10;\n" +
            "ORDER BY \n" +
            "    total_sales DESC;\n", interceptor.changeTable(sql));
    }

    @Test
    void test3() {
        // 这种jsql解析不了的
        var sql = """
            DELIMITER $$
            DECLARE\s
                cur CURSOR FOR\s
                    SELECT employee_id FROM employees WHERE salary < 50000;
                emp_id INT;
            BEGIN
                OPEN cur;
                WHILE TRUE DO
                    FETCH cur INTO emp_id;
                    IF cur_rowcount = 0 THEN
                        LEAVE;
                    END IF;
                   \s
                    UPDATE employees\s
                    SET salary = salary * 1.1\s
                    WHERE employee_id = emp_id;
                   \s
                    INSERT INTO audit_log (employee_id, old_salary, new_salary)
                    VALUES (emp_id, salary_before_update, salary_after_update);
                END WHILE;
                CLOSE cur;
            END
            $$
            DELIMITER ;
            """;
        assertEquals("DELIMITER $$\n" +
            "DECLARE \n" +
            "    cur CURSOR FOR \n" +
            "        SELECT employee_id FROM employees_r WHERE salary < 50000;\n" +
            "    emp_id INT;\n" +
            "BEGIN\n" +
            "    OPEN cur;\n" +
            "    WHILE TRUE DO\n" +
            "        FETCH cur INTO emp_id_r;\n" +
            "        IF cur_rowcount = 0 THEN\n" +
            "            LEAVE;\n" +
            "        END IF;\n" +
            "        \n" +
            "        UPDATE employees_r \n" +
            "        SET salary = salary * 1.1 \n" +
            "        WHERE employee_id = emp_id;\n" +
            "        \n" +
            "        INSERT INTO audit_log_r (employee_id, old_salary, new_salary)\n" +
            "        VALUES (emp_id, salary_before_update, salary_after_update);\n" +
            "    END WHILE;\n" +
            "    CLOSE cur;\n" +
            "END\n" +
            "$$\n" +
            "DELIMITER ;\n", interceptor.changeTable(sql));
    }

    @Test
    void test4() {
        var sql = """
            SELECT *
            FROM employees e
            JOIN departments d ON e.department_id = d.department_id
            WHERE\s
                e.last_name LIKE CONCAT('%', :lastName, '%')
                AND (
                    d.department_name IN (:departmentList)
                    OR :departmentList IS NULL
                )
                AND (
                    e.hire_date >= :startDate
                    OR :startDate IS NULL
                )
            ORDER BY\s
                e.employee_id
            """;
        assertEquals("SELECT * FROM employees_r e JOIN departments_r d ON e.department_id = d.department_id WHERE e.last_name LIKE CONCAT('%', :lastName, '%') AND (d.department_name IN (:departmentList) OR :departmentList IS NULL) AND (e.hire_date >= :startDate OR :startDate IS NULL) ORDER BY e.employee_id", interceptor.changeTable(sql));
    }

    @Test
    void test5() {
        var sql  = """
            SELECT\s
                product_id,
                product_name,
                stock_quantity,
                (SELECT\s
                    SUM(ordered_qty)\s
                 FROM\s
                    purchase_orders po\s
                 WHERE\s
                    po.product_id = products.product_id\s
                    AND po.order_date >= CURDATE() - INTERVAL 3 MONTH) AS recent_order_volume
            FROM\s
                products
            WHERE\s
                stock_quantity < (
                    SELECT\s
                        AVG(recommended_stock)\s
                    FROM\s
                        product_settings\s
                    WHERE\s
                        product_id = products.product_id
                )
                AND recent_order_volume > 500
            """;
        assertEquals("SELECT product_id, product_name, stock_quantity, (SELECT SUM(ordered_qty) FROM purchase_orders_r po WHERE po.product_id = products.product_id AND po.order_date >= CURDATE() - INTERVAL 3 MONTH) AS recent_order_volume FROM products_r WHERE stock_quantity < (SELECT AVG(recommended_stock) FROM product_settings_r WHERE product_id = products.product_id) AND recent_order_volume > 500", interceptor.changeTable(sql));
    }

    @Test
    void test6() {
        var sql = """
            WITH user_activity AS (
                SELECT\s
                    user_id,
                    event_type,
                    event_time,
                    ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY event_time) AS activity_seq
                FROM\s
                    user_events
            )
            SELECT\s
                user_id,
                event_type,
                event_time,
                LAG(event_time) OVER (PARTITION BY user_id ORDER BY event_time) AS prev_event_time
            FROM\s
                user_activity
            WHERE\s
                activity_seq = 5
            """;
        assertEquals("WITH user_activity AS (SELECT user_id, event_type, event_time, ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY event_time) AS activity_seq FROM user_events_r) SELECT user_id, event_type, event_time, LAG(event_time) OVER (PARTITION BY user_id ORDER BY event_time) AS prev_event_time FROM user_activity_r WHERE activity_seq = 5", interceptor.changeTable(sql));
    }

    @Test
    void test7() {
        var sql = "select * from db1.test where a = ?";
        assertEquals("SELECT * FROM db1.test_r WHERE a = ?", interceptor.changeTable(sql));
        sql = "select * from db1.`test` where a = ?";
        assertEquals("SELECT * FROM db1.`test_r` WHERE a = ?", interceptor.changeTable(sql));
        sql = "select * from db1.`test` where a = ?";
        assertEquals("SELECT * FROM db1.`test_r` WHERE a = ?", interceptor.changeTable(sql));
    }

    @Test
    void test8() {
        // 这种jsql解析不了的
        var sql = "SELECT * FROM [HR].[dbo].[Employee_Salary_2023];";
        assertEquals("SELECT * FROM [HR].[dbo].[Employee_Salary_2023_r];", interceptor.changeTable(sql));
    }

    @Test
    void test9(){
        // 这种jsql解析不了的
        var sql = """
            SELECT * FROM [SalesDB].[dbo].[Orders]
            JOIN [MarketingDB].[dbo].[Customers]\s
            ON Orders.CustomerID = Customers.CustomerID;
            """;
        assertEquals("SELECT * FROM [SalesDB].[dbo].[Orders_r]\n" +
            "JOIN [MarketingDB].[dbo].[Customers_r] \n" +
            "ON Orders.CustomerID = Customers.CustomerID;\n", interceptor.changeTable(sql));
    }

    @Test
    void test10() {
        var sql = """
            SELECT * FROM ecommerce_orders\s
            PARTITION (p2022, p2023)
            WHERE order_date BETWEEN '2022-01-01' AND '2023-12-31';
            """;
        assertEquals("SELECT * FROM ecommerce_orders_r PARTITION(p2022, p2023) WHERE order_date BETWEEN '2022-01-01' AND '2023-12-31'", interceptor.changeTable(sql));
    }

    @Test
    void test11() {
        var sql = """
            SELECT order_id, customer_id,amount,
                  RANK() OVER (PARTITION BY customer_id ORDER BY amount DESC) AS rank
                FROM orders;
            """;
        assertEquals("SELECT order_id, customer_id, amount, RANK() OVER (PARTITION BY customer_id ORDER BY amount DESC) AS rank FROM orders_r", interceptor.changeTable(sql));
    }

}
