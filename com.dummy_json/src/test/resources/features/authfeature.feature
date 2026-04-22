Feature: Auth API Automation

# =========================================
# LOGIN API (ONLY VALID CASES)
# =========================================
Scenario Outline: Validate Login API using Excel Data

  Given I read auth test data "<TestCaseID>"
  And I validate auth precondition
  When I perform auth API request
  Then I validate auth expected result

Examples:
  | TestCaseID     |
  | TC_Auth_01_01  |
  | TC_Auth_01_02  |
  | TC_Auth_01_03  |
  | TC_Auth_01_04  |


# =========================================
# GET AUTH USER (NO TOKEN → 401 PASS)
# =========================================
Scenario: Validate Get Auth User API

  Given I set auth request "GET" "/auth/me"
  When I send auth request
  Then I validate auth status "401"


# =========================================
# STRUCTURE VALIDATION (LOGIN SUCCESS)
# =========================================
Scenario: Validate Auth Response Structure

  Given I set auth request "POST" "/auth/login"
  When I send auth request
  Then I validate auth status "200"
  And Response body should contain "accessToken"
  And Response body should contain "refreshToken"
  And Response body should contain "id"
  And Response body should contain "username"