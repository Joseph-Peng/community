package com.pjh.community.dao.elasticsearch;

import com.pjh.community.entity.DiscussPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost, Integer> { // 对应的表，表中主键的类型
}
