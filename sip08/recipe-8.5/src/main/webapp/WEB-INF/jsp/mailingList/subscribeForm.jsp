<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:url var="subscribeUrl" value="/mailinglist/subscribe.html" />
<c:url var="unsubscribeUrl" value="/mailinglist/unsubscribe.html" />

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Subscribe to the mailing list</title>
	</head>
	<body>
		<div>
			<h1 style="display:inline">Subscribe to the mailing list</h1>
			<span style="margin-left:20px"><a href="${unsubscribeUrl}">Unsubscribe</a></span>
		</div>
					
		<c:if test="${not empty expired}">
			<div class="warning alert">
				Sorry, your previous subscription request has expired. To subscribe you will need to complete a new
				subscription request using the form below.
			</div>
		</c:if>
		<c:if test="${not empty failed}">
			<div class="warning alert">
				Sorry, we were unable to confirm your subscription. If you copied the URL from your confirmation e-mail
				into the browser, please make sure you copied the entire URL. Otherwise, you can complete a new
				subscription request using the form below.
			</div>
		</c:if>
					
		<p>To subscribe, please provide your name and e-mail address.</p>
		
		<%-- Specify the action explicitly to eliminate the expires param --%>
		<form:form cssClass="main" modelAttribute="subscriber" action="${subscribeUrl}">
			<form:errors path="*">
				<div class="warning alert"><spring:message code="error.global" /></div>
			</form:errors>
			
			<div class="panel grid">
				<div class="gridRow yui-gf">
					<div class="fieldLabel yui-u first">Your first name:</div>
					<div class="yui-u">
						<div><form:input path="firstName" cssClass="short" cssErrorClass="shot error" /></div>
						<form:errors path="email">
							<div class="errorMessage"><form:errors path="firstName" htmlEscape="false" /></div>
						</form:errors>
					</div>
				</div>
				<div class="gridRow yui-gf">
					<div class="fieldLabel yui-u first">Your last name:</div>
					<div class="yui-u">
						<div><form:input path="lastName" cssClass="short" cssErrorClass="shot error" /></div>
						<form:errors path="email">
							<div class="errorMessage"><form:errors path="lastName" htmlEscape="false" /></div>
						</form:errors>
					</div>
				</div>
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
