package com.baomidou.mybatisplus.test.kotlin

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers
import com.baomidou.mybatisplus.extension.toolkit.Db
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class ChainWrappersTest : BaseDbTest<UserMapper>() {

    @Test
    fun testQueryChain() {
        val list = ChainWrappers.ktQueryChain(User::class.java).eq(User::id, 1).list()
        assertThat(list.size).isEqualTo(1)
        val listRid = ChainWrappers.ktQueryChain(User::class.java).eq(User::roleId, 1).list()
        assertThat(listRid.size).isEqualTo(2)
        val oneUser = ChainWrappers.ktQueryChain(User::class.java).eq(User::id, 1).one()
        assertThat("gozei").isEqualTo(oneUser.name)
        val count = ChainWrappers.ktQueryChain(User::class.java).eq(User::id, 1).count()
        assertThat(count).isEqualTo(1)
        val exists = ChainWrappers.ktQueryChain(User::class.java).eq(User::id, 1).exists()
        assertThat(exists).isTrue()
        val oneIdName = ChainWrappers.ktQueryChain(User::class.java).eq(User::id, 1).eq(User::name, "gozei").one()
        assertThat(oneIdName.roleId).isEqualTo(1)
        val page: IPage<User> = Db.page(Page(1, 3), User::class.java)
        val pageU = ChainWrappers.ktQueryChain(User::class.java).page(page)
        assertThat(pageU.size).isEqualTo(3)
    }

    @Test
    fun testUpdate() {
        ChainWrappers.ktUpdateChain(User::class.java).eq(User::id, 3).set(User::name, "haku").update()
        assertThat("haku").isEqualTo(Db.ktQuery(User::class.java).eq(User::id, 3).one().name)
        ChainWrappers.ktUpdateChain(User::class.java).eq(User::id, 2).set(User::name, "haku").set(User::roleId, 4)
            .update()
        assertThat(4).isEqualTo(Db.ktQuery(User::class.java).eq(User::id, 2).one().roleId)
    }

    @Test
    fun testDefaultMethod() {
        doTestAutoCommit(fun(m) {
            assertThat("hello baomidou!").isEqualTo(m.hello())
            assertThat(m.findById(1)).isNotNull()
            assertThat(m.findById(-1)).isNull()
        })
    }

    @Test
    fun testSetSql() {
        assertThat(
            ChainWrappers.ktUpdateChain(User::class.java).eq(User::id, 3).setSql("username = {0}", "haku").update()
        ).isTrue();
    }

    @Test
    fun testSelectByPredicate() {
//        Assertions.assertDoesNotThrow { ChainWrappers.ktQueryChain(User::class.java).select({ true }).list() }
        doTestAutoCommit(fun(m) {
            Assertions.assertDoesNotThrow {
                m.selectList(QueryWrapper(User()).select { true })
            }
        })
    }

    override fun tableDataSql(): String {
        return "insert into `sys_user`(id,username,role_id) values(1,'gozei',1),(2,'chocolate',2),(3,'sheep',1)"
    }

    override fun tableSql(): List<String>? {
        return Arrays.asList(
            "drop table if exists `sys_user`", "CREATE TABLE IF NOT EXISTS `sys_user` (" +
            "id INT NOT NULL," +
            "username VARCHAR(30) NULL DEFAULT NULL," +
            "role_id INT NULL DEFAULT NULL," +
            "PRIMARY KEY (id))"
        )
    }
}
