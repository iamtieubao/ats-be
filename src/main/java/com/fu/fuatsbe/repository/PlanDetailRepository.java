package com.fu.fuatsbe.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fu.fuatsbe.entity.PlanDetail;
import com.fu.fuatsbe.entity.RecruitmentPlan;

@Transactional
@Repository
public interface PlanDetailRepository extends JpaRepository<PlanDetail, Integer> {
    Optional<PlanDetail> findById(int id);

    List<PlanDetail> findByStatus(String status);

    List<PlanDetail> findByRecruitmentPlan(RecruitmentPlan recruitmentPlan);

    @Modifying
    @Query(value = "select * from plan_detail p where p.approver_id = ?1", nativeQuery = true)
    List<PlanDetail> findByApproverId(int id);
}