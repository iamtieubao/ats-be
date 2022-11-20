package com.fu.fuatsbe.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fu.fuatsbe.entity.Interview;

@Repository
@Transactional
public interface InterviewRepository extends JpaRepository<Interview, Integer> {

        Page<Interview> findInterviewByCandidateId(int candidate_id, Pageable pageable);

        @Query(nativeQuery = true, value = "select * from interview where id in \n" +
                        "(select interview_id from interview_employee where employee_id = ?1)")
        Page<Interview> findInterviewByEmployeeId(int employee_id, Pageable pageable);

        @Query(nativeQuery = true, value = "select distinct i.* from interview i join candidate c on i.candidate_id = c.id \n"
                        + "where c.name like %?1% \n" +
                        " and i.type like %?2% \n"
                        + " and i.status like %?3% \n" +
                        "order by i.id desc")
        List<Interview> searchInterview(String candidateName, String type, String status);

        @Query(nativeQuery = true, value = "select * from interview \n"
                        + "where id in (select interview_id from interview_employee \n"
                        + "where employee_id in (select id from employee where department_id = ?1))")
        Page<Interview> findInterviewByDepartrment(int department_id, Pageable pageable);

}
