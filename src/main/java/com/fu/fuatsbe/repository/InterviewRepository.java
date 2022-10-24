package com.fu.fuatsbe.repository;

import com.fu.fuatsbe.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface InterviewRepository extends JpaRepository<Interview, Integer> {
}