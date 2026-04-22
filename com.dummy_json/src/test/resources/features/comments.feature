Feature: Comments API Automation

# =========================================
# POST → ADD COMMENT
# =========================================

Scenario Outline: Validate POST Comments API
  Given I read comments test data "<TestCaseID>"
  And I validate comments precondition
  When I perform comments API request
  Then I validate comments expected result

Examples:
  | TestCaseID        |
  | TC_Comments_01_01 |
  | TC_Comments_01_02 |


# =========================================
# GET → COMMENTS
# =========================================

Scenario Outline: Validate GET Comments APIs
  Given I set comments request "<method>" "<endpoint>"
  When I send comments request
  Then I validate comments status "<status>"

Examples:
  | method | endpoint        | status |
  | GET    | /comments       | 200    |
  | GET    | /comments/1     | 200    |
  | GET    | /comments/2     | 200    |
  | GET    | /comments/9999  | 404    |
  | GET    | /comments123    | 404    |


# =========================================
# RESPONSE STRUCTURE
# =========================================

Scenario: Validate Comments Response Structure
  Given I set comments request "GET" "/comments"
  When I send comments request
  Then Response body should contain comments array
  And Each comment should have "id", "body", "postId", "user"


# =========================================
# PUT → UPDATE COMMENT
# =========================================

Scenario: Update Comment using Data Table
  Given I set comments request "PUT" "/comments/1"
  When I send comments PUT request with body:
    | body                     | postId | userId |
    | Updated comment content | 3      | 5      |
  Then I validate comments status "200"
  And Comments response should contain "Updated comment content"


# =========================================
# PATCH → PARTIAL UPDATE
# =========================================

Scenario: Partial Update Comment
  Given I set comments request "PATCH" "/comments/1"
  When I send comments PATCH request with body "Partially updated comment"
  Then I validate comments status "200"
  And Comments response should contain "Partially updated comment"


# =========================================
# NEGATIVE SCENARIO
# =========================================

Scenario: Update Comment with Invalid ID
  Given I set comments request "PUT" "/comments/9999"
  When I send comments PUT request with body:
    | body           | postId | userId |
    | Invalid update | 4      | 1      |
  Then I validate comments status "404"


# =========================================
# DELETE → SCENARIO OUTLINE
# =========================================

Scenario Outline: Delete Comment API
  Given I set comments request "DELETE" "/comments/<id>"
  When I send comments request
  Then I validate comments status "<status>"

Examples:
  | id   | status |
  | 1    | 200    |
  | 2    | 200    |
  | 9999 | 404    |
  | abc  | 404    |


# =========================================
# DELETE RESPONSE VALIDATION
# =========================================

Scenario: Validate Delete Response Fields
  Given I set comments request "DELETE" "/comments/1"
  When I send comments request
  Then Comments response should contain "isDeleted"
  And Comments response should contain "deletedOn"