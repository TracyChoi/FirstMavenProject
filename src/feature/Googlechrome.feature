Feature:Google finding

  @Demo1
  Scenario: Google finding
    Given User on cypressDoc page
    And User type 'Hello World'
    And User clicks first choice
    Then User check 'Write some text to a txt file' is shown
