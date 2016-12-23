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

        <table>
            <tr>
                <td>no</td>
                <td>token name</td>
                <td>token key</td>
                <td>token value</td>
                <td>session id</td>
                <td>created at</td>
            </tr>
            <c:forEach var="token" items="${tokens}" varStatus="status">
                <tr>
                    <td>${status.count}</td>
                    <td>${token.tokenName}</td>
                    <td>${token.tokenKey}</td>
                    <td>${token.tokenValue}</td>
                    <td>${token.sessionId}</td>
                    <td>${token.createdAt}</td>
                </tr>
            </c:forEach>
        </table>
    </div>
</body>
</html>
