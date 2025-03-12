package com.baomidou.mybatisplus.test;

import com.baomidou.mybatisplus.extension.ddl.DdlScript;
import org.h2.Driver;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DdlScriptTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DdlScriptTest.class);

    @Test
    void test() throws Exception {
        var dddScript = new DdlScript(Driver.class.getName(),
            "jdbc:h2:mem:test;MODE=mysql;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
            "sa", "");
        LOGGER.info("--------------execute----------------");
        dddScript.run( "select 1 from dual;", msg ->{});
        LOGGER.info("--------------execute----------------");
        dddScript.run( "select 2 from dual;", msg ->{});
        LOGGER.info("--------------run----------------");
        dddScript.run("select 1 from dual;");
        LOGGER.info("--------------run----------------");
        dddScript.run("select 3 from dual#", "#");
    }

}
