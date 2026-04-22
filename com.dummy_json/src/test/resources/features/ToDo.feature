Feature: Todos API Automation

# POST
Scenario Outline: Validate POST Todos API
  Given I read todos test data "<TestCaseID>"
  And I validate todos precondition
  When I perform todos API request
  Then I validate todos expected result

Examples:
  | TestCaseID        |
  | TC_Todos_01_01    |
  | TC_Todos_01_02    |
  | TC_Todos_01_03    |
  | TC_Todos_01_04    |
  | TC_Todos_01_05    |


# GET
Scenario Outline: Validate GET Todos APIs
  Given I set todos request "<method>" "<endpoint>"
  When I send todos request
  Then I validate todos status "<status>"

Examples:
  | method | endpoint      | status |
  | GET    | /todos        | 200    |
  | GET    | /todos/1      | 200    |
  | GET    | /todos/9999   | 404    |
  | GET    | /todos/abc    | 404    |


# STRUCTURE
Scenario: Validate Todos Response Structure
  Given I set todos request "GET" "/todos"
  When I send todos request
  Then Response body should contain todos array
  And Each todo should have "id", "todo", "completed", "userId"


# PUT
Scenario: Update Todo using Data Table
  Given I set todos request "PUT" "/todos/1"
  When I send PUT request with body:
    | todo                | completed | userId |
    | Updated task value | true      | 5      |
  Then I validate todos status "200"
  And Response body should contain "Updated task value"


# PATCH
Scenario: Partial Update Todo
  Given I set todos request "PATCH" "/todos/1"
  When I send PATCH request with body "Partially updated task"
  Then I validate todos status "200"
  And Response body should contain "Partially updated task"


# INVALID UPDATE
Scenario: Update Todo with Invalid ID
  Given I set todos request "PUT" "/todos/9999"
  When I send PUT request with body:
    | todo           | completed | userId |
    | Invalid update | false     | 1      |
  Then I validate todos status "404"


# DELETE
Scenario Outline: Delete Todo API
  Given I set todos request "DELETE" "/todos/<id>"
  When I send todos request
  Then I validate todos status "<status>"

Examples:
  | id   | status |
  | 1    | 200    |
  | 2    | 200    |
  | 9999 | 404    |
  | abc  | 404    |


# DELETE VALIDATION
Scenario: Validate Delete Response Fields
  Given I set todos request "DELETE" "/todos/1"
  When I send todos request
  Then Response body should contain "isDeleted"
  And Response body should contain "deletedOn"