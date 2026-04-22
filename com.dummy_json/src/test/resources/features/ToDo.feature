Feature: Todos API Automation

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


Scenario Outline: Validate GET Todos APIs
  Given I set todos request "<method>" "<endpoint>"
  When I send todos request
  Then I validate todos status "<status>"

Examples:
  | method | endpoint      | status |
  | GET    | /todos        | 200    |
  | GET    | /todos/1      | 200    |
  | GET    | /todos/2      | 200    |
  | GET    | /todos/9999   | 404    |
  | GET    | /todos/abc    | 404   |

Scenario Outline: Validate UPDATE Todos API
  Given I read todos test data "<TestCaseID>"
  And I validate todos precondition
  When I perform todos API request
  Then I validate todos expected result

Examples:
  | TestCaseID        |
  | TC_Todos_04_01    |
  | TC_Todos_04_02    |
  | TC_Todos_04_03    |
  | TC_Todos_04_04    |
  | TC_Todos_04_05    |


Scenario: Delete Todo - Valid ID
  Given I set todos request "DELETE" "/todos/1"
  When I send todos request
  Then I validate todos status "200"

Scenario: Delete Todo - Invalid ID
  Given I set todos request "DELETE" "/todos/9999"
  When I send todos request
  Then I validate todos status "404"

Scenario: Delete Todo - Invalid format
  Given I set todos request "DELETE" "/todos/abc"
  When I send todos request
  Then I validate todos status "404"

Scenario: Delete Todo - Repeat delete
  Given I set todos request "DELETE" "/todos/1"
  When I send todos request
  Then I validate todos status "200"

Scenario: Delete Todo - Another valid ID
  Given I set todos request "DELETE" "/todos/2"
  When I send todos request
  Then I validate todos status "200"