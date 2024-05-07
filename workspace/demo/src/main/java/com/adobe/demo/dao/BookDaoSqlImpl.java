package com.adobe.demo.dao;


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;



@Repository("sql")
public class BookDaoSqlImpl implements BookDao{
    @Override
    public void addBook() {
        System.out.println("SQL Store !!!");
    }
}
