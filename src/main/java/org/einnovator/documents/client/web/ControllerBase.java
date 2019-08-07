package org.einnovator.documents.client.web;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;


public class ControllerBase extends org.einnovator.util.web.ControllerBase {


	protected boolean isAllowed(Principal principal, boolean admin) {
		if (principal==null) {
			return false;
		}
		if (admin) {
			return isAdmin(principal);
		}
		return true;
	}

	protected boolean isAllowed(Principal principal, String owner, String app, String path) {
		if (principal == null) {
			return false;
		}
		String principalUser = principal.getName();
		if (principalUser.equals(owner)) {
			return true;
		}
		if (isAdmin(principal)) {
			return true;
		}
		return false;
	}

	protected boolean isAllowed(Principal principal, String owner) {
		if (principal == null) {
			return false;
		}
		String principalUser = principal.getName();
		if (principalUser.equals(owner)) {
			return true;
		}
		if (isAdmin(principal)) {
			return true;
		}
		return false;
	}

	protected void setupToken(Principal principal, Authentication authentication) {
	}

	
	protected boolean isAdmin(Principal principal) {
		return false;
	}

}
