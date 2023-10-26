package Linker.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Linker.model.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
	Optional<Member> findByMemberId(String memberId);
	Optional<Member> findByMemberIdAndMemberPw(String memberId, String memberPw);
}