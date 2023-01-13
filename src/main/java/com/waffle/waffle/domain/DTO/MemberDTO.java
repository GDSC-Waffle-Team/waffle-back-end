package com.waffle.waffle.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDTO {
    private String memberId;
    private String nickname;
    private String part;
    private List<FineDTO> fines;
}
