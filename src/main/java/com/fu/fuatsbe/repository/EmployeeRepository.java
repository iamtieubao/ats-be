package com.fu.fuatsbe.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fu.fuatsbe.entity.Department;
import com.fu.fuatsbe.entity.Employee;
import com.fu.fuatsbe.entity.Position;

@Repository
@Transactional
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    public Optional<Employee> findById(int id);

    public Optional<Employee> findByEmployeeCode(String employeeCode);

    public Optional<Employee> findByPhone(String phone);

//    public Page<Employee> findByPositionAndStatus(Position position, String status, Pageable pageable);

    public Page<Employee> findByDepartment(Department department, Pageable pageable);
    @Query(nativeQuery = true, value = "select * from employee")
    Page<Employee> getAll(Pageable pageable);


    @Modifying
    @Query(nativeQuery = true, value = "select * from employee e where e.id in (select ie.employee_id from interview_employee ie where ie.interview_id = ?1);")
    public List<Employee> getEmployeeByInterviewId(int interviewId);

}
