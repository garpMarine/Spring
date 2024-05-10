package com.adobe.orderapp.dto;
// construct + getter [ no setter ]
public record Post(Integer id, Integer userId, String title, String body) {
}
