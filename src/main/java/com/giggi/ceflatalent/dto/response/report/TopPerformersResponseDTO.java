package com.giggi.ceflatalent.dto.response.report;

import com.giggi.ceflatalent.entity.ReportType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopPerformersResponseDTO {
    private ReportType reportType;
    private List<RankingEntryDTO> entries;
}
