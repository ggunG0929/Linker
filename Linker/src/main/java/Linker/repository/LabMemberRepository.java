package Linker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Linker.model.LabMember;

public interface LabMemberRepository extends JpaRepository<LabMember, Integer> {
	// memberId로 검색해서 리스트로 가져오기
	List<LabMember> findAllByMemberId(String memberId);
	// labId로 검색해서 리스트로 가져오기
	List<LabMember> findAllByLabId(int labId);
	// memberId와 labId로 검색
	LabMember findByMemberIdAndLabId(String memberId, int labId);
}