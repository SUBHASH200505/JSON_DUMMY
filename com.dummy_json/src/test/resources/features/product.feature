Feature: Products API Automation

Scenario Outline: Validate POST Products API
  Given I read products test data "<TestCaseID>"
  And I validate products precondition
  When I perform products API request
  Then I validate products expected result

Examples:
  | TestCaseID         |
  | TC_Products_01_01  |
  | TC_Products_01_02  |
  | TC_Products_01_03  |
  | TC_Products_01_04  |



Scenario Outline: Validate GET Products APIs
  Given I set products request "<method>" "<endpoint>"
  When I send products request
  Then I validate products status "<status>"

Examples:
  | method | endpoint               | status |
  | GET    | /products              | 200    |
  | GET    | /products/1            | 200    |
  | GET    | /products/2            | 200    |
  | GET    | /products/99999        | 404    |
  | GET    | /products/abc          | 404    |
  


Scenario Outline: Validate UPDATE Products API
  Given I read products test data "<TestCaseID>"
  And I validate products precondition
  When I perform products API request
  Then I validate products expected result

Examples:
  | TestCaseID         |
  | TC_Products_05_01  |
  | TC_Products_05_02  |
  | TC_Products_05_03  |
  | TC_Products_05_04  |
  
  






Scenario: Delete Product - Valid ID
  Given I set products request "DELETE" "/products/1"
  When I send products request
  Then I validate products status "200"

Scenario: Delete Product - Invalid ID
  Given I set products request "DELETE" "/products/99999"
  When I send products request
  Then I validate products status "404"

Scenario: Delete Product - Invalid format
  Given I set products request "DELETE" "/products/abc"
  When I send products request
  Then I validate products status "404"

Scenario: Delete Product - Repeat delete
  Given I set products request "DELETE" "/products/1"
  When I send products request
  Then I validate products status "200"

Scenario: Delete Product - Another valid ID
  Given I set products request "DELETE" "/products/2"
  When I send products request
  Then I validate products status "200"
