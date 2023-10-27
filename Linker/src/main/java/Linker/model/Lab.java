package Linker.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Lab {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int labId;
    private int labMemberId;	// ?
    private String labName;
    private int labPhone;
    private String labAddress;
    private String labAddressDetail;
    private int labZipcode;
    private String labIntro;
    private String labBusinessCode;
    private String labStamp;
    private String labLogo;
}
