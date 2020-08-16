<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<html>
<body>

<form:form method="POST" modelAttribute="course" action="addCourse">
    <table>
        <tr>
            <th>Add Course</th>
        </tr>
        <tr>
            <form:hidden path="id" />
            <td><form:label path="name">Name:</form:label></td>
            <td><form:input path="name" size="30" maxlength="30"></form:input></td>
        </tr>
        <tr>
            <td><form:label path="courseAbbr">Abbreviation:</form:label></td>
            <td><form:input path="courseAbbr" size="30" maxlength="30"></form:input></td>
        </tr>
        <tr>
            <td>
                <form:label path="courseFaculty">Faculty:</form:label>
            </td>
            <td>
                <form:select path="courseFaculty">
                    <c:choose>
                        <c:when test="${not empty faculties}">
                            <form:options itemLabel="name" items="${faculties}" />
                        </c:when>
                        <c:otherwise>
                            <form:option value="" items="${null}" />
                        </c:otherwise>
                    </c:choose>
                </form:select>
            </td>
        </tr>
    </table>
    <input type="submit" value="submit">
</form:form>

<table border="1" cellpadding="5">
    <tr>
        <th>Name</th>
        <th>Abbreviation</th>
        <th>Faculty</th>
        <th>Edit</th>
        <th>Delete</th>
    </tr>
    <c:forEach var="course" items="${courses}">

        <c:url var="editCourse" value="/editCourse">
            <c:param name="id" value="${course.id}" />
        </c:url>
        <c:url var="deleteCourse" value="/deleteCourse">
            <c:param name="id" value="${course.id}" />
        </c:url>
        <tr>
            <td>${course.name}</td>
            <td>${course.courseAbbr}</td>
            <td>${course.courseFaculty.name}</td>
            <td><a href="${editCourse}">Edit</a></td>
            <td><a href="${deleteCourse}">Delete</a></td>
        </tr>
    </c:forEach>
</table>
<form action="/" method="get">
    <input type="submit" value="Back"/>
</form>
</body>
</html>

