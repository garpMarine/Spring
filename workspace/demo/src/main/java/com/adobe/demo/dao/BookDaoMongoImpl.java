package com.adobe.demo.dao;


import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;


@ConditionalOnMissingBean(name="sql")
@Repository("mongo")
public class BookDaoMongoImpl implements BookDao{
    @Override
    public void addBook() {
        System.out.println("Mongo Store!!!");
    }
}
