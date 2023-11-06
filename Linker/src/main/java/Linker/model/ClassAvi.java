package Linker.model;

import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ClassAvi {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int classAviId;
	String className;
	String aviName;
	String aviFileName;
	int classCount;
	

	
	
}
