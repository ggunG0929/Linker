package Linker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Linker.model.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
	Member findByMemberId(String memberId);
	// 강사 목록 페이지에서 강사 이름 검색 시 찾을 회원
	List<Member> findByMemberNameLike(String memberName);
}