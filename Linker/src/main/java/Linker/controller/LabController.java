package Linker.controller;

import java.util.List;

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

	// 학원페이지 개설하기
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
		// 생성된 학원정보로 학원회원 정보에 학원id 추가
		labMember.setLabId(lab.getLabId());
		labMemberRepository.save(labMember);
		String msg = "링커에 학원페이지가 개설되었습니다.";
		String goUrl = "/member/mylab";
		model.addAttribute("msg", msg);
		model.addAttribute("goUrl", goUrl);
		return "member/alert";
	}

	// 각 학원들 페이지
	@RequestMapping("/{labId}")
	String labMain(@PathVariable("labId") int labId, Model model, HttpSession session) {
		// 세션확인
//		Member sessionMember = (Member) session.getAttribute("sessionMember");
		String sessionId = (String) session.getAttribute("sessionId");
		Member sessionMember = memberRepository.findById(sessionId).get();
		if(sessionMember != null) {
			model.addAttribute("sessionMember", sessionMember);
			// 세션이 확인되면 학원회원페이지에서 타입정보 가져오기
			LabMember sessionLabMember = labMemberRepository.findByMemberIdAndLabId(sessionMember.getMemberId(), labId);
			if(sessionLabMember != null) {
				model.addAttribute("sessionLabMember", sessionLabMember);
			}			
		}
		Lab lab = labRepository.findById(labId).get();
		model.addAttribute("lab", lab);
		return "lab/main";
	}
	
	@RequestMapping("/{labId}/join")
	String labJoin(@PathVariable("labId") int labId, Model model, HttpSession session) {
		String sessionId = (String) session.getAttribute("sessionId");
		String msg = "로그인이 필요합니다.";
		String goUrl = "/member/login";
		// 세션없음
		if(sessionId == null) {
			model.addAttribute("msg", msg);
			model.addAttribute("goUrl", goUrl);
			return "member/alert";
		}
		goUrl = "/lab/" + labId;
		// 가입승인대기상태
		LabMember labMember = labMemberRepository.findByMemberIdAndLabId(sessionId, labId);
		if(labMember != null) {
			msg = "이미 가입신청을 하셨습니다.";
			model.addAttribute("msg", msg);
			model.addAttribute("goUrl", goUrl);
			return "member/alert";
		}
		// 가입신청시 학원회원테이블에 상태를 대기상태로 저장
		LabMember newLabMember = new LabMember();
		newLabMember.setLabId(labId);
		newLabMember.setLabMemberType(0);
		newLabMember.setMemberId(sessionId);
		labMemberRepository.save(newLabMember);
		String labName = labRepository.findById(labId).get().getLabName();
		msg = labName + "에 가입신청이 완료되었습니다.";
		model.addAttribute("msg", msg);
		model.addAttribute("goUrl", goUrl);
		return "member/alert";
	}

	// 회원목록
	// memberName, type별로 정렬(현재는 모두), 엑셀저장, 초대기능 추가해야 함
	@GetMapping("/{labId}/member")
	String LabMemberList(@PathVariable("labId") int labId, HttpSession session, Model model) {
		// 학원회원테이블에서 학원에 연결된 정보 가져오기
        List<LabMember> labMembers = labMemberRepository.findAllByLabId(labId);
        model.addAttribute("labMembers", labMembers);
		return "lab/member";
    }
	
	// 학원가입승인
    @RequestMapping("/{labId}/member/{memberId}/confirm")
    public String joinLabConfirm(@PathVariable("labId") int labId, @PathVariable("memberId") String memberId, Model model) {
        Member member = memberRepository.getById(memberId);
        String msg = "회원이 존재하지 않습니다.";
        String goUrl = "/lab/"+labId+"/member";
        if (member != null) {
        	// 학원회원테이블에서 타입정보를 대기상태에서 회원타입으로 변경
            LabMember labMember = labMemberRepository.findByMemberIdAndLabId(memberId, labId);
            labMember.setLabMemberType(member.getMemberType());
            labMemberRepository.save(labMember);
            msg = "가입을 승인했습니다.";
        }
		model.addAttribute("msg", msg);
		model.addAttribute("goUrl", goUrl);
		return "member/alert";
    }
}
