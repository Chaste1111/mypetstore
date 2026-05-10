import request from '@/utils/request'

export const accountApi = {
  // 用户注册
  register(data) {
    return request({
      url: '/accounts/register',
      method: 'post',
      data
    })
  },

  // 用户登录
  login(data) {
    return request({
      url: '/accounts/login',
      method: 'post',
      data
    })
  },

  // 根据用户名获取账户信息
  getAccountByUsername(username) {
    return request({
      url: `/accounts/${username}`,
      method: 'get'
    })
  },

  // 检查用户名是否存在
  checkUsername(username) {
    return request({
      url: `/accounts/check-username/${username}`,
      method: 'get'
    })
  },

  // 更新账户信息
  updateAccount(username, data) {
    return request({
      url: `/accounts/${username}`,
      method: 'put',
      data
    })
  },

  // 修改密码
  updatePassword(username, oldPassword, newPassword) {
    return request({
      url: `/accounts/${username}/password`,
      method: 'put',
      params: { oldPassword, newPassword }
    })
  }
}
