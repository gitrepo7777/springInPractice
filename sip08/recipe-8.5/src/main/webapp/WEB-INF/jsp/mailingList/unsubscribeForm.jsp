<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:url var="unsubscribeUrl" value="/mailinglist/unsubscribe.html" />

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Unsubscribe from the mailing list</title>
	</head>
	<body>
		<h1>Unsubscribe from the mailing list</h1>
		
		<c:if test="${not empty expired}">
			<div class="warning alert">
				Sorry, your previous unsubscription request has expired. To unsubscribe you will need to complete a new
				unsubscription request using the form below.
			</div>
		</c:if>
		<c:if test="${not empty failed}">
			<div class="warning alert">
				Sorry, we were unable to confirm your unsubscription. If you copied the URL from your confirmation
				e-mail into the browser, please make sure you copied the entire URL. Otherwise, you can complete a new
				unsubscription request using the form below.
			</div>
		</c:if>
		
		<p>To unsubscribe, please provide your e-mail address.</p>
		
		<%-- Specify the action explicitly to eliminate the expires param --%>
		<form:form class="main" modelAttribute="unsubscriber" action="${unsubscribeUrl}">
			<form:errors path="*">
				<div class="warning alert"><spring:message code="error.global" /></div>
			</form:errors>
			<div class="panel grid">
				<div class="gridRow yui-gf">
					<div class="fieldLabel yui-u first">Your e-mail address:</div>
					<div class="yui-u">
						<div><form:input path="email" cssClass="medium" cssErrorClass="medium error" /></div>
						<form:errors path="email">
							<div class="errorMessage"><form:errors path="email" htmlEscape="false" /></div>
						</form:errors>
					</div>
				</div>
				<div class="gridRow yui-gf">
					<div class="yui-u first"></div>
					<div class="yui-u"><input type="submit" value="Submit"></input></div>
				</div>
			</div>
		</form:form>
	</body>
</html>
