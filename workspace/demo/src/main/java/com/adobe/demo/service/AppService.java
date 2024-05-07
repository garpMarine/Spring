package com.adobe.demo.service;

import com.adobe.demo.dao.BookDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppService {
    @Autowired
    private BookDao bookDao;

    public void insertBook() {
        bookDao.addBook();
    }
}
