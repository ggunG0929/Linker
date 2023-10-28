package Linker.model;

import java.sql.Timestamp;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Member {
	@Id
    private String memberId;
    private String memberPw;
    private String memberName;
    private String memberEmail;
    private String memberPhone;
    private String memberAddress;
    private String memberAddressDetail;
    private int memberZipcode;
    private String memberProfile;
    private int memberType;
    private Timestamp memberRegDate;
    private boolean memberBlack;
    private boolean memberQuit;
    private Timestamp memberQuitDate;
}
