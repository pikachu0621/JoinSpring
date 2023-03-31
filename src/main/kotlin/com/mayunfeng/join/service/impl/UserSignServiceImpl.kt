package com.mayunfeng.join.service.impl

import com.mayunfeng.join.base.BaseServiceImpl
import com.mayunfeng.join.config.TOKEN_PARAMETER
import com.mayunfeng.join.mapper.TokenTableMapper
import com.mayunfeng.join.mapper.UserSignTableMapper
import com.mayunfeng.join.model.UserSignAndStartSign
import com.mayunfeng.join.model.UserSignTable
import com.mayunfeng.join.service.IUserSignService
import com.mayunfeng.join.service.ParameterException
import com.mayunfeng.join.service.StartSignNulAddUserEditException
import com.mayunfeng.join.utils.JsonResult
import com.mayunfeng.join.utils.OtherUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest


@Service
class UserSignServiceImpl: BaseServiceImpl(), IUserSignService {

    @Autowired
    private lateinit var tokenServiceImpl: TokenServiceImpl

    @Autowired
    private lateinit var groupServiceImpl: GroupServiceImpl

    @Autowired
    private lateinit var joinGroupServiceImpl: JoinGroupServiceImpl

    @Autowired
    private lateinit var startSignServiceImpl: StartSignServiceImpl

    @Autowired
    private lateinit var userSignTableMapper: UserSignTableMapper

    @Autowired
    private lateinit var request: HttpServletRequest




    override fun startUserSign(signId: Long, key: String?): JsonResult<Boolean> {
        TODO("Not yet implemented")
    }

    override fun queryAllBySignId(signId: Long): JsonResult<UserSignAndStartSign> {
        TODO("Not yet implemented")
    }

    override fun queryUserSign(): JsonResult<ArrayList<UserSignTable>> {
        TODO("Not yet implemented")
    }


    override fun notifyUserSign(groupId: Long, signId: Long) {
        if (OtherUtils.isFieldEmpty(groupId, signId)) throw ParameterException()
        groupServiceImpl.verifyGroup(groupId)
        startSignServiceImpl.verifySign(signId)
        val queryJoinGroupAllUser = joinGroupServiceImpl.queryJoinGroupAllUser(groupId).result!!.result!!
        if ((queryJoinGroupAllUser.size <= 1)) throw StartSignNulAddUserEditException()
        queryJoinGroupAllUser.forEach {
            userSignTableMapper.insert(UserSignTable().apply {
                this.userId = it.id
                this.signId = signId
            })
        }
    }




    override fun delUserSignBySign(signId: Long) {
        super.delUserSignBySign(signId)
    }






}