package com.waffle.waffle.controller;

import com.waffle.waffle.domain.DTO.FineDTO;
import com.waffle.waffle.domain.DTO.MemberDTO;
import com.waffle.waffle.domain.Member;
import com.waffle.waffle.repository.MemberRepository;
import com.waffle.waffle.service.FineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminController {
    private final MemberRepository memberRepository;

    private final FineService fineService;

    // 관리자 전용 페이지, 모든 멤버의 이름과 파트가 보임
    @GetMapping("/admin")
    public ResponseEntity<List<MemberDTO>> admin() {
        List<Member> list = memberRepository.findAll();
        return ResponseEntity.ok(list.stream()
                .map(Member::toDTO)
                .collect(Collectors.toList()));
    }

    // 관리자가 특정 멤버의 내역을 확인
    @GetMapping("/admin/{memberId}")
    public ResponseEntity<List<FineDTO>> findByMemberId(@PathVariable("memberId") String memberId) {
        List<FineDTO> list = fineService.findByMemberId(memberId);
        return ResponseEntity.ok(list);
    }

    // 관리자 페이지에서 벌금 추가하기 RequestBody -> memberId(String), date(YYYY-MM-DD), type(00, 01, 10)
    @PostMapping("/admin/{memberId}")
    public ResponseEntity<String> addFineAdmin(@PathVariable("memberId") String memberId, @RequestBody FineDTO fineRequestDto) throws Exception {

        if(memberId.isEmpty()) {
            throw new Exception("memberId 비어있음");
        }
        if(fineRequestDto.getDate() == null) {
            throw new Exception("날짜 null");
        }
        //if(!checkDate(new SimpleDateFormat("yyyy-MM-dd").format(fineRequestDto.getDate()))) {
        //    throw new Exception("날짜 포맷 오류" + fineRequestDto.getDate().toString());
        //}
        if(fineRequestDto.getType() == null) {
            throw new Exception("납부 사유 null");
        }
        fineService.addFine(memberId, fineRequestDto);
        return ResponseEntity.ok("add fine successfully");
    }

    // 멤버의 내역 중 선택한 내역의 상태를 변경 (납부/미납)
    @PatchMapping("/admin/{memberId}")
    public ResponseEntity<String> payFine(@PathVariable("memberId") String memberId, @RequestBody FineDTO fineRequestDto) {
        if(fineRequestDto == null) {
            return ResponseEntity.ok("bad request");
        }
        else {
            fineService.updateFine(fineRequestDto);
            return ResponseEntity.ok("change status successfully");
        }
    }

    // 멤버의 내역 중 선택한 내역을 삭제
    @DeleteMapping("/admin/{memberId}")
    public ResponseEntity<String> deleteFine(@PathVariable("memberId") String memberId, @RequestBody FineDTO fineRequestDto) {
        if(fineRequestDto == null) {
            return ResponseEntity.ok("bad request");
        }
        else {
            fineService.deleteFine(fineRequestDto);
            return ResponseEntity.ok("delete fine successfully");
        }
    }

    // 테스트용, 관리자 페이지에서 전체 벌금 내역을 문자열로 출력, 테스트용
    @GetMapping("/admin/list")
    public ResponseEntity<List<FineDTO>> adminList() {
        List<FineDTO> list = fineService.findAll();
        return ResponseEntity.ok(list);
    }

    // 날짜 형식 체크
    public static boolean checkDate(String checkDate) {
        try {
            SimpleDateFormat dateFormatParser = new SimpleDateFormat("yyyy-MM-dd"); // 검증할 날짜 포맷 설정
            dateFormatParser.setLenient(false); // false일경우 처리시 입력한 값이 잘못된 형식일 시 오류가 발생
            dateFormatParser.parse(checkDate); // 대상 값 포맷에 적용되는지 확인
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
