package com.pjh.community.dao;

import org.springframework.stereotype.Repository;

@Repository("hibernateDao")
public class TestHibernateDaoImpl implements TestDao{
    @Override
    public String select() {
        return "Hibernate";
    }
}
