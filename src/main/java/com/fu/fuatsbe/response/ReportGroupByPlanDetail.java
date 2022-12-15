package com.fu.fuatsbe.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportGroupByPlanDetail {
    private String planDetailName;
    private List<ReportGroupByJobRequest> jobRequests;
}
