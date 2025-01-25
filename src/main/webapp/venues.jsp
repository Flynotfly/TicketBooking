<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="com.example.entities.Venue" %>
<%@ page import="com.example.beans.LoginBean" %>

<%
    // Restrict access to admins
    LoginBean loginBean = (LoginBean) session.getAttribute("loginBean");
    if (loginBean == null) {
        response.sendRedirect("login.xhtml");
        return;
    }
    
    if (loginBean.getLoggedInUser() == null) {
        response.sendRedirect("login.xhtml");
        return;
    }
    
    if (loginBean.getLoggedInUser().getRole() != com.example.entities.User.Role.ADMIN) {
        response.sendRedirect("login.xhtml");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Manage Venues</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }

        th, td {
            padding: 12px;
            border: 1px solid #ddd;
            text-align: left;
        }

        th {
            background-color: #f4f4f4;
        }
    </style>
</head>
<body>
    <h1>Manage Venues</h1>

    <!-- Venue List -->
    <table>
        <thead>
            <tr>
                <th>Name</th>
                <th>Place</th>
                <th>Date and time</th>
                <th>Total Tickets</th>
                <th>Booked Tickets</th>
                <th>Free Tickets</th>
                <th>Category</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="venue" items="${venues}">
                <tr>
                    <td>${venue.name}</td>
                    <td>${venue.place}</td>
                    <td>${venue.formattedDateTime}</td>
                    <td>${venue.quantityOfTickets}</td>
                    <td>${venue.bookedTickets}</td>
                    <td>${venue.freeTickets}</td>
                    <td>${venue.category.name}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>