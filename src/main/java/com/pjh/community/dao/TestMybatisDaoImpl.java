package com.pjh.community.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class TestMybatisDaoImpl implements TestDao{
    @Override
    public String select() {
        return "Mybatis";
    }
}
