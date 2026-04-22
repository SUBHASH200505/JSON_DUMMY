# ---------------------------------------------------------
# Author : Kishore
# Module : Recipes
# Description : Excel Driven Recipes Testing
# ---------------------------------------------------------

Feature: Recipes API Automation

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