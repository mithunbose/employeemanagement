package com.rakuten.test.employee.utils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author mithbose
 */
public final class ValidationUtils {

    private static final String ALPHABET_MATCHER = "[a-zA-Z ]+";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DEPARTMENT_MATCHER = "^[a-zA-Z0-9\\-\\_\\*]*$";
    private static final String VALID_DATE_FORMAT_MATCHER = "([0-9]{4})-([0-9]{2})-([0-9]{2})";
    private static final Set<String> DESIGNATION_SET = new HashSet<>(Arrays.asList("Developer",
            "Senior Developer", "Manager",
            "Team Lead", "VP", "CEO")
    );

    public static String validateInputs(String name, String department, String designationString, String salary, String joiningDate) {
        StringBuilder sb = new StringBuilder();
        boolean isValidName = validateName(name);
        if (!isValidName) {
            sb.append("Name is invalid.\n");
        }
        boolean isValidDept = validateDepartment(department);
        if (!isValidDept) {
            sb.append("Department is invalid.\n");
        }
        boolean isValidDesignation = validateDesigantion(designationString);
        if (!isValidDesignation) {
            sb.append("Designation is invalid.\n");
        }
        boolean isValidSalary = validateSalary(salary);
        if (!isValidSalary) {
            sb.append("Salary is invalid.\n");
        }
        boolean isValidJoiningDate = validateJoiningDate(joiningDate);
        if (!isValidJoiningDate) {
            sb.append("Joining date is invalid.\n");
        }
        return sb.toString();
    }

    private static boolean validateName(String name) {
        return name != null && name.matches(ALPHABET_MATCHER);
    }

    private static boolean validateDepartment(String department) {
        return department != null && department.matches(DEPARTMENT_MATCHER);
    }

    private static boolean validateDesigantion(String designationString) {
        if (designationString != null && DESIGNATION_SET.contains(designationString)) {
            return true;
        } else {
            return false;
        }

    }

    private static boolean validateSalary(String salary) {
        boolean valid = false;
        try {
            double value = Double.parseDouble(salary);
            if (value > 0) {
                valid = true;
            }
        } catch (NumberFormatException e) {
//            e.printStackTrace();
        }
        return valid;
    }

    private static boolean validateJoiningDate(String joiningDate) {
        boolean valid = false;
        try {
            if (joiningDate.matches(VALID_DATE_FORMAT_MATCHER)) {
                SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
                formatter.setLenient(false);
                Date date = formatter.parse(joiningDate);
                String formattedDate = formatter.format(date);
                valid = true;
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return valid;
    }

}
