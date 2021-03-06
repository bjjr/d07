<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<!-- Listing grid -->
<display:table pagesize="5" class="displaytag"
	name="properties" requestURI="${requestURI}" id="row">
	<!-- Attributes -->
	<spring:message code="property.name" var="nameHeader" />
	<display:column property="name" title="${nameHeader}" sortable="true" />
	
	<!-- Action links -->
	<display:column>
	<a href="ingredient/nutritionist/remove.do?ingredientId=${ingredient.id}&propertyId=${row.id}"><spring:message code="property.remove"/></a>
	</display:column>
	
</display:table>