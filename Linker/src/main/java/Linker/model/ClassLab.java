package Linker.model;

import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Data
@Table(name ="class_lab")
public class ClassLab {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int classId;
	String className;
	Timestamp classStartDate; 
	Timestamp classEndDate;
	int	classCount; 
	int	classOnOff;
	int	classTuition;
	String classSyllabus;
	int classRank; 
	String classSubject;
	int classDiscount;  
	String classContents;
	int classPersonnel; 
	int classPeriodStatus;


	
}
