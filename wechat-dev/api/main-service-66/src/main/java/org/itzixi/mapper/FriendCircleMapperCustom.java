package org.itzixi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.itzixi.pojo.FriendCircle;
import org.itzixi.pojo.vo.FriendCircleVO;
import org.itzixi.pojo.vo.NewFriendsVO;

import java.util.Map;

/**
 * <p>
 * 朋友圈表 Mapper 接口
 * </p>
 *
 * @author yang
 */
public interface FriendCircleMapperCustom {

    public Page<FriendCircleVO> queryFriendCircleList(
            @Param("page") Page<FriendCircleVO> page,
            @Param("paramMap") Map<String, Object> map);

}
