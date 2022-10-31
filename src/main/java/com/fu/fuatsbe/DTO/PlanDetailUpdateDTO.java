package com.fu.fuatsbe.DTO;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanDetailUpdateDTO {

    private int amount;
    private String salary;
    private String reason;
    private Date timeRecruitingFrom;
    private Date timeRecruitingTo;
    private String note;
    private int positionId;

}
