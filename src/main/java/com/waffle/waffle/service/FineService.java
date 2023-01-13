package com.waffle.waffle.service;

import com.waffle.waffle.domain.DTO.FineDTO;
import com.waffle.waffle.domain.Fine;
import com.waffle.waffle.repository.FineRepository;
import com.waffle.waffle.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FineService {

    private final FineRepository fineRepository;
    private final MemberRepository memberRepository;

    // 내역 추가
    public void addFine(String memberId, FineDTO fineRequestDto) {
        fineRequestDto.setMember(memberRepository.findByMemberId(memberId).get());
        fineRepository.save(fineRequestDto.toEntity());
    }

    // 전체 내역
    public List<FineDTO> findAll() {
        return fineRepository.findAll().stream()
                .map(Fine::toDTO)
                .collect(Collectors.toList());
    }

    // 멤버의 전체 내역
    public List<FineDTO> findByMemberId(String memberId) {
        return fineRepository.findFinesByMember(memberRepository.findByMemberId(memberId).get()).stream()
                .map(Fine::toDTO)
                .collect(Collectors.toList());
    }

    // 벌금 지불
    public void updateFine(FineDTO fineRequestDto) {
        Fine fine = fineRepository.findById(fineRequestDto.getId()).get();
        fine.setStatus(!fine.getStatus());
        fineRepository.save(fine);
    }

    // 벌금 내역 삭제
    public void deleteFine(FineDTO fineRequestDto) {
        Fine fine = fineRepository.findById(fineRequestDto.getId()).get();
        fineRepository.delete(fine);
    }

}
