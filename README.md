# employeemanagement
Problem Statement:
 
UI Task
--------------------------------------------------------------------------------------------------------------------
1.      Create an UI which allow to upload csv file containing employee information
    a.      Employee csv contains the following columns: name, department, designation, salary, joining date
2.      Show the uploaded Employee information in a table.
3.      Table should have pagination capabilities.
4.      Any updates in the employee information should be reflected when refreshing the table or page.
5.      Provide link to download the file with error records

 
Server API Task
--------------------------------------------------------------------------------------------------------------------
1.      Expose service to consume uploaded file.
2.      Validation
3.      service to update the employee information

Validation
--------------------------------------------------------------------------------------------------------------------
Beside basic validation for the uploaded file.
Following are the Validation for each field:
Name -> can only contain alphabets
department -> alphanumeric with -_* as special characters
designation -> Developer, Senior Developer, Manager, Team Lead, VP, CEO
Salary -> can only contain Numeric value
joining date -> yyyy-MM-dd format
 
