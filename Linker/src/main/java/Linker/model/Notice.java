package Linker.model;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="notice")
@Data
public class Notice {
 
	@Id  
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int noticeId;
	@Column  
	String noticeWriter;
	String noticeTitle;
	String noticeContent; 
	@CreationTimestamp
	Timestamp noticeRegDate;
	@UpdateTimestamp
	Timestamp noticeModiDate; 
	int noticeCnt;;
}
 