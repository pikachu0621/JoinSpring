package com.pkpk.join

import com.pkpk.join.mapper.UserSignTableMapper
import com.pkpk.join.mapper.UserTableMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.Arrays

@SpringBootTest
class AdminApplicationTests {

    @Autowired
    private lateinit var userMapper: UserTableMapper

    @Test
    fun contextLoads() {

    }

}
