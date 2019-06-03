<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html>
<head>
<base href="${pageContext.request.contextPath }/" />
<title>받은 편지</title>
<script type="text/javascript">
	function confirmDelete() {
		if (confirm("삭제하시겠습니까?"))
			return true;
		else
			return false;
	}
</script>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/header.jsp"%>
	<h2>편지 보기</h2>
	<hr />
	<p>
		<span>${letter.letterId }</span> | <span style="font-weight: bold;">${letter.title }</span>
	</p>
	<p>
		보낸사람 : ${letter.senderName }
	</p>
	<p>
		받는사람 : ${letter.receiverName }
	</p>
	<p>
		작성 일시:${letter.cdate }
	</p>
	<hr />
	<p>내용: ${letter.contentHtml }</p>
	<hr />
	<p>
		<a href="./app/letter/delete?letterId=${letter.letterId }" onclick="return confirmDelete();">삭제</a> |
	</p>
</body>
</html>