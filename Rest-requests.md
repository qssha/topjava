### GET request for all meals
curl -X GET 'http://localhost:8080/topjava/rest/meals'

### GET request for all meals between localDates and localTimes
curl -X GET 'http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30&startTime=11:00:00&endDate=2020-01-31&endTime=15:00:00'

### GET request for all meals between localDates
curl -X GET 'http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30&endDate=2020-01-30'

### DELETE request for meal with id=100007
curl -X DELETE 'http://localhost:8080/topjava/rest/meals/100007'

### GET request for meal with id=100007
curl -X GET 'http://localhost:8080/topjava/rest/meals/100007'

### GET request for meal with id=100005
curl -X GET 'http://localhost:8080/topjava/rest/meals/100005'

### POST request with new meal parameters as json
curl -X POST -d '{"dateTime": "2011-12-03T10:15:30", "description": "Тестовая еда", "calories": 1500}' -H 'Content-Type: application/json' http://localhost:8080/topjava/rest/meals


### PUT request with new meal parametres and id for existing meal
curl -X PUT -d '{"id": 100005, "dateTime": "2011-12-03T10:15:30", "description": "Тестовая еда", "calories": 1500}' -H 'Content-Type: application/json' http://localhost:8080/topjava/rest/meals