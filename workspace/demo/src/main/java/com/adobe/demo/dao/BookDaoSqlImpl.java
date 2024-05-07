package com.adobe.demo.dao;

import org.springframework.stereotype.Repository;

@Repository
public class BookDaoSqlImpl implements BookDao{
    @Override
    public void addBook() {
        System.out.println("SQL Store !!!");
    }
}
