package com.example.netnew.bean

import java.io.Serializable

/**
 * 用户
 */
class User(//用户ID
    var id: Int, //账号
    var account: String, //密码
    var password: String, //昵称
    var name: String, //性别
    var sex: String, //手机号
    var phone: String, //地址
    var address: String, //头像
    var photo: String
) : Serializable