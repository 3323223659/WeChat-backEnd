package org.itzixi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.itzixi.pojo.Users;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author yang
 */
public interface UsersService {

    /**
     * 判断用户是否存在，如果存在则返回用户信息，否则null
     * @param mobile
     * @return
     */
    public Users queryMobileIfExist(String mobile);

    /**
     * 创建用户信息，并且返回用户对象
     * @param mobile
     * @return
     */
    public Users createUsers(String mobile, String nickname);

}
