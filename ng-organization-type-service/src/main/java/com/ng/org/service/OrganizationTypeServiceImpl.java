package com.ng.org.service;

import java.util.List;
import java.util.Objects;
import javax.transaction.Transactional;
import com.ng.org.model.OrganizationType;
import com.ng.org.repository.IOrganizationTypeRepository;
import com.ng.org.dto.organizationtype.OrganizationTypeDto;
import com.ng.org.projections.orgtype.OrganizationTypeAndIdProjection;
import com.ng.org.projections.orgtype.OrganizationTypeAliasAndIdProjection;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@Transactional
public class OrganizationTypeServiceImpl implements IOrganizationTypeService {

	@Autowired
	private IOrganizationTypeRepository organizationTypeRepository;
	//Method:Get list of orgtype and ids
	@Override
	public List<OrganizationTypeAndIdProjection> getOrganizationTypeAndIdList() {
		return organizationTypeRepository.getOrganizationTypeAndIdList();
	}
	//Method:Save orgtype details
	@Override
	public String saveOrganizationTypeDetails(OrganizationTypeDto organizationTypeDto) {

		OrganizationType organizationType = new OrganizationType();

		organizationType.setOrganizationType(organizationTypeDto.getOrganizationType());
		organizationType.setOrganizationAlias(organizationTypeDto.getOrganizationAlias());
		organizationType.setCreatedBy(organizationTypeDto.getCreatedBy());

		organizationTypeRepository.save(organizationType);
		return "Organization Data Add Succefully.";
	}
	//Method:Update orgtype details
	@Override
	public String updateOrganizationDetails(int id, OrganizationTypeDto organizationTypeDto) {
		OrganizationType organizationType = organizationTypeRepository.getById(id);

		if (Objects.nonNull(organizationTypeDto.getOrganizationAlias())
				&& !"".equalsIgnoreCase(organizationTypeDto.getOrganizationAlias())) // check null & empty space
			organizationType.setOrganizationAlias(organizationTypeDto.getOrganizationAlias());

		if (Objects.nonNull(organizationTypeDto.getOrganizationType())
				&& !"".equalsIgnoreCase(organizationTypeDto.getOrganizationType()))
			organizationType.setOrganizationType(organizationTypeDto.getOrganizationType());
		organizationType.setCreatedBy(organizationTypeDto.getCreatedBy());
		organizationTypeRepository.save(organizationType);
		return "Data Updated Succefully.";
	}
	//Method:get OrgType object
	@Override
	public List<OrganizationType> getOrganizationTypeObjectList() {
		return organizationTypeRepository.findAllByOrderByIdAsc();
	}
	//Method:delete orgTpye NOT USED
	@Override
	public String deleteOrganizationTypeById(Integer id) {
		organizationTypeRepository.deleteById(id);
		return "Organization Deleted Succesfully";
	}
	//Method:get list of orgType and alias NOT USED
	@Override
	public List<OrganizationTypeAliasAndIdProjection> getOrganizationTypeAliasAndIdList() {
		
		return organizationTypeRepository.getOrganizationTypeAliasAndIdList();
	}
}
