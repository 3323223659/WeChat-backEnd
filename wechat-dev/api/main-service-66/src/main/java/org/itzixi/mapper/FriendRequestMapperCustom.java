package org.itzixi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.itzixi.pojo.FriendRequest;
import org.itzixi.pojo.vo.NewFriendsVO;

import java.util.Map;

/**
 * <p>
 * 好友请求记录表 Mapper 接口
 * </p>
 *
 * @author yang
 */
public interface FriendRequestMapperCustom {

    public Page<NewFriendsVO> queryNewFriendList(@Param("page") Page<NewFriendsVO> page,
                                                 @Param("paramMap") Map<String, Object> map);

}
