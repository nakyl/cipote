<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<jsp:include page="header.jsp" />


<c:forEach items="${registroEditado}" var="item">
    ${item.valid} - ${item.coin} - ${item.exchange} <br /> 
</c:forEach>
<jsp:include page="footer.jsp" />
