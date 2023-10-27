package Linker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Linker.model.LabMember;

public interface LabMemberRepository extends JpaRepository<LabMember, Integer> {
	// memberId로 검색하기
	List<LabMember> findAllByMemberId(String memberId);
	List<LabMember> findAllByLabId(int labId);
	LabMember findByMemberIdAndLabId(String memberId, int labId);
}