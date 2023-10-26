package Linker.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "board_file")
@Data
public class File {

	@Id
	@Column(name = "board_file_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "file_name", length = 150)
	private String fileName;
	
	@Column(name = "member_id", length = 150)
	private String memberId;
}
