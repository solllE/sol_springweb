package org.sol.letter;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sol.article.Article;
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
	@GetMapping("/letter/receivelist")
	public void letterReceiveList(
			@RequestParam(value = "page", defaultValue = "1") int page, Model model) {

		// 페이지당 행의 수와 페이지의 시작점
		final int COUNT = 100;
		int offset = (page - 1) * COUNT;

		List<Letter> letterReceiveList = letterDao.listReceiveLetter(offset, COUNT);
		model.addAttribute("letterReceiveList", letterReceiveList);
	}

	
	/**
	 * 보낸 목록
	 */
	@GetMapping("/letter/receivelist")
	public void letterSendList(
			@RequestParam(value = "page", defaultValue = "1") int page, Model model) {

		// 페이지당 행의 수와 페이지의 시작점
		final int COUNT = 100;
		int offset = (page - 1) * COUNT;

		List<Letter> letterSendList = letterDao.listSendLetter(offset, COUNT);
		model.addAttribute("letterSendList", letterSendList);
	}
	
	/**
	 *받은 편지 조회
	 */
	@GetMapping("/letter/viewReceive")
	public void letterViewReceive(@RequestParam("letterId") String letterId, Model model) {
		Letter letter = LetterDao.getReceiveLetter(letterId);
		model.addAttribute("letter", letter);
	}
	
	/**
	 *보낸 편지 조회
	 */
	@GetMapping("/letter/viewSend")
	public void letterViewSend(@RequestParam("letterId") String letterId, Model model) {
		Letter letter = LetterDao.getSendLetter(letterId);
		model.addAttribute("letter", letter);
	}

	/**
	 * 편지 쓰기 화면
	 */
	@GetMapping("/letter/addLetterForm")
	public String LetterAddForm(HttpSession session) {
		return "article/addLetterForm";
	}

	/**
	 * 편지 쓰기
	 */
	@PostMapping("/letter/add")
	public String letterAdd(Letter letter,
			@SessionAttribute("MEMBER") Member member) {
		letter.setSenderId(member.getMemberId());
		letter.setName(member.getName());
		letterDao.addArticle(article);
		return "redirect:/app/article/list";
	}

	/**
	 * 글 삭제
	 */
	@GetMapping("/letter/delete")
	public String delete(@RequestParam("letterId") String letterId,
			@SessionAttribute("MEMBER") Member member) {
		int updatedRows = letterDao.deleteLetter(letterId,
				member.getMemberId());

		// 권한 체크 : 글이 삭제되었는지 확인
		if (updatedRows == 0)
			// 글이 삭제되지 않음. 자신이 쓴 글이 아님
			throw new RuntimeException("No Authority!");

		logger.debug("글을 삭제했습니다. articleId={}", letterId);
		return "redirect:/app/letter/receivelist";
	}

}
