package com.company.app.constants;

import java.util.Arrays;
import java.util.List;

public class GlobalConstants {

    //Jira status
    public static final String OPEN = "open";
    public static final String IN_PROGRESS = "In-Progress";
    public static final String TESTING = "Testing";
    public static final String IN_REVIEW = "In-Review";
    public static final String RESOLVED = "Resolved";

    //Lists
    public static final List<String> STATUS_LIST = Arrays.asList(GlobalConstants.OPEN,GlobalConstants.IN_PROGRESS,GlobalConstants.TESTING,GlobalConstants.IN_REVIEW,GlobalConstants.RESOLVED);


}
