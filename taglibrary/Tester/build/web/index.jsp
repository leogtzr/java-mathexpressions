<%-- 
    Document   : index
    Created on : 23/02/2014, 06:26:17 PM
    Author     : Leo Gutierrez R. <leogutierrezramirez@gmail.com>
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="math" uri="mathparse" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>

        <jsp:scriptlet>
            request.setAttribute("expr", "2.0 + sin(pi + 0.2");
        </jsp:scriptlet>
        
        <math:parse expression="x^3" x="2" />
        <hr/>
        
        <math:parse expression="1.5 + pi^2" />
        <hr/>
        
        <math:parse>
            <jsp:attribute name="expression">
                1.5 + e^2
            </jsp:attribute>
        </math:parse>
        <hr/>
        
        <%-- 
            With JSTL ...
        --%>
        
        <c:catch var="result">
            <%-- The next expression has an error --%>
            <math:parse expression="3.34566 * sin(pi - 2" />
        </c:catch>
        
        <c:if test="${result ne null}">
            <h1>Error!</h1>
            <h2>${result.message}</h2>
        </c:if>
        
        
    </body>
</html>
