package com.ng.org.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ng.org.dto.ResponseDto;
import com.ng.org.dto.organizationtype.OrganizationTypeDto;
import com.ng.org.service.IOrganizationTypeService;
@RestController
@RequestMapping("/organizationtype")
public class OrganizationTypeController {
	
	@Autowired
	private IOrganizationTypeService iOrganizationTypeService;

	@GetMapping(value = "/getorganizationtype" )
	public ResponseEntity<?> getOrganizationTypeList(){		
		return new ResponseEntity<>( iOrganizationTypeService.getOrganizationTypeAndIdList(),HttpStatus.OK);		
	}

	@GetMapping(value="/getorganizationtypelist")
	public ResponseEntity<?> getOrganizationTypeObjectList(){
		return new ResponseEntity<>(  iOrganizationTypeService.getOrganizationTypeObjectList(),HttpStatus.OK);
		
	}

	@GetMapping(value="/getorganizationtypealiaslist")
	public ResponseEntity<?> getOrganizationTypeAliasList(){
		return new ResponseEntity<>(  iOrganizationTypeService.getOrganizationTypeAliasAndIdList(),HttpStatus.OK);
		
	}

	@PostMapping(value="/saveorganizationtypedetails")
	public ResponseEntity<?> saveOrganizationTypeDetails(@RequestBody OrganizationTypeDto organizationTypeDto){
		return new ResponseEntity<>(new ResponseDto(iOrganizationTypeService.saveOrganizationTypeDetails(organizationTypeDto)),HttpStatus.OK);		
	}

	@PutMapping(value="/updateorganizationtypedata/{id}")
	public ResponseEntity<?> updateOrganizationTypeDetails(@PathVariable("id") int id, @RequestBody OrganizationTypeDto organizationTypeDto){
		return new ResponseEntity<>(new ResponseDto( iOrganizationTypeService.updateOrganizationDetails(id,organizationTypeDto)), HttpStatus.OK);
		
	}

	@DeleteMapping(value="/deleteorganizationtype/{id}")
	public ResponseEntity<?> deleteOrganizationTypeById(@PathVariable("id") int id){
		return new ResponseEntity<>(new ResponseDto(iOrganizationTypeService.deleteOrganizationTypeById(id) ), HttpStatus.OK);
		
	}
	
}
