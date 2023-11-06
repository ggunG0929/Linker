package Linker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import Linker.model.ClassAvi;
import Linker.model.ClassLab;
import Linker.model.FileUploadDownload;
import Linker.repository.ClassAviRepository;
import Linker.repository.ClassRepository;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("class")
public class ClassController {

	@Autowired
	ClassRepository classRepository;
	@Autowired
	ClassAviRepository classAviRepository;
	@Autowired
	FileUploadDownload fileupload;
	// 클래스 개설하기
		@GetMapping("/add")
		public String addClassForm(HttpSession session, Model model) {

	/*		String sessionId = (String) session.getAttribute("sessionId");
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
			 */
			
			return "class/add";
		}
		
		@PostMapping("/add")
		public String addClassReg(ClassLab classLab, Model model) {
			classRepository.save(classLab);
			// 생성된 학원정보로 학원회원 정보에 학원id 추가
		
			String msg = "링커에 학원페이지가 개설되었습니다.";
			String goUrl = "/member/mylab";
			model.addAttribute("msg", msg);
			model.addAttribute("goUrl", goUrl);
			return "member/alert";
		}
		
	//동영상 파일업로드 
		@GetMapping("/upload")
		public String uploadForm(HttpSession session, Model model) {

			
			return "class/upload_avi";
		}
		
		@PostMapping("/upload")
		public String uploadReg(
				ClassAvi classAvi, 
				MultipartFile aviFile, HttpSession session, Model model,RedirectAttributes redirectAttributes) {
		

			classAvi.setAviFileName(fileupload.videoSave(aviFile));
			classAviRepository.save(classAvi);
			return "redirect:avi/"+classAvi.getClassAviId();
		}
		
		 @GetMapping("/avi/{classAviId}")
		    public String playVideo(@PathVariable int classAviId, Model model) {
		        // 동영상 파일 이름을 받아서 재생 페이지로 리디렉션
			 ClassAvi classAvi = classAviRepository.findById(classAviId).get();
			 
			 model.addAttribute("aviFileName", classAvi.getAviFileName());
		        return "class/play";
		    }
		
		
}
