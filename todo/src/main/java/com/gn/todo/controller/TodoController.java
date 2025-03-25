package com.gn.todo.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gn.todo.dto.PageDto;
import com.gn.todo.dto.SearchDto;
import com.gn.todo.dto.TodoDto;
import com.gn.todo.entity.Todo;
import com.gn.todo.service.TodoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TodoController {

    private Logger logger = LoggerFactory.getLogger(TodoController.class);
    
    private final TodoService service;
    
    // 루트 경로 접근 시 /todo로 리다이렉션
    @GetMapping("/")
    public String redirectToTodo() {
        return "redirect:/todo";
    }
    
    @GetMapping("/todo")
    public String showList(Model model, SearchDto searchDto, PageDto pageDto) {
        logger.info("showList 메서드 호출됨");
        
        if (pageDto.getNowPage() == 0) {
            pageDto.setNowPage(1);
        }
        
        Page<Todo> resultList = service.selectTodoAll(searchDto, pageDto);
        pageDto.setTotalPage(resultList.getTotalPages());
        
        int pageBarSize = 5; 
        int nowPage = pageDto.getNowPage();
        int totalPage = pageDto.getTotalPage();
        
        int start = Math.max(1, nowPage - (pageBarSize / 2));
        int end = Math.min(totalPage, start + pageBarSize - 1);
        start = Math.max(1, end - pageBarSize + 1);
        
        pageDto.setPageBarStart(start);
        pageDto.setPageBarEnd(end);
        pageDto.setPrev(nowPage > 1);
        pageDto.setNext(nowPage < totalPage);
        
        model.addAttribute("todolist", resultList);
        model.addAttribute("searchDto", searchDto != null ? searchDto : new SearchDto());
        model.addAttribute("pageDto", pageDto);
        
        return "todo/list"; 
    }
    
    @PostMapping("/todo")
    @ResponseBody
    public Map<String, Object> createTodoApi(@ModelAttribute("new_ToDo") String content, @ModelAttribute("flag") String flag) {
        logger.info("할 일 추가: " + content);
        
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("res_code", 500);
        resultMap.put("res_msg", "오류 발생");
        
        try {
            TodoDto dto = new TodoDto();
            dto.setContent(content);
            dto.setFlag(flag != null && flag.equals("Y") ? "Y" : "N");
            
            int result = service.createTodo(dto);
            if (result > 0) {
                resultMap.put("res_code", 200);
                resultMap.put("res_msg", "할 일이 등록되었습니다");
            } else {
                resultMap.put("res_code", 400);
                resultMap.put("res_msg", "할 일 등록에 실패하였습니다.");
            }
        } catch (Exception e) {
            logger.error("오류 발생", e);
        }
        
        return resultMap;
    }
    
    // 할 일 삭제
    @DeleteMapping("/todo/{no}")
    @ResponseBody
    public Map<String, Object> deleteTodoApi(@PathVariable("no") Long no) {
        logger.info("할 일 삭제: no=" + no);
        
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("res_code", 500);
        resultMap.put("res_msg", "오류 발생");
        
        try {
            int result = service.deleteTodo(no);
            if (result > 0) {
                resultMap.put("res_code", 200);
                resultMap.put("res_msg", "할 일이 삭제되었습니다");
            } else {
                resultMap.put("res_code", 400);
                resultMap.put("res_msg", "할 일 삭제에 실패하였습니다.");
            }
        } catch (Exception e) {
            logger.error("오류 발생", e);
        }
        
        return resultMap;
    }
    
    // 완료 상태 업데이트
    @PostMapping("/todo/{no}")
    @ResponseBody
    public Map<String, Object> updateFlagApi(@PathVariable("no") Long no) {
        logger.info("완료 상태 업데이트: no=" + no);
        
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("res_code", 500);
        resultMap.put("res_msg", "오류 발생");
        
        try {
            int result = service.toggleFlag(no);
            if (result > 0) {
                resultMap.put("res_code", 200);
                resultMap.put("res_msg", "완료 상태가 업데이트되었습니다");
            } else {
                resultMap.put("res_code", 400);
                resultMap.put("res_msg", "완료 상태 업데이트에 실패하였습니다.");
            }
        } catch (Exception e) {
            logger.error("오류 발생", e);
        }
        
        return resultMap;
    }
}