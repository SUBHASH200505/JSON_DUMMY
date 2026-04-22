Feature: Products API Automation

# =========================================
# POST → ADD PRODUCT (SCENARIO OUTLINE)
# =========================================

Scenario Outline: Validate POST Products API
  Given I read products test data "<TestCaseID>"
  And I validate products precondition
  When I perform products API request
  Then I validate products expected result

Examples:
  | TestCaseID            |
  | TC_Products_01_01     |
  | TC_Products_01_02     |
  | TC_Products_01_03     |
  | TC_Products_01_04     |

# =========================================
# GET → ALL PRODUCTS
# =========================================

Scenario Outline: Validate GET Products APIs
  Given I set products request "<method>" "<endpoint>"
  When I send products request
  Then I validate products status "<status>"

Examples:
  | method | endpoint      | status |
  | GET    | /products     | 200    |
  | GET    | /products     | 200    |
  | GET    | /productss    | 404    |
  | POST   | /products     | 404    |

# =========================================
# GET → SINGLE PRODUCT
# =========================================

Scenario Outline: Validate GET Single Product APIs
  Given I set products request "<method>" "<endpoint>"
  When I send products request
  Then I validate products status "<status>"

Examples:
  | method | endpoint              | status |
  | GET    | /products/1           | 200    |
  | GET    | /products/1           | 200    |
  | GET    | /products/99999       | 404    |
  | GET    | /products/999999999   | 404    |

# =========================================
# GET → PRODUCT CATEGORIES
# =========================================

Scenario Outline: Validate Product Categories APIs
  Given I set products request "<method>" "<endpoint>"
  When I send products request
  Then I validate products status "<status>"

Examples:
  | method | endpoint               | status |
  | GET    | /products/categories   | 200    |
  | GET    | /products/categories   | 200    |
  | GET    | /products/categories   | 200    |
  | GET    | /wrong                 | 404    |

# =========================================
# GET RESPONSE STRUCTURE
# =========================================

Scenario: Validate Products Response Structure
  Given I set products request "GET" "/products"
  When I send products request
  Then Response body should contain products array
  And Each product should have "id", "title", "price", "category"

# =========================================
# PUT → UPDATE PRODUCT
# =========================================

Scenario: Update Product using Data Table
  Given I set products request "PUT" "/products/1"
  When I send PUT request with body:
    | title               |
    | iPhone Galaxy +1    |
  Then I validate products status "200"
  And Response body should contain "iPhone Galaxy +1"

# =========================================
# PATCH → PARTIAL UPDATE
# =========================================

Scenario: Partial Update Product
  Given I set products request "PATCH" "/products/1"
  When I send PATCH request with body "Partially updated product"
  Then I validate products status "200"
  And Response body should contain "Partially updated product"

# =========================================
# INVALID UPDATE
# =========================================

Scenario: Update Product with Invalid ID
  Given I set products request "PUT" "/products/99999"
  When I send PUT request with body:
    | title               |
    | iPhone Galaxy +1    |
  Then I validate products status "404"

Scenario: Update Product with Invalid Data
  Given I set products request "PUT" "/products/1"
  When I send PUT request with body:
    | price |
    | abc   |
  Then I validate products status "400"

# =========================================
# DELETE → NORMAL SCENARIOS
# =========================================

Scenario Outline: Delete Product API
  Given I set products request "DELETE" "/products/<id>"
  When I send products request
  Then I validate products status "<status>"

Examples:
  | id     | status |
  | 1      | 200    |
  | 1      | 200    |
  | 99999  | 404    |
  | 1      | 200    |

# =========================================
# DELETE RESPONSE VALIDATION
# =========================================

Scenario: Validate Delete Response Fields
  Given I set products request "DELETE" "/products/1"
  When I send products request
  Then Response body should contain "isDeleted"
  And Response body should contain "deletedOn"