package Linker.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import Linker.model.Lab;
import Linker.model.LabMember;
import Linker.model.Member;
import Linker.repository.LabMemberRepository;
import Linker.repository.LabRepository;
import Linker.repository.MemberRepository;
import Linker.service.MailService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("member")
public class MemberController {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private LabMemberRepository labMemberRepository;

	@Autowired
	private LabRepository labRepository;

	@Autowired
	private MailService mailService;

	// 회원가입 - ajax
	@PostMapping("/idchk")
	@ResponseBody
	public String idchk(@RequestParam("memberId") String memberId) {
		// Optional = isPresent를 쓸 수 있고 get으로 받을 수 있음 null이 없음
		Optional<Member> idchk = memberRepository.findById(memberId);
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
		mailService.sendEmail(to, subject, text);
		// 생성된 인증번호 - 뷰에서 회원의 입력값과 비교
		return number;
	}

	@GetMapping("/join")
	public String addMemberForm(Model model) {
		model.addAttribute("member", new Member());
		return "member/join";
	}

	@PostMapping("/join")
	public String addMemberReg(@ModelAttribute Member member, Model model) {
		// 등록일 설정 후 저장
		member.setMemberRegDate(new Timestamp(System.currentTimeMillis()));
		memberRepository.save(member);
		String msg = "링커에 가입해주셔서 감사합니다.\n로그인 후 서비스를 이용해주세요.";
		String goUrl = "/member/login";
		model.addAttribute("msg", msg);
		model.addAttribute("goUrl", goUrl);
		return "member/alert";
	}

	@GetMapping("/login")
	public String loginMemberForm(HttpSession session, Model model) {
		String sessionId = (String) session.getAttribute("sessionId");
		if (sessionId != null) {
			Member sessionMember = memberRepository.findById(sessionId).get();
//		Member sessionMember = (Member) session.getAttribute("sessionMember");
			if (sessionMember != null) {
				// 세션존재시 memberName띄우려고
				model.addAttribute("member", sessionMember);
				System.out.println("세션존재 :" + sessionMember);
			}
		}
		return "member/login";
	}

	// 로그인
	@PostMapping("/login")
	public String loginMemberReg(@RequestParam("memberId") String memberId, @RequestParam("memberPw") String memberPw,
			HttpSession session, Model model) {
		Member member = memberRepository.findById(memberId).get();
		String msg = "로그인에 실패했습니다.\n아이디와 비밀번호를 확인하세요.";
		String goUrl = "/member/login";
		if (member != null && member.getMemberPw().equals(memberPw)) {
			// 둘 중 하나만 쓸까? 둘 다 쓸까?
			// 왜 세션 멤버는 죽었는데 아이디는 남아있을까? - 세션id를 갖고와야 안전해서 id를 쓰는 게 나음
			session.setAttribute("sessionId", memberId);
			session.setAttribute("sessionMember", member);
//			System.out.println("로그인성공 :" + member);
			msg = "로그인에 성공했습니다.";
			goUrl = "/member/login";
		}
		model.addAttribute("msg", msg);
		model.addAttribute("goUrl", goUrl);
		return "member/alert";
	}

	@RequestMapping("/logout")
	String logout(HttpSession session, Model model) {
		session.invalidate();
		String msg = "로그아웃되었습니다.";
		String goUrl = "/";
		model.addAttribute("msg", msg);
		model.addAttribute("goUrl", goUrl);
		return "member/alert";
	}

	@RequestMapping("/mypage")
	public String mypage() {
		return "member/mypage";
	}

	// 내 강의실
	@RequestMapping("/mylab")
	public String mylab(HttpSession session, Model model) {
		String sessionId = (String) session.getAttribute("sessionId");
//		Member sessionMember = (Member) session.getAttribute("sessionMember");
		// 세션만료
		if (sessionId == null) {
//		if (sessionMember == null) {
			String msg = "로그인이 필요합니다.";
			String goUrl = "/member/login";
			model.addAttribute("msg", msg);
			model.addAttribute("goUrl", goUrl);
			return "member/alert";
		}
		// 세션존재
		int memberType = memberRepository.getById(sessionId).getMemberType();
//		int memberType = sessionMember.getMemberType();
//		String sessionId = sessionMember.getMemberId();
		// fk를 이용하면 간단해질 듯?
		// memberId로 labMember에서 검색
		List<LabMember> labMembers = labMemberRepository.findAllByMemberId(sessionId);
		// labMemberType를 리스트로 만들기
		List<Integer> labMemberTypes = labMembers.stream().map(LabMember::getLabMemberType)
				.collect(Collectors.toList());
		// type을 한글로
		List<String> labMemberRoles = new ArrayList<>();
		for (Integer type : labMemberTypes) {
			if (type == 0) {
				labMemberRoles.add("대기중");
			} else if (type == 1) {
				labMemberRoles.add("원장님");
			} else if (type == 2) {
				labMemberRoles.add("강사님");
			} else if (type == 3) {
				labMemberRoles.add("수강생");
			}
		}
		// labId를 리스트로 만들기
		List<Integer> labIds = labMembers.stream().map(LabMember::getLabId).collect(Collectors.toList());
		// labRepository를 사용하여 Lab 정보 가져오기
		List<Lab> labs = labRepository.findByLabIdIn(labIds);
		// 회원에게 연결된 학원이름과 그에 대한 자기역할 정보전달
		model.addAttribute("memberType", memberType);
		model.addAttribute("labMemberRoles", labMemberRoles);
		model.addAttribute("labs", labs);
		return "member/mylab";
	}

	// 내정보 수정
	@RequestMapping("/edit")
	public String myinfoForm(HttpSession session, Model model) {
//		Member sessionMember = (Member) session.getAttribute("sessionMember");
		String sessionId = (String) session.getAttribute("sessionId");
		Member sessionMember = memberRepository.findById(sessionId).get();
		// 세션만료
		if (sessionMember == null) {
			String msg = "로그인 세션이 만료되었습니다.";
			String goUrl = "/member/login";
			model.addAttribute("msg", msg);
			model.addAttribute("goUrl", goUrl);
			return "member/alert";
		}
		// 세션존재
		model.addAttribute("member", sessionMember);
		return "member/edit";
	}

	@PostMapping("/edit")
	public String myinfoReg(HttpSession session, @ModelAttribute Member updatedMember, Model model) {
		// 수정폼 정보로 검색
		String id = updatedMember.getMemberId();
		Member member = memberRepository.findById(id).get();
		String msg = "회원정보가 확인되지 않습니다.\n다시 시도해주세요.";
		String goUrl = "/member/login";
		if (member != null) {
			// 그냥 save하면 됐던 것 같은데 확인해보기
			member.setMemberPw(updatedMember.getMemberPw());
			member.setMemberPhone(updatedMember.getMemberPhone());
			member.setMemberZipcode(updatedMember.getMemberZipcode());
			member.setMemberAddress(updatedMember.getMemberAddress());
			member.setMemberAddressDetail(updatedMember.getMemberAddressDetail());
			member.setMemberProfile(updatedMember.getMemberProfile());
			memberRepository.save(member);
			session.invalidate();
			msg = "회원정보가 수정되었습니다.\n다시 로그인해주세요.";
			goUrl = "/member/login";
		}
		model.addAttribute("msg", msg);
		model.addAttribute("goUrl", goUrl);
		return "member/alert";
	}
}
