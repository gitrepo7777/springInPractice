<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:url var="contactUrl" value="/contact.html" />

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Contact Us</title>
	</head>
	<body>
		<h1>Contact Us</h1>
		
		<p>Please use the form below to contact us with ideas, issues, questions, general feedback or anything else.
		We typically respond within one business day.</p>
		
		<p>All fields are required.</p>
		
		<form:form cssClass="main" action="${contactUrl}" modelAttribute="userMessage">
			<form:errors path="*">
				<div class="warning alert"><spring:message code="error.global" /></div>
			</form:errors>
			
			<form:hidden path="referer"/>
			
			<div class="panel grid">
				<div class="gridRow yui-gf">
					<div class="fieldLabel yui-u first">Your name:</div>
					<div class="yui-u">
						<div><form:input path="name" cssClass="medium" cssErrorClass="medium error" /></div>
						<form:errors path="name">
							<div class="errorMessage"><form:errors path="name" htmlEscape="false" /></div>
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
					<div class="fieldLabel yui-u first">Your message:</div>
					<div class="yui-u">
						<div><form:textarea path="text" cssErrorClass="error" /></div>
						<form:errors path="text">
							<div class="errorMessage"><form:errors path="text" htmlEscape="false" /></div>
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
