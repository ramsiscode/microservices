package com.ng.org.service;

import java.util.List;

import com.ng.org.dto.organizationtype.OrganizationTypeDto;
import com.ng.org.model.OrganizationType;
import com.ng.org.projections.orgtype.OrganizationTypeAliasAndIdProjection;
import com.ng.org.projections.orgtype.OrganizationTypeAndIdProjection;

public interface IOrganizationTypeService {

	public List<OrganizationTypeAndIdProjection> getOrganizationTypeAndIdList();

	public String saveOrganizationTypeDetails(OrganizationTypeDto organizationTypeDto);

	public String updateOrganizationDetails(int id, OrganizationTypeDto organizationTypeDto);

	public List<OrganizationType> getOrganizationTypeObjectList();

	public String deleteOrganizationTypeById(Integer id);

	public List<OrganizationTypeAliasAndIdProjection> getOrganizationTypeAliasAndIdList();

}
