Feature: Customer functionalities
  This feature contains a list of functionalities related to customer

  Scenario: retrieving records by tenant id 1
    Given two customer records are persisted in db
    When the user retrieves all records by passing tenantId 1
    Then Only 1 record for tenantId 1 is returned successfully

  Scenario: retrieving records by tenant id 2
    Given two customer records are persisted in db
    When the user retrieves all records by passing tenantId 2
    Then Only 1 record for tenantId 2 is returned successfully
