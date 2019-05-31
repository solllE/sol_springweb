package org.sol.letter;

import java.util.List;

import org.sol.letter.Letter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class LetterDao {
	//받은 목록
	static final String LIST_RECEIVE_LETTERS = "select letterId, title, content, senderId, senderName cdate from letter where receiverId=?";
	//보낸 목록
	static final String LIST_SEND_LETTERS = "select letterId, title, content, receiverId, receiverName cdate from letter where senderId=?";
	//받은 편지 보기
	static final String GET_RECEIVE_LETTER = "select letterId, title, content, senderId, senderName, cdate from letter where letterId=? and receiverId=?";
	//보낸 편지 보기
	static final String GET_SEND_LETTER = "select letterId, title, content, receiverId, receiverName, cdate from letter where letterId=? and senderId=?";
	//편지 쓰기
	static final String ADD_LETTER = "insert letter(title,content,senderId,senderName,receiverId,receiverName) values(?,?,?,?,?,?)";
	//편지삭제
	static final String DELETE_LETTER = "delete from letter where (letterId, senderId) = (?,?)";
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	RowMapper<Letter> letterMapper = new BeanPropertyRowMapper<>(Letter.class);
	
	/**
	 * 받은 목록
	 */
	public List<Letter> listReceiveLetter(int offset, int count) {
		return jdbcTemplate.query(LIST_RECEIVE_LETTERS, letterMapper, offset,count);
	}
	
	/**
	 * 보낸 목록
	 */
	public List<Letter> listSendLetter(int offset, int count) {
		return jdbcTemplate.query(LIST_SEND_LETTERS, letterMapper, offset,count);
	}

	/**
	 * 받은 편지 조회
	 */
	public Letter getReceiveLetter(String letterId) {
		return jdbcTemplate.queryForObject(GET_RECEIVE_LETTER, letterMapper, letterId);
	}
	
	/**
	 * 보낸 편지 조회
	 */
	public Letter getSendLetter(String letterId) {
		return jdbcTemplate.queryForObject(GET_SEND_LETTER, letterMapper, letterId);
	}

	/**
	 * 편지 쓰기
	 */
	public int addLetter(Letter letter) {
		return jdbcTemplate.update(ADD_LETTER, letter.getTitle(), letter.getContent(), letter.getReceiverId(), letter.getReceiverName());
	}

	/**
	 * 글 삭제
	 */
	public int deleteLetter(String letterId, String senderId) {
		return jdbcTemplate.update(DELETE_LETTER, letterId, senderId);
	}
}
