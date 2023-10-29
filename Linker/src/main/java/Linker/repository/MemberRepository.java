package Linker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Linker.model.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
}