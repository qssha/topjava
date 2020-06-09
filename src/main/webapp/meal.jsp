<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Add/update meal</title>
</head>
<body>

<form method="POST" action='meals' name="addMealForm">
    <input type="hidden"  name="id" value=${meal.id} >
    Meal time : <input type="datetime-local" name="dateTime"
        value="${meal.dateTime}" > <br />
    Description : <input type="text" name="description"
        value=${meal.description} > <br />
    Calories : <input type="text" name="calories"
                      value=${meal.calories} > <br />
    <input type="submit" value="Submit" >
</form>

</body>
</html>
