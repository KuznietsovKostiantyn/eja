<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<html>
<body>

    <form:form method="POST" modelAttribute="student" action="addStudent">
        <table>
            <tr>
                <th>Add Student</th>
            </tr>
            <tr>
            <form:hidden path="id" />
                <td><form:label path="name">Name:</form:label></td>
                <td><form:input path="name" size="30" maxlength="30"></form:input></td>
            </tr>
            <tr>
                <td><form:label path="surname">Surname:</form:label></td>
                <td><form:input path="surname" size="30" maxlength="30"></form:input></td>
            </tr>
            <tr>
                <td><form:label path="personalNumber">Personal number:</form:label></td>
                <td><form:input path="personalNumber" size="30" maxlength="30"></form:input></td>
            </tr>
            <tr>
                <td><form:label path="age">Age:</form:label></td>
                <td><form:input path="age" size="30" maxlength="30"></form:input></td>
            </tr>
            <tr>
                <td><form:label path="email">Email:</form:label></td>
                <td><form:input path="email" size="30" maxlength="30"></form:input></td>
            </tr>
            <tr>
                <td><form:label path="studentFaculty">Faculty:</form:label></td>
                <td>
                    <form:select multiple="true" path="studentFaculty">
                        <c:choose>
                            <c:when test="${not empty faculties}">
                                <form:options itemLabel="name" items="${faculties}"/>
                            </c:when>
                            <c:otherwise>
                                <form:option value="" items="${null}" />
                            </c:otherwise>
                        </c:choose>
                    </form:select>
                </td>
            </tr>
            <tr>
                <td><form:label path="courses">Courses:</form:label></td>
                <td>
                    <form:select multiple="true" path="courses">
                        <c:choose>
                            <c:when test="${not empty courses}">
                                <form:options itemLabel="name" items="${courses}"/>
                            </c:when>
                            <c:otherwise>
                                <form:option value="" items="${null}" />
                            </c:otherwise>
                        </c:choose>
                    </form:select>
                </td>
            </tr>

        </table>
        <input type="submit" value="Submit">
    </form:form>

    <table border="1" cellpadding="5">
        <tr>
            <th>Name</th>
            <th>Surname</th>
            <th>Personal number</th>
            <th>Age</th>
            <th>Email</th>
            <th>Faculty</th>
            <th>Courses</th>
            <th>Edit</th>
            <th>Delete</th>
        </tr>
        <c:forEach var="student" items="${students}">

            <c:url var="editStudent" value="/editStudent">
                <c:param name="id" value="${student.id}" />
            </c:url>
            <c:url var="deleteStudent" value="/deleteStudent">
                <c:param name="id" value="${student.id}" />
            </c:url>
            <c:url var="studentCourses" value="/student-course/${student.id}">
            </c:url>
            <tr>
                <td>${student.name}</td>
                <td>${student.surname}</td>
                <td>${student.personalNumber}</td>
                <td>${student.age}</td>
                <td>${student.email}</td>
                <td>${student.studentFaculty.name}</td>
                <td>
                    <a href="${studentCourses}" >
                        <c:forEach var="course" items="${student.courses}">
                            <p>${course.name}&nbsp;</p>
                        </c:forEach>
                    </a>
                </td>
                <td><a href="${editStudent}">Edit</a></td>
                <td><a href="${deleteStudent}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
    <p></p>
    <p></p>
    <table>
        <tr>
            <td>
                <form action="/faculties" method="get">
                    <input type="submit" value="Faculties"/>
                </form>
            </td>
            <td>
                <form action="/courses" method="get">
                    <input type="submit" value="Courses"/>
                </form>
            </td>
        </tr>
    </table>
</body>
</html>
