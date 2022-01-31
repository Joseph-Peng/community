package com.pjh.community.service;

import com.pjh.community.entity.User;
import com.pjh.community.utils.CommunityConstant;
import com.pjh.community.utils.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.pjh.community.utils.CommunityConstant.ENTITY_TYPE_USER;

@Service
public class FollowService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    // 用户对某一个实体发起的关注
    public void follow(int userId, int entityType,int entityId){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
                String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);

                operations.multi();

                operations.opsForZSet().add(followeeKey, entityId, System.currentTimeMillis());
                operations.opsForZSet().add(followerKey,userId, System.currentTimeMillis());

                return operations.exec();
            }
        });
    }

    // 用户对某一个实体发起的取消关注
    public void unfollow(int userId, int entityType,int entityId){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
                String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);

                operations.multi();

                operations.opsForZSet().remove(followeeKey, entityId);
                operations.opsForZSet().remove(followerKey,userId);

                return operations.exec();
            }
        });
    }

    // 查询关注的实体的数量
    public long findFolloweeCount(int userId, int entityType){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId,entityType);
        return redisTemplate.opsForZSet().zCard(followeeKey);
    }

    // 查询实体的粉丝的数量
    public long findFollowerCount(int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return redisTemplate.opsForZSet().zCard(followerKey);
    }

    // 查询当前用户是否已关注该实体
    public boolean hasFollowed(int userId, int entityType, int entityId) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return redisTemplate.opsForZSet().score(followeeKey, entityId) != null;
    }

    /**
     * 查询某个用户关注的人
     * @param userId   需要查询的用户ID
     * @param entityTypeUser 实体类型，这里默认是查询用户
     * @param offset  分页起始
     * @param limit   一页条数
     * @return
     */
    public List<Map<String, Object>> findFollowees(int userId, int entityTypeUser, int offset, int limit) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityTypeUser);
        // followee:userId:entityType   --->  userId,time
        // 得到所有userId关注过的用户的id
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followeeKey, offset, offset + limit - 1);
        if (targetIds == null){
            return null;
        }

        List<Map<String, Object>> res = new ArrayList<>();
        // 获取用户信息和关注时间，并放入返回值中
        for(Integer targetId :targetIds){
            Map<String, Object> map = new HashMap<>();
            User user = userService.selectById(targetId);
            map.put("user", user);
            Double followTime = redisTemplate.opsForZSet().score(followeeKey, targetId);
            map.put("followTime", new Date(followTime.longValue()));
            res.add(map);
        }
        return res;
    }

    // 查询某用户的粉丝
    public List<Map<String, Object>> findFollowers(int userId, int offset, int limit) {
        // follower:3:userId  ---> entityId,time(这里的entityId是粉丝的Id)
        String followerKey = RedisKeyUtil.getFollowerKey(ENTITY_TYPE_USER, userId);
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followerKey, offset, offset + limit - 1);

        if (targetIds == null) {
            return null;
        }

        List<Map<String, Object>> list = new ArrayList<>();
        for (Integer targetId : targetIds) {
            Map<String, Object> map = new HashMap<>();
            User user = userService.selectById(targetId);
            map.put("user", user);
            Double followTime = redisTemplate.opsForZSet().score(followerKey, targetId);
            map.put("followTime", new Date(followTime.longValue()));
            list.add(map);
        }
        return list;
    }
}
