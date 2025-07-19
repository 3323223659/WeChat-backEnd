package org.itzixi.netty.websocket;

import io.netty.channel.Channel;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * 会话管理
 * 用户id和channel的关联处理
 * @Auther yang
 */
public class UserChannelSession {

    /**
     * 多设备会话映射表（线程安全实现）
     * Key: 用户ID
     * Value: 该用户所有活跃的Channel列表（CopyOnWriteArrayList保证线程安全）
     */
    private static final ConcurrentMap<String, CopyOnWriteArrayList<Channel>> multiSession =
            new ConcurrentHashMap<>();

    /**
     * Channel与用户ID的逆向映射（线程安全实现）
     * Key: Channel长ID（asLongText）
     * Value: 绑定的用户ID
     */
    private static final ConcurrentMap<String, String> userChannelIdRelation =
            new ConcurrentHashMap<>();

    /**
     * 添加用户-Channel关联关系（原子操作）
     * @param channelId Channel的长ID（ctx.channel().id().asLongText()）
     * @param userId 绑定的用户ID
     */
    public static void putUserChannelIdRelation(String channelId, String userId) {
        userChannelIdRelation.put(channelId, userId);
    }

    /**
     * 通过ChannelID查找用户ID（线程安全）
     * @param channelId Channel的长ID
     * @return 关联的用户ID，未找到返回null
     */
    public static String getUserIdByChannelId(String channelId) {
        return userChannelIdRelation.get(channelId);
    }

    /**
     * 添加用户与设备Channel关系（原子操作）
     * @param userId 用户ID
     * @param channel 新增的Channel对象
     */
    public static void putMultiChannels(String userId, Channel channel) {
        multiSession.compute(userId, (k, v) -> {
            if (v == null) v = new CopyOnWriteArrayList<>();
            v.addIfAbsent(channel); // 避免重复添加
            return v;
        });

        // 添加用户-ChannelId关联关系
        putUserChannelIdRelation(channel.id().asLongText(), userId);
    }

    /**
     * 获取用户的所有活跃Channel（线程安全快照）
     * @param userId 用户ID
     * @return Channel列表（可能为null）
     */
    public static List<Channel> getMultiChannels(String userId) {
        return multiSession.get(userId);
    }

    /**
     * 移除指定用户的无效Channel（原子操作）
     * @param userId 用户ID
     * @param channelId 需要移除的Channel长ID
     */
    public static void removeUselessChannels(String userId, String channelId) {
        multiSession.computeIfPresent(userId, (k, v) -> {
            v.removeIf(c -> c.id().asLongText().equals(channelId));
            return v.isEmpty() ? null : v; // 自动清理空列表
        });
        userChannelIdRelation.remove(channelId);
    }

    /**
     * 获取用户其他设备的Channel（线程安全快照）
     * @param userId 用户ID
     * @param channelId 需要排除的Channel长ID
     * @return 其他设备的Channel列表（可能为null）
     */
    public static List<Channel> getMyOtherChannels(String userId, String channelId) {
        CopyOnWriteArrayList<Channel> channels = multiSession.get(userId);
        if (channels == null || channels.isEmpty()) {
            return null;
        }
        return channels.stream()
                .filter(c -> !c.id().asLongText().equals(channelId))
                .collect(Collectors.toList());
    }

    /**
     * 打印当前所有会话状态（线程安全快照）
     * 注意：输出期间可能有数据更新，但不影响输出一致性
     */
    public static void outputMulti() {
        System.out.println("++++++++++++++++++");
        multiSession.forEach((userId, channels) -> {
            System.out.println("----------");
            System.out.println("UserId: " + userId);
            channels.forEach(c ->
                    System.out.println("\t\t ChannelId: " + c.id().asLongText())
            );
            System.out.println("----------");
        });
        System.out.println("++++++++++++++++++");
    }

}
