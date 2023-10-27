package Linker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Linker.model.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
	// Optional = isPresent를 쓸 수 있고 get으로 받을 수 있음 null이 없음
}