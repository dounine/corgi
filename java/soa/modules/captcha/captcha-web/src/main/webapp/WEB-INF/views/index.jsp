<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<HTML xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>captcha Home</title>
		<script src="${C}/public/js/lib/jquery/jquery.js" ></script>
	</head>
	<body>
	<script>
		function jsonpCallback(data){
			console.info(data);
		}
		$.ajax({
			url:"checkNeedCaptcha",
			dataType:'jsonp',
			type:'POST',
			data:'callback=jsonpCallback',
			jsonp:'jsonpCallback',
			timeout:3000
		});
	</script>
	come on~~
	</body>
</html>