package com.ng.org.projections.orgtype;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public interface OrganizationTypeAliasAndIdProjection {
	Integer getId();
	String getOrganizationAlias();
}
