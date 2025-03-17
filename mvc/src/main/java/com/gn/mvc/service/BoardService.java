package com.gn.mvc.service;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gn.mvc.dto.BoardDto;
import com.gn.mvc.dto.PageDto;
import com.gn.mvc.dto.SearchDto;
import com.gn.mvc.entity.Board;
import com.gn.mvc.repository.BoardRepository;
import com.gn.mvc.specification.BoardSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

//	@Autowired
//	BoardRepository repository;
	
	private final BoardRepository repository;
	
	public int deleteBoard(Long id){
		int result = 0;
		try {
			Board target = repository.findById(id).orElse(null);
			if(target != null) {
				repository.deleteById(id);
			}
			result = 1;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public Board selectBoardOne(Long id) {
		return repository.findById(id).orElse(null);
		
	}
	public Board updateBoard(BoardDto param){
		Board result = null;
		// 1. @Id를 쓴 필드를 기준으로 타겟 조회
		Board target = repository.findById(param.getBoard_no()).orElse(null);
		// 2. 타겟이 존재하는 경우 업데이트
		if(target != null) {
			result = repository.save(param.toEntity());
		}
		return result;
	}
	public Page<Board> selectBoardAll(SearchDto searchDto, PageDto pageDto){
		/*
		 * List<Board> list = new ArrayList<Board>(); if(searchDto.getSearch_type() ==
		 * 1) { // 제목 기준으로 검색 list =
		 * repository.findByTitleLike(searchDto.getSearch_text()); }else
		 * if(searchDto.getSearch_type() == 2) { // 내용 기준으로 검색 list =
		 * repository.findByContentLike(searchDto.getSearch_text()); }else
		 * if(searchDto.getSearch_type() == 3) { // 제목 또는 내용 기준으로 검색 list =
		 * repository.findByContentLikeOrfindByTitleLike(searchDto.getSearch_text(),
		 * searchDto.getSearch_text()); }else { // WHERE 절 없이 검색 list =
		 * repository.findAll(); }
		 * 
		 * return list;
		 */
		
		/*
		 * Sort sort = Sort.by("regDate").descending(); if(searchDto.getOrder_type() ==
		 * 2) { sort = Sort.by("regDate").ascending(); }
		 */
		
		Pageable pageable = PageRequest.of(pageDto.getNowPage()-1, pageDto.getNumPerPage(), Sort.by("regDate").descending());
		if(searchDto.getOrder_type() == 2) {
			pageable = PageRequest.of(pageDto.getNowPage()-1, pageDto.getNumPerPage(), Sort.by("regDate").ascending());
		}
		
		Specification<Board> spec = (root,query,criteriaBuilder) -> null;
		if(searchDto.getSearch_type() == 1) {
			spec = spec.and(BoardSpecification.boardTitleContains(searchDto.getSearch_text()));
 		}else if(searchDto.getSearch_type() == 2) {
 			spec = spec.and(BoardSpecification.boardContentContains(searchDto.getSearch_text()));
 		}else if(searchDto.getSearch_type() == 3) {
 			spec = spec.and(BoardSpecification.boardTitleContains(searchDto.getSearch_text()))
 							.or(BoardSpecification.boardContentContains(searchDto.getSearch_text()));
 		}
		Page<Board> list = repository.findAll(spec, pageable );
		return list;
	}
	public BoardDto createBoard(BoardDto dto) {
		//1. 매개변수 dto를 estity로 변경
//		Board parm = Board.builder()
//				.boardTitle(dto.getBoard_title())
//				.boardContent(dto.getBoard_content())
//				.build();
		Board param = dto.toEntity();
		//2. repository의 save() 호출
		Board result = repository.save(param);
		//3. 결과 entity -> dto
		return new BoardDto().toDto(result);
	}
}
