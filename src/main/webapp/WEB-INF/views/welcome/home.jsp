<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Home</title>
<link rel="stylesheet"
    href="${pageContext.request.contextPath}/resources/app/css/styles.css">
</head>
<body>
    <div id="wrapper">
        <h3>transaction token test</h3>
        <form:form action="${pageContext.request.contextPath}/token">
            <input type="submit" />
        </form:form>
        TransactionTokenValue=${requestScope["org.terasoluna.gfw.web.token.transaction.TransactionTokenInterceptor.NEXT_TOKEN"].getTokenString()}
    </div>
</body>
</html>
