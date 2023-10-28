package Linker.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class LabMember {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int labMemberId;
	
	private int labId;
	
	private String memberId;
	
	private int labMemberType;
	private int labMemberStatus;
}
