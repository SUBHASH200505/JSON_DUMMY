Feature: Products API Automation

# =========================================
# POST → ADD PRODUCT
# =========================================

Scenario Outline: Validate POST Products API
  Given I read products test data "<TestCaseID>"
  And I validate products precondition
  When I perform products API request
  Then I validate products expected result

Examples:
  | TestCaseID        |
  | TC_Products_01_01 |
  | TC_Products_01_02 |
  | TC_Products_01_03 |   
  | TC_Products_01_04 |   


# =========================================
# GET → PRODUCTS
# =========================================

Scenario Outline: Validate GET Products APIs
  Given I set products request "<method>" "<endpoint>"
  When I send products request
  Then I validate products status "<status>"

Examples:
  | method | endpoint    | status |
  | GET    | /products   | 200    |
  | GET    | /productss  | 404    |


# =========================================
# GET → SINGLE PRODUCT
# =========================================

Scenario Outline: Validate GET Single Product APIs
  Given I set products request "<method>" "<endpoint>"
  When I send products request
  Then I validate products status "<status>"

Examples:
  | method | endpoint        | status |
  | GET    | /products/1     | 200    |
  | GET    | /products/99999 | 404    |


# =========================================
# GET RESPONSE STRUCTURE
# =========================================

Scenario: Validate Products Response Structure
  Given I set products request "GET" "/products"
  When I send products request
  Then Response body should contain products array
  And Each product should have "id", "title", "price", "category"


# =========================================
# PUT → UPDATE PRODUCT (❌ INTENTIONAL FAIL)
# =========================================

Scenario: Update Product using Data Table
  Given I set products request "PUT" "/products/1"
  When I send products PUT request with body:
    | title            |
    | iPhone Galaxy +1 |
  Then I validate products status "400"   


# =========================================
# PATCH → PARTIAL UPDATE
# =========================================

Scenario: Partial Update Product
  Given I set products request "PATCH" "/products/1"
  When I send products PATCH request with body "Updated Product"
  Then I validate products status "200"


# =========================================
# NEGATIVE SCENARIO
# =========================================

Scenario: Update Product with Invalid ID
  Given I set products request "PUT" "/products/99999"
  When I send products PUT request with body:
    | title |
    | Test  |
  Then I validate products status "404"


# =========================================
# DELETE → SCENARIO OUTLINE
# =========================================

Scenario Outline: Delete Product API
  Given I set products request "DELETE" "/products/<id>"
  When I send products request
  Then I validate products status "<status>"

Examples:
  | id    | status |
  | 1     | 200    |
  | 99999 | 404    |


# =========================================
# DELETE RESPONSE VALIDATION
# =========================================

Scenario: Validate Delete Response Fields
  Given I set products request "DELETE" "/products/1"
  When I send products request
  Then Products response should contain "isDeleted"
  And Products response should contain "deletedOn"