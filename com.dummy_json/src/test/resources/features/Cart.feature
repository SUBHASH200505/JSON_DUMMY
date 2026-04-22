Feature: Carts API Automation

Scenario Outline: Validate POST Carts API
  Given I read carts test data "<TestCaseID>"
  And I validate carts precondition
  When I perform carts API request
  Then I validate carts expected result

Examples:
  | TestCaseID        |
  | TC_Carts_01_01    |
  | TC_Carts_01_02    |
  | TC_Carts_01_03    |
  | TC_Carts_01_04    |


Scenario Outline: Validate GET Carts APIs
  Given I set carts request "<method>" "<endpoint>"
  When I send carts request
  Then I validate carts status "<status>"

Examples:
  | method | endpoint              | status |
  | GET    | /carts                | 200    |
  | GET    | /carts/1              | 200    |
  | GET    | /carts/99999          | 404    |
  | GET    | /carts?limit=-5       | 404    |

Scenario Outline: Validate UPDATE Carts API
  Given I read carts test data "<TestCaseID>"
  And I validate carts precondition
  When I perform carts API request
  Then I validate carts expected result

Examples:
  | TestCaseID        |
  | TC_Carts_05_01    |
  | TC_Carts_05_02    |
  | TC_Carts_05_03    |
  | TC_Carts_05_04    |



Scenario: Delete Cart - Valid ID
  Given I set carts request "DELETE" "/carts/1"
  When I send carts request
  Then I validate carts status "200"

Scenario: Delete Cart - Invalid ID
  Given I set carts request "DELETE" "/carts/9999"
  When I send carts request
  Then I validate carts status "404"

Scenario: Delete Cart - Invalid format
  Given I set carts request "DELETE" "/carts/abc"
  When I send carts request
  Then I validate carts status "404"

Scenario: Delete Cart - Another valid ID
  Given I set carts request "DELETE" "/carts/2"
  When I send carts request
  Then I validate carts status "200"