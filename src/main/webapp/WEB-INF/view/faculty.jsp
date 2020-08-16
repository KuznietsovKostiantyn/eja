<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<html>
<body>

<form:form method="POST" modelAttribute="faculty" action="addFaculty">
    <table>
        <tr>
            <th>Add Faculty</th>
        </tr>
        <tr>
            <form:hidden path="id" />
            <td><form:label path="name">Name:</form:label></td>
            <td><form:input path="name" size="30" maxlength="30"></form:input></td>
        </tr>
        <tr>
            <td><form:label path="facultyAbbr">Abbreviation:</form:label></td>
            <td><form:input path="facultyAbbr" size="30" maxlength="30"></form:input></td>
        </tr>
    </table>
    <input type="submit" value="Submit">
</form:form>

<table border="1" cellpadding="5">
    <tr>
        <th>Name</th>
        <th>Abbreviation</th>
        <th>Delete</th>
        <th>Students</th>
        <th>Courses</th>
    </tr>
    <c:forEach var="faculty" items="${faculties}">

        <c:url var="deleteFaculty" value="/deleteFaculty/${faculty.id}"/>
        <c:url var="facultyCourses" value="/faculty-courses/${faculty.id}"/>
        <tr>
            <td>${faculty.name}</td>
            <td>${faculty.facultyAbbr}</td>
            <td><a href="${deleteFaculty}">Delete</a></td>
            <td>
                <c:forEach var="student" items="${faculty.students}">
                    <p>${student.name}&nbsp;</p>
                </c:forEach>
            </td>
            <td>
                <a href="${facultyCourses}" >
                    <c:forEach var="course" items="${faculty.courses}">
                        <p>${course.name}&nbsp;</p>
                    </c:forEach>
                </a>
            </td>
        </tr>
    </c:forEach>
</table>
<form action="/" method="get">
    <input type="submit" value="Back"/>
</form>
</body>
</html>

