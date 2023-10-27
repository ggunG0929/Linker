package Linker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Linker.model.LabMember;

public interface LabMemberRepository extends JpaRepository<LabMember, Integer> {
	// memberId로 검색하기
	List<LabMember> findByMemberId(String memberId);
	LabMember findByMemberIdAndLabId(String memberId, int labId);
}