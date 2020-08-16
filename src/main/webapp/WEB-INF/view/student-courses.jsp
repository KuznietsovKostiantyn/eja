<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<head>
    <title>Student courses</title>
</head>
<body>
<h1>Student ${student.name} courses</h1>
<table border="1" cellpadding="5">
    <tr>
        <th>Name</th>
        <th>Abbreviation</th>
        <th>Delete</th>
    </tr>
    <c:forEach var="course" items="${courses}">
        <c:url var="deleteCourse" value="/deleteStudentFromCourse">
            <c:param name="student_id" value="${student.id}" />
            <c:param name="course_id" value="${course.id}" />
        </c:url>
        <tr>
            <td>${course.name}</td>
            <td>${course.courseAbbr}</td>
            <td><a href="${deleteCourse}">Delete</a></td>
        </tr>
    </c:forEach>
</table>
<h2>Add another course</h2>
<table border="1" cellpadding="5">
    <tr>
        <th>Name</th>
        <th>Abbreviation</th>
        <th>Add</th>
    </tr>
    <c:forEach var="course" items="${remain}">
        <c:url var="addCourse" value="/student-course/${student.id}/${course.id}"/>
        <tr>
            <td>${course.name}</td>
            <td>${course.courseAbbr}</td>
            <td><a href="${addCourse}">Add</a></td>
        </tr>
    </c:forEach>
</table>
</body>
<form action="/" method="get">
    <input type="submit" value="Back"/>
</form>
</html>
