package Linker.repository;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import Linker.model.Notice;




public interface NoticeRepository extends JpaRepository<Notice, Integer> {

	//검색처리
	Page<Notice> findByNoticeTitleContaining(String keyword, Pageable pageable);
	Page<Notice> findByNoticeContentContaining(String keyword, Pageable pageable);
	Page<Notice> findByNoticeWriterContaining(String keyword, Pageable pageable);
	Page<Notice> findByNoticeTitleContainingOrNoticeContentContaining(String keyword,String keyword2, Pageable pageable);

}
