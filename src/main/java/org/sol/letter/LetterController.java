package org.sol.letter;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sol.book.chap11.Member;
import org.sol.letter.LetterDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

public class LetterController {
	
	@Autowired
	LetterDao letterDao;
	
	static final Logger logger = LogManager.getLogger();
	
	/**
	 * 받은 목록
	 */
	@GetMapping("/letter/receiveList")
	public void letterReceiveList(@SessionAttribute("MEMBER") Member member,
			@RequestParam(value = "page", defaultValue = "1") int page, Model model) {

		// 페이지당 행의 수와 페이지의 시작점
		final int COUNT = 100;
		int offset = (page - 1) * COUNT;

		List<Letter> letterReceiveList = letterDao.listReceiveLetter(member.getMemberId(), offset, COUNT);
		model.addAttribute("letterReceiveList", letterReceiveList);
	}

	
	/**
	 * 보낸 목록
	 */
	@GetMapping("/letter/sendList")
	public void letterSendList(@SessionAttribute("MEMBER") Member member,
			@RequestParam(value = "page", defaultValue = "1") int page, Model model) {

		// 페이지당 행의 수와 페이지의 시작점
		final int COUNT = 100;
		int offset = (page - 1) * COUNT;

		List<Letter> letterSendList = letterDao.listSendLetter(member.getMemberId(), offset, COUNT);
		model.addAttribute("letterSendList", letterSendList);
	}

	
	/**
	 * 편지 보기
	 */
	@GetMapping("/letter/viewLetter")
	public void letterView(@RequestParam("letterId") String letterId,
			@SessionAttribute("MEMBER") Member member, Model model) {
		Letter letter = letterDao.getLetter(letterId, member.getMemberId());
		model.addAttribute("letter", letter);
}

	/**
	 * 편지 쓰기 화면
	 */
	@GetMapping("/letter/addLetterForm")
	public String LetterAddForm() {
		return "letter/addLetterForm";
	}

	/**
	 * 편지 쓰기
	 */
	@PostMapping("/letter/addLetter")
	public String letterAdd(Letter letter,
			@SessionAttribute("MEMBER") Member member) {
		letter.setSenderId(member.getMemberId());
		letter.setSenderName(member.getName());
		letterDao.addLetter(letter);
		return "redirect:/app/letter/sendList";
	}

	/**
	 * 편지 삭제
	 */
	@GetMapping("/letter/deleteLetter")
	public String delete(@RequestParam("letterId") String letterId,
			@SessionAttribute("MEMBER") Member member) {
		int updatedRows = letterDao.deleteLetter(letterId,
				member.getMemberId());

		// 권한 체크 : 글이 삭제되었는지 확인
		if (updatedRows == 0)
			// 글이 삭제되지 않음. 자신이 쓴 글이 아님
			throw new RuntimeException("No Authority!");

		logger.debug("글을 삭제했습니다. letterId={}", letterId);
		return "redirect:/app/letter/receivelist";
	}

}
