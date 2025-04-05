package com.baomidou.mybatisplus.generator.index;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MapperMethodHandlerTest {

    @Test
    void testIsPrimaryKey() {
        var method = new DefaultGenerateMapperMethodHandler();
        Assertions.assertTrue(method.isPrimaryKey("PRIMARY"));
        Assertions.assertTrue(method.isPrimaryKey("primary"));
        Assertions.assertTrue(method.isPrimaryKey("PRIMARY_KEY_1"));
        Assertions.assertTrue(method.isPrimaryKey("primary_key_1"));
        Assertions.assertFalse(method.isPrimaryKey("id_idx"));
    }

}
