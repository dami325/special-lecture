package io.dami.speciallecture.interfaces.api.speciallecture;

import io.dami.speciallecture.domain.speciallecture.result.SpecialLectureParticipantInfo;
import io.dami.speciallecture.domain.speciallecture.result.SpecialLectureInfo;
import io.dami.speciallecture.domain.speciallecture.service.SpecialLectureService;
import io.dami.speciallecture.interfaces.request.ParticipantsRequest;
import io.dami.speciallecture.interfaces.request.SpecialLecturesRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/special-lectures")
public class SpecialLectureController {

    private final SpecialLectureService specialLectureService;

    /**
     * 특강 신청 가능 목록 API
     * 날짜별로 현재 신청 가능한 특강 목록을 조회합니다.
     */
    @GetMapping
    public ResponseEntity<Page<SpecialLectureInfo>> getAvailableSpecialLectures(SpecialLecturesRequest request) {
        return ResponseEntity.ok(specialLectureService.getAvailableSpecialLecturePage(request.toCommand()));
    }

    /**
     * 특강 신청 API
     * 특정 userId로 제공되는 특강을 신청합니다.
     * 선착순으로 30명까지만 신청 가능하며, 중복 신청은 불가능합니다.
     */
    @PostMapping("/{specialLectureId}/participant")
    public ResponseEntity<SpecialLectureParticipantInfo> registerParticipant(
            @PathVariable Long specialLectureId,
            @RequestParam Long userId
    ) {
        return ResponseEntity.ok(specialLectureService.registerParticipant(specialLectureId,userId));
    }

    /**
     * 특강 신청 완료 목록 조회 API
     * 특정 userId로 신청 완료된 특강 목록을 조회합니다.
     */
    @GetMapping("/participants")
    public ResponseEntity<Page<SpecialLectureParticipantInfo>> getCompletedSpecialLectures(ParticipantsRequest request) {
        return ResponseEntity.ok(specialLectureService.getSpecialLectureParticipantInfoPage(request.toCommand()));
    }
}
