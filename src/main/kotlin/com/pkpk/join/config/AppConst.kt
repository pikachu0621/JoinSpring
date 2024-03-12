package com.pkpk.join.config

const val TOKEN_PARAMETER = "token"

const val DEVICE_INFO_PARAMETER = "device"

/**
 * 前缀
 */
const val PREFIX = "pk"

//====================================================================================================================//

/**
 * [com.pkpk.join.controller.GroupController]
 */
const val API_GROUP = "/${PREFIX}-group-api"

/**
 * [com.pkpk.join.controller.JoinGroupController]
 */
const val API_JOIN_GROUP = "/${PREFIX}-join-group-api"

/**
 * [com.pkpk.join.controller.PictureController]
 */
const val API_PICTURE = "/${PREFIX}-pic-api"

/**
 * [com.pkpk.join.controller.PublicController]
 */
const val API_PUBLIC = "/${PREFIX}-puc-api"

/**
 * [com.pkpk.join.controller.StartSignController]
 */
const val API_START_SIGN = "/${PREFIX}-sign-api"

/**
 * [com.pkpk.join.controller.UserController]
 */
const val API_USER = "/${PREFIX}-user-api"

/**
 * [com.pkpk.join.controller.UserSignController]
 */
const val API_USER_SIGN = "/${PREFIX}-user-sign-api"

/**
 * [com.pkpk.join.controller.BackstageController]
 */
const val API_BG = "/${PREFIX}-bg-api"

//====================================================================================================================//

/**
 * [com.pkpk.join.model.GroupTable]
 */
const val TABLE_GROUP = "${PREFIX}_group_table"

/**
 * [com.pkpk.join.model.JoinGroupTable]
 */
const val TABLE_JOIN_GROUP = "${PREFIX}_join_group_table"

/**
 * [com.pkpk.join.model.UserLogTable]
 */
const val TABLE_USER_LOGIN_LOG = "${PREFIX}_user_log_table"

/**
 * [com.pkpk.join.model.StartSignTable]
 */
const val TABLE_START_SIGN = "${PREFIX}_start_sign_table"

/**
 * [com.pkpk.join.model.TokenTable]
 */
const val TABLE_TOKEN = "${PREFIX}_token_table"

/**
 * [com.pkpk.join.model.UserSignTable]
 */
const val TABLE_USER_SIGN = "${PREFIX}_user_sign_table"

/**
 * [com.pkpk.join.model.UserTable]
 */
const val TABLE_USER = "${PREFIX}_user_table"

//====================================================================================================================//