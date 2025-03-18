package com.gn.mvc.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.assertj.core.api.Assertions;

import com.gn.mvc.entity.Board;

@SpringBootTest
class BoardServiceTest {
	
	//객체를 자동으로 주입하는 역활을 한다.
	@Autowired
	private BoardService service;
	
	//테스트 메서드임을 나타내는 어노테이션.
	@Test
	void selectBoardOne_success() {
		// 1. 예상 데이터
		Long id = 13L;
		Board expected = Board.builder().boardTitle("테스트 1").build();
		// 2. 실제 데이터
		Board real = service.selectBoardOne(id);
		// 3. 비교 및 검증
		assertEquals(expected.getBoardTitle(),real.getBoardTitle());
	}
	
	// 실패 테스트
	// 존재하지 않는 PK기준으로 조회 요청
	@Test
	void selectBoardOne_fail() {
		// 1. 예상 데이터
		Long id = 1500L;
		Board expected = null;
		// 2. 실제 데이터
		Board real = service.selectBoardOne(id);
		// 3. 비교 및 검증
		assertEquals(expected,real);
		
	}
}
