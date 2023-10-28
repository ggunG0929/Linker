package Linker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Linker.model.Lab;

public interface LabRepository extends JpaRepository<Lab, Integer> {
	// 학원id 리스트로 검색해서 학원정보 리스트로 가져오기
	List<Lab> findByLabIdIn(List<Integer> labIds);
}