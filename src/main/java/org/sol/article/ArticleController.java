package org.sol.article;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.sol.book.chap11.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ArticleController {

	@Autowired
	ArticleDao articleDao;

	/**
	 * 글 목록
	 */
	@GetMapping("/article/list")
	public void articleList(
			@RequestParam(value = "page", defaultValue = "1") int page,
			Model model) {

		// 페이지당 행의 수와 페이지의 시작점
		final int COUNT = 100;
		int offset = (page - 1) * COUNT;

		List<Article> articleList = articleDao.listArticles(offset, COUNT);
		int totalCount = articleDao.getArticlesCount();
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("articleList", articleList);
	}

	/**
	 * 글 보기
	 */
	@GetMapping("/article/view")
	public void articleView(@RequestParam("articleId") String articleId,
			Model model) {
		Article article = articleDao.getArticle(articleId);
		model.addAttribute("article", article);
	}

	/**
	 * 글 등록 화면
	 */
	@GetMapping("/article/addForm")
	public String articleAddForm(HttpSession session) {
		// 세션에 MEMBER가 있는 지 확인
		Object memberObj = session.getAttribute("MEMBER");
		if (memberObj == null)
			// 세션에 MEMBER가 없으면 로그인 화면으로
			return "redirect:/app/loginForm";

		// 글쓰기 화면으로
		return "article/addForm";
	}

	/**
	 * 글 등록
	 */
	@PostMapping("/article/add")
	public String articleAdd(Article article, HttpSession session) {
		// 세션에 MEMBER가 있는 지 확인
		Object memberObj = session.getAttribute("MEMBER");
		if (memberObj == null)
			// 세션에 MEMBER가 없으면 로그인 화면으로
			return "redirect:/loginForm";

		// 아이디와 이름을 세션의 값으로 사용
		Member member = (Member) memberObj;
		article.setUserId(member.getMemberId());
		article.setName(member.getName());
		articleDao.addArticle(article);
		return "redirect:/app/article/list";
	}
}