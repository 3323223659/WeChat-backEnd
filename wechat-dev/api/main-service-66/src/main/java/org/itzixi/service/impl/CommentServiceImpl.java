package org.itzixi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.itzixi.base.BaseInfoProperties;
import org.itzixi.mapper.CommentMapper;
import org.itzixi.mapper.CommentMapperCustom;
import org.itzixi.pojo.Comment;
import org.itzixi.pojo.Users;
import org.itzixi.pojo.bo.CommentBO;
import org.itzixi.pojo.vo.CommentVO;
import org.itzixi.service.CommentService;
import org.itzixi.service.UsersService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl extends BaseInfoProperties implements CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private UsersService usersService;

    @Resource
    private CommentMapperCustom commentMapperCustom;

    @Transactional
    @Override
    public CommentVO createComment(CommentBO commentBO) {

        // 新增留言
        Comment pendingComment = new Comment();

        BeanUtils.copyProperties(commentBO, pendingComment);
        pendingComment.setCreatedTime(LocalDateTime.now());

        commentMapper.insert(pendingComment);

        // 留言后的最新评论数据需要返回给前端（提供前端做的扩展数据）
        CommentVO commentVO = new CommentVO();
        BeanUtils.copyProperties(pendingComment, commentVO);

        Users commentUser = usersService.getById(commentBO.getCommentUserId());
        commentVO.setCommentUserNickname(commentUser.getNickname());
        commentVO.setCommentUserFace(commentUser.getFace());
        commentVO.setCommentId(pendingComment.getId());

        return commentVO;
    }

    @Override
    public List<CommentVO> queryAll(String friendCircleId) {

        Map<String, Object> map = new HashMap<>();
        map.put("friendCircleId", friendCircleId);

        return commentMapperCustom.queryFriendCircleComments(map);
    }

    @Transactional
    @Override
    public void deleteComment(String commentUserId,
                              String commentId,
                              String friendCircleId) {

        QueryWrapper<Comment> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.eq("id", commentId);
        deleteWrapper.eq("comment_user_id", commentUserId);
        deleteWrapper.eq("friend_circle_id", friendCircleId);

        commentMapper.delete(deleteWrapper);
    }
}
