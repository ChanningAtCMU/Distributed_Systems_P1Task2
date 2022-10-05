<%--
  Created by IntelliJ IDEA.
  User: Channing
  Date: 2022/9/23
  Time: 18:56
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title>State Information Result</title>
    </head>
        <body>
            <h1>State: <%= request.getParameter("stateName")%></h1>
            <br><br/>

            <h2>Population: <%= request.getAttribute("pop")%> </h2>
            <h2>Nickname: <%= request.getAttribute("nickname")%></h2>
            <br>
            <h2>Capital: <%= request.getAttribute("capital")%></h2>
            <br>
            <h2>State Song(s): <%= request.getAttribute("song")%></h2>
            <dd>
                <a href="" title=" "></a>
            </dd>
            <br>
            <h2>Flower:</h2>
            <img src= "<%= request.getAttribute("flower")%>" width="150">
            <h3>Credit: <%= request.getAttribute("flower")%></h3>
            <br>

            <h2>Flag:</h2>
            <img src= "<%= request.getAttribute("flag")%>" width="150">
            <h3>Credit: <%= request.getAttribute("flag")%></h3>
            <br>

            <%-- hit button below to get back to the original page--%>
            <button type="button" name="back" onclick="history.back()">Continue</button>
        </body>

</html>
