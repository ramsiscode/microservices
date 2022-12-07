package com.ng.org.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ng.org.model.OrganizationType;
import com.ng.org.projections.orgtype.OrganizationTypeAliasAndIdProjection;
import com.ng.org.projections.orgtype.OrganizationTypeAndIdProjection;

@Repository
public interface IOrganizationTypeRepository extends JpaRepository<OrganizationType, Integer>{
	
	@Query("select new com.ng.org.model.OrganizationType(o.id,o.organizationType) from OrganizationType o")
	public List<OrganizationTypeAndIdProjection> getOrganizationTypeAndIdList();
	public List<OrganizationType> findAllByOrderByIdAsc();
	@Query("select new com.ng.org.model.OrganizationType(o.organizationAlias,o.id) from OrganizationType o")
	public List<OrganizationTypeAliasAndIdProjection> getOrganizationTypeAliasAndIdList();
	
}
