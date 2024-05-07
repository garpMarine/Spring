package com.adobe.demo.dao;


import org.springframework.stereotype.Repository;


@Repository
public class BookDaoMongoImpl implements BookDao{
    @Override
    public void addBook() {
        System.out.println("Mongo Store!!!");
    }
}
