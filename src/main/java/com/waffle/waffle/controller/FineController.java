package com.waffle.waffle.controller;

import com.waffle.waffle.domain.DTO.FineDTO;
import com.waffle.waffle.service.FineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FineController {

    private final FineService fineService;

    // 관리자 전용 페이지
    @GetMapping("/admin")
    public ResponseEntity<String> admin() {
        return ResponseEntity.ok("관리자 페이지");
    }

    // 관리자 페이지에서 벌금 추가하기 RequestBody -> memberId(String), date(YYYY-MM-DD), type(00, 01, 10)
    @PostMapping("/admin/{memberId}")
    public ResponseEntity<String> adminAdd(@PathVariable("memberId") String memberId, @RequestBody FineDTO fineRequestDto) {
        fineService.addFine(memberId, fineRequestDto);
        return ResponseEntity.ok("add fine successfully");
    }

    // 테스트용, 관리자 페이지에서 전체 벌금 내역을 문자열로 출력
    @GetMapping("/admin/list")
    public ResponseEntity<List<FineDTO>> adminList() {
        List<FineDTO> list = fineService.findAll();
        return ResponseEntity.ok(list);
    }

    // 관리자가 특정 멤버의 내역을 확인
    @GetMapping("/admin/{memberId}/list")
    public ResponseEntity<List<FineDTO>> findByMemberId(@PathVariable("memberId") String memberId) {
        List<FineDTO> list = fineService.findByMemberId(memberId);
        return ResponseEntity.ok(list);
    }

    // 멤버의 내역 중 선택한 내역의 상태를 변경 (납부/미납)
    @PatchMapping("/admin/{memberId}/list")
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
    @DeleteMapping("/admin/{memberId}/list")
    public ResponseEntity<String> deleteFine(@PathVariable("memberId") String memberId, @RequestBody FineDTO fineRequestDto) {
        if(fineRequestDto == null) {
            return ResponseEntity.ok("bad request");
        }
        else {
            fineService.deleteFine(fineRequestDto);
            return ResponseEntity.ok("delete fine successfully");
        }
    }

}