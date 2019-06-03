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
	static final String LIST_SEND_LETTERS = "select letterId, title, receiverId, receiverName, left(cdate,16) cdate from letter where senderId=? order by letterId desc limit ?,?";
	
	//편지 보기
	static final String GET_LETTER = "select letterId, title, content, senderId, senderName, receiverId, receiverName, left(cdate,16) cdate from letter where letterId=? and (senderId=? or receiverId=?)";

	//편지 쓰기
	static final String ADD_LETTER = "insert letter(title,content,senderId,senderName,receiverId,receiverName) values(?,?,?,?,?,?)";

	//편지 삭제
	static final String DELETE_LETTER = "delete from letter where letterId=? and senderId=?";
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	RowMapper<Letter> letterMapper = new BeanPropertyRowMapper<>(Letter.class);
	
	/**
	 * 받은 목록
	 */
	public List<Letter> listReceiveLetter(String receiverId, int offset, int count) {
		return jdbcTemplate.query(LIST_RECEIVE_LETTERS, letterMapper, receiverId, offset,count);
	}
	
	/**
	 * 보낸 목록
	 */
	public List<Letter> listSendLetter(String senderId, int offset, int count) {
		return jdbcTemplate.query(LIST_RECEIVE_LETTERS, letterMapper, senderId, offset,count);
	}

	/**
	 * 편지 조회
	 */
	public Letter getLetter(String letterId, String memberId) {
		return jdbcTemplate.queryForObject(GET_LETTER, letterMapper, letterId, memberId, memberId);
}

	/**
	 * 편지 쓰기
	 */
	public int addLetter(Letter letter) {
		return jdbcTemplate.update(ADD_LETTER, letter.getTitle(), letter.getContent(), letter.getSenderId(),
				letter.getSenderName(), letter.getReceiverId(), letter.getReceiverName());
	}

	/**
	 * 글 삭제
	 */
	public int deleteLetter(String letterId, String memberId) {
		return jdbcTemplate.update(DELETE_LETTER, letterId, memberId, memberId);
	}
}
