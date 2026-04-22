# ---------------------------------------------------------
# Author : Kishore
# Module : Recipes
# Description : Recipes API Testing with all Scenario Types
# ---------------------------------------------------------

Feature: Recipes API Automation

# =========================================================
# NORMAL SCENARIO
# =========================================================

Scenario: Get All Recipes (Normal Scenario)

    Given I set endpoint "/recipes"
    When I send GET request
    Then Response status code should be 200


# =========================================================
# SCENARIO OUTLINE
# =========================================================

Scenario Outline: Get Recipe by ID (Scenario Outline)

    Given I set endpoint "/recipes/<id>"
    When I send GET request
    Then Response status code should be <status>

Examples:

| id    | status |
| 1     | 200    |
| 9999  | 404    |
| abc   | 404    |


# =========================================================
# SCENARIO WITH DATA TABLE
# =========================================================

Scenario: Add Recipe using Data Table

    Given I set endpoint "/recipes/add"

    When I send POST request with body:

    | name        | ingredients                      | instructions                    |
    | Test Pizza  | Flour,Cheese,Tomato             | Prepare dough,Add toppings,Bake |

    Then Response status code should be 200


# =========================================================
# SCENARIO WITH EXCEL DATA
# =========================================================

Scenario Outline: Validate Recipes API using Excel Data

    Given I read test data "<TestCaseID>"
    And I validate precondition
    When I perform API request
    Then I validate expected result

Examples:

| TestCaseID |

| TC_Recipes_01_01 |
| TC_Recipes_01_02 |
| TC_Recipes_01_03 |
| TC_Recipes_01_04 |
| TC_Recipes_01_05 |
| TC_Recipes_01_06 |

| TC_Recipes_02_01 |
| TC_Recipes_02_05 |
| TC_Recipes_02_06 |

| TC_Recipes_03_01 |
| TC_Recipes_03_05 |
| TC_Recipes_03_06 |

| TC_Recipes_04_01 |
| TC_Recipes_04_02 |
| TC_Recipes_04_03 |
| TC_Recipes_04_04 |
| TC_Recipes_04_05 |
| TC_Recipes_04_06 |

| TC_Recipes_05_01 |
| TC_Recipes_05_05 |
| TC_Recipes_05_06 |

| TC_Recipes_06_01 |
| TC_Recipes_06_02 |
| TC_Recipes_06_03 |
| TC_Recipes_06_04 |
| TC_Recipes_06_05 |
| TC_Recipes_06_06 |