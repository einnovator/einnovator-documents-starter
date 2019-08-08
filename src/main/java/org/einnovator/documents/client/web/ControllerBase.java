package org.einnovator.documents.client.web;

import java.security.Principal;

import org.einnovator.util.security.UserTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;


public class ControllerBase extends org.einnovator.util.web.ControllerBase {


	@Autowired(required=false)
	private UserTokenProvider tokenProvider;
	
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
		if (tokenProvider!=null) {
			tokenProvider.setupToken();
		}
	}

	
	protected boolean isAdmin(Principal principal) {
		return false;
	}

}
