package org.egov.vendor.util;

import org.egov.vendor.web.model.user.UserSearchRequest;

import lombok.Getter;

public class DuplicateUserNameException extends Exception {
	 private static final long serialVersionUID = -6903761146294214595L;
	    @Getter
	    private UserSearchRequest userSearchCriteria;

	    public DuplicateUserNameException(UserSearchRequest userSearchCriteria) {
	        this.userSearchCriteria = userSearchCriteria;
	    }
}
