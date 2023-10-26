package Linker.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import Linker.model.Member;
import Linker.repository.MemberRepository;
import Linker.service.MailService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("member")
public class MemberController {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private MailService mailS;

	// 회원가입 - ajax
	@PostMapping("/idchk")
	@ResponseBody
	public String idchk(@RequestParam("memberId") String memberId) {
		Optional<Member> idchk = memberRepository.findByMemberId(memberId);
		return idchk.isPresent() ? "존재" : "없음";
	}

	// 회원가입 - ajax
	@PostMapping("/mail")
	@ResponseBody
	int joinConfirmMailSend(String memberEmail) {
		String to = memberEmail; // 받는사람 메일주소
		String subject = "[링커] 회원가입을 위한 인증메일입니다."; // 메일제목
		int number = (int) (Math.random() * 1000000); // 6자리 인증번호 생성
		String text = "\n 링커의 회원가입절차를 진행해주셔서 감사합니다. \n\n 회원가입 인증번호입니다. > " + number
				+ " \n\n 링커 회원가입 페이지로 돌아가 인증번호를 입력하고 회원가입을 완료해주세요. \n\n 인증번호는 5분간만 유효합니다 \n\n  - 당신을 기다리는 링커관리자 올림"; // 메일내용
		// 메일발송(수신자, 제목, 내용+인증번호)
		mailS.sendEmail(to, subject, text);
		// 생성된 인증번호 - 뷰에서 회원의 입력값과 비교
		return number;
	}

	@GetMapping("/join")
	public String addMemberForm(Model model) {
		model.addAttribute("member", new Member());
		return "member/join";
	}

	@PostMapping("/join")
	public String addMemberReg(@ModelAttribute Member member) {
		memberRepository.save(member);
		return "redirect:/member/login";
	}

	@GetMapping("/login")
	public String loginMemberForm(HttpSession session, Model model) {
	    Member loginMember = (Member) session.getAttribute("loginMember");
	    if (loginMember != null) {
	        model.addAttribute("member", loginMember);
	    }
	    return "member/login";
	}

	// 로그인 - ajax
	@PostMapping("/login")
	@ResponseBody
	public Map<String, String> loginMemberReg(@RequestParam("memberId") String memberId,
			@RequestParam("memberPw") String memberPw, HttpSession session) {
		Map<String, String> response = new HashMap<>();
		Optional<Member> match = memberRepository.findByMemberIdAndMemberPw(memberId, memberPw);
		if (match.isPresent()) {
			Member member = match.get();
			session.setAttribute("loginMember", member);
			System.out.println(member);
			response.put("success", "true");
		} else {
			response.put("success", "false");
		}
		return response;
	}

	@RequestMapping("/logout")
	String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/member/login";
	}
	
	@RequestMapping("/mypage")
	public String mypage() {
		return "member/mypage";
	}
}
