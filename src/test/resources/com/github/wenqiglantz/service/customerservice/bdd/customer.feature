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


  Scenario: get a customer detail by customerId

    Given the collection of customers:
      | customerId                       | firstName | lastName |
      | ABCDEFG12345678910HIJKLMNOP12345 | John      | Smith    |
      | ABCDEFG12345678910HIJKLMNOP12346 | Jane      | Smith    |

    When customerId ABCDEFG12345678910HIJKLMNOP12345 is passed in to retrieve the customer details

    Then The customer detail is retrieved
      | customerId                       | firstName | lastName |
      | ABCDEFG12345678910HIJKLMNOP12345 | John      | Smith    |
