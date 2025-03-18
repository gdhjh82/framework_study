package com.gn.mvc.dto;

import com.gn.mvc.entity.Attach;
import com.gn.mvc.entity.Board;

import groovy.transform.ToString;
import groovy.transform.builder.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class AttachDto {
	private Long attach_no;
	private Long board_no;
	private String ori_name;
	private String new_name;
	private String attach_path;
	
	public Attach toEntity() {
		return Attach.builder()
				.attachNo(attach_no)
				.oriName(ori_name)
				.newName(new_name)
				.attachPath(attach_path)
				.board(Board.builder().boardNo(board_no).build())
				.build();
	}
	
}
