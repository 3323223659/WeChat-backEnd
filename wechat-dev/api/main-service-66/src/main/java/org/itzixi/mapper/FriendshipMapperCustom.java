package org.itzixi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.itzixi.pojo.Friendship;
import org.itzixi.pojo.vo.ContactsVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 朋友关系表 Mapper 接口
 * </p>
 *
 * @author yang
 */
public interface FriendshipMapperCustom {

    public List<ContactsVO> queryMyFriends(@Param("paramMap") Map<String, Object> map);

}
