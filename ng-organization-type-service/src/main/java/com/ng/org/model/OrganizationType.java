package com.ng.org.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrganizationType extends BaseEntity {
	@Column(unique = true,nullable = false)
	private String organizationType;
	private String organizationAlias;
	private String createdBy;
	@CreationTimestamp
	private LocalDateTime creationTime;

	public OrganizationType(int id, String organizationType) {
		super(id);
		this.organizationType = organizationType;
	}

	public OrganizationType(String organizationAlias, int id) {
		super(id);
		this.organizationAlias = organizationAlias;
	}

}
