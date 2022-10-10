Feature: Weatherstack API tests

  @positive
  Scenario Template: Comparing GET "/current" response parameters with expected values
    Given  generate the expected data for "<City>"
    When get data about the current weather in "<City>" from the Weatherstack
    Then compare the data received from the service with the expected data
    Examples:
      | City      |
      | Peterburg |
      | Moscow    |
      | Tbilisi   |
      | Ryazan    |

  @negative
  Scenario: Getting error 101 - missing_access_key
    When Weatherstack API called without an access key
    Then received and validated error 101

  @negative
  Scenario: Getting error 103 - invalid_api_function
    When Weatherstack API called with incorrect path
    Then received and validated error 103

  @negative
  Scenario: Getting error 601 - missing_query
    When Weatherstack API called without query parameter
    Then received and validated error 601

  @negative
  Scenario: Getting error 615 - request_failed
    When Weatherstack API called for wrong city
    Then received and validated error 615