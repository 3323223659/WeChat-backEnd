package org.itzixi.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.itzixi.base.BaseInfoProperties;
import org.itzixi.grace.result.GraceJSONResult;
import org.itzixi.pojo.FriendCircleLiked;
import org.itzixi.pojo.bo.CommentBO;
import org.itzixi.pojo.bo.FriendCircleBO;
import org.itzixi.pojo.vo.CommentVO;
import org.itzixi.pojo.vo.FriendCircleVO;
import org.itzixi.service.CommentService;
import org.itzixi.service.FriendCircleService;
import org.itzixi.utils.PagedGridResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Auther yang
 */
@RestController
@RequestMapping("comment")
public class CommentController extends BaseInfoProperties {

    @Resource
    private CommentService commentService;

    @PostMapping("create")
    public GraceJSONResult create(@RequestBody CommentBO friendCircleBO,
                                  HttpServletRequest request) {
        CommentVO commentVO = commentService.createComment(friendCircleBO);
        return GraceJSONResult.ok(commentVO);
    }

    @PostMapping("query")
    public GraceJSONResult create(String friendCircleId) {
        return GraceJSONResult.ok(commentService.queryAll(friendCircleId));
    }

    @PostMapping("delete")
    public GraceJSONResult delete(String commentUserId,
                                  String commentId,
                                  String friendCircleId) {

        if (StringUtils.isBlank(commentUserId) ||
                StringUtils.isBlank(commentId) ||
                StringUtils.isBlank(friendCircleId)
        ) {
            return GraceJSONResult.error();
        }

        commentService.deleteComment(commentUserId, commentId, friendCircleId);
        return GraceJSONResult.ok();
    }

}
