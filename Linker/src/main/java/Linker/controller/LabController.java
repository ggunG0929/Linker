package Linker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import Linker.model.Lab;
import Linker.model.LabMember;
import Linker.model.Member;
import Linker.repository.LabMemberRepository;
import Linker.repository.LabRepository;
import Linker.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("lab")
public class LabController {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private LabMemberRepository labMemberRepository;

	@Autowired
	private LabRepository labRepository;

	@GetMapping("/add")
	public String addLabForm(HttpSession session, Model model) {
//		Member sessionMember = (Member) session.getAttribute("sessionMember");
//		String sessionId = sessionMember.getMemberId();
		String sessionId = (String) session.getAttribute("sessionId");
		// 세션만료
		if (sessionId == null) {
			String msg = "로그인 세션이 만료되었습니다.";
			String goUrl = "/member/login";
			model.addAttribute("msg", msg);
			model.addAttribute("goUrl", goUrl);
			return "member/alert";
		}
		// 세션존재
		model.addAttribute("memberId", sessionId);
		return "lab/add";
	}

	// 파일저장처리, 유효성검사, 사업자번호 처리 해야함
	@PostMapping("/add")
	public String addLabReg(@ModelAttribute Lab lab, @ModelAttribute LabMember labMember, Model model) {
		labRepository.save(lab);
		labMember.setLabId(lab.getLabId());
		labMemberRepository.save(labMember);
		String msg = "링커에 학원이 개설되었습니다.";
		String goUrl = "/member/mylab";
		model.addAttribute("msg", msg);
		model.addAttribute("goUrl", goUrl);
		return "member/alert";
	}

	// 각 학원들 페이지
	@RequestMapping("/{labId}")
	String labMain(@PathVariable("labId") int labId, Model model, HttpSession session) {
		Member sessionMember = (Member) session.getAttribute("sessionMember");
//		String sessionId = (String) session.getAttribute("sessionId");
//		Member sessionMember = memberRepository.findById(sessionId).get();
		if(sessionMember != null) {
			model.addAttribute("sessionMember", sessionMember);
			LabMember sessionLabMember = labMemberRepository.findByMemberIdAndLabId(sessionMember.getMemberId(), labId);
			if(sessionLabMember != null) {
				model.addAttribute("sessionLabMember", sessionLabMember);
			}			
		}
		Lab lab = labRepository.findById(labId).get();
		model.addAttribute("lab", lab);
		return "lab/main";
	}

}
