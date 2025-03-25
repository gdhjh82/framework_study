package com.gn.todo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageDto {
    private int nowPage; // 현재 페이지
    private int numPerPage = 10; // 페이지당 항목 수
    private int totalPage; // 전체 페이지 수
    private int pageBarStart; // 페이지 바 시작 번호
    private int pageBarEnd; 
    private boolean prev; 
    private boolean next; 
}