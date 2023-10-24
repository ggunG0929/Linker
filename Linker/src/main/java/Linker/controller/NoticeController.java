package Linker.controller;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import Linker.model.Notice;
import Linker.repository.NoticeRepository;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;



@Controller
@RequestMapping("notice")
public class NoticeController {
	@Resource
	NoticeRepository noticeRepository;
	// 공지사항 리스트
	@GetMapping("list")
	public String noticeList(
			HttpSession session, Model model,Pageable pageable,
			@RequestParam(name = "sortBy", defaultValue = "noticeRegDate") String sortBy,
			@RequestParam(name = "sortDirection", defaultValue = "desc") String sortDirection,
			@RequestParam(name = "type", defaultValue = "") String type,
			@RequestParam(name = "keyword", defaultValue = "") String keyword
			) {
		Sort sort = Sort.by(sortDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

		Page<Notice> notice;
		if (!keyword.isEmpty()) {
			// 선택한 검색 조건에 따라 검색 메서드 호출
			if ("title".equals(type)) {
				notice = noticeRepository.findByNoticeTitleContaining(keyword, pageable);
			} else if ("content".equals(type)) {
				notice = noticeRepository.findByNoticeContentContaining(keyword, pageable);
			} else if ("writer".equals(type)) {

				notice = noticeRepository.findByNoticeWriterContaining(keyword, pageable);
			} else if ("titleAndContent".equals(type)) {
				notice = noticeRepository.findByNoticeTitleContainingOrNoticeContentContaining(keyword,keyword, pageable);
			} else {
				notice = noticeRepository.findAll(pageable);
			} 
		} else { 
			notice = noticeRepository.findAll(pageable);
		}
		
		model.addAttribute("notice", notice);
		return "notice/list";
	}

	@GetMapping("write")
	public String noticeWrite(	) {
	 
		return "notice/write";
	}
	
	@PostMapping("write")
	public String noticeWriteReg(Notice notice	) {
	 	
		noticeRepository.save(notice);
		return "redirect:/notice/list";
	}
	
	@GetMapping("detail/{id}")
	public String noticeDetail(@PathVariable int id, Model model) {
		Notice notice = noticeRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
		notice.setNoticeCnt(notice.getNoticeCnt()+1);
		noticeRepository.save(notice);
		model.addAttribute("notice", notice);
		return "notice/detail";
	}
	
	@GetMapping("modify/{id}")
	public String noticeModify(@PathVariable int id, Model model) {
		Notice notice = noticeRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
		model.addAttribute("notice", notice);
		return "notice/modify";
	}
	
	@PostMapping("modify/{id}")
	public String noticeModifyReg(@PathVariable int id,Notice newnotice, Model model) {
		Notice notice = noticeRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
		notice.setNoticeTitle(newnotice.getNoticeTitle());
		notice.setNoticeContent(newnotice.getNoticeContent());
		noticeRepository.save(notice);
		return "redirect:/notice/detail/" + id;
	}
	
	@GetMapping("delete/{id}")
	public String noticeDelete(@PathVariable int id, Model model) {
		Notice notice = noticeRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
		noticeRepository.delete(notice);
		return "redirect:/notice/list";
	}
	
}
