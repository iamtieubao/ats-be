package com.fu.fuatsbe.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fu.fuatsbe.DTO.DepartmentCreateDTO;
import com.fu.fuatsbe.DTO.DepartmentUpdateDTO;
import com.fu.fuatsbe.constant.department.DepartmentSuccessMessage;
import com.fu.fuatsbe.constant.response.ResponseStatusDTO;
import com.fu.fuatsbe.constant.role.RolePreAuthorize;
import com.fu.fuatsbe.entity.Department;
import com.fu.fuatsbe.response.DepartmentResponse;
import com.fu.fuatsbe.response.ResponseDTO;
import com.fu.fuatsbe.response.ResponseWithTotalPage;
import com.fu.fuatsbe.service.DepartmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/department")
@CrossOrigin("*")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping("getAll")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_EMPLOYEE)
    public ResponseEntity<ResponseDTO> getAllDepartments(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        ResponseDTO<ResponseWithTotalPage> response = new ResponseDTO();
        ResponseWithTotalPage<DepartmentResponse> list = departmentService.getAllDepartments(pageNo, pageSize);
        response.setData(list);
        response.setMessage(DepartmentSuccessMessage.GET_ALL_DEPARTMENT);
        response.setStatus(ResponseStatusDTO.SUCCESS);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("getById/{id}")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_EMPLOYEE)
    public ResponseEntity<ResponseDTO> getDepartmentById(@RequestParam("id") int id) {
        ResponseDTO<DepartmentResponse> responseDTO = new ResponseDTO();
        DepartmentResponse departmentResponse = departmentService.getDepartmentById(id);
        responseDTO.setData(departmentResponse);
        responseDTO.setMessage(DepartmentSuccessMessage.GET_DEPARTMENT_BY_ID);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("getByName")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_EMPLOYEE)
    public ResponseEntity<ResponseDTO> getDepartmentByName(
            @RequestParam("name") String name,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        ResponseDTO<ResponseWithTotalPage> response = new ResponseDTO();
        ResponseWithTotalPage<DepartmentResponse> list = departmentService.getDepartmentByName(name, pageNo, pageSize);
        response.setData(list);
        response.setMessage(DepartmentSuccessMessage.GET_DEPARTMENT_BY_NAME);
        response.setStatus(ResponseStatusDTO.SUCCESS);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("edit/{id}")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_EMPLOYEE)
    public ResponseEntity<ResponseDTO> updateDepartment(@RequestParam("id") int id,
            @RequestBody DepartmentUpdateDTO updateDTO) {
        ResponseDTO<DepartmentResponse> responseDTO = new ResponseDTO();
        DepartmentResponse department = departmentService.updateDepartment(id, updateDTO);
        responseDTO.setData(department);
        responseDTO.setMessage("Update department successfully");
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("create")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<ResponseDTO> createDepartment(
            @RequestBody DepartmentCreateDTO createDTO) {
        ResponseDTO<Department> responseDTO = new ResponseDTO();
        Department department = departmentService.createDepartment(createDTO);
        responseDTO.setData(department);
        responseDTO.setMessage("Create department successfully");
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping("delete/{id}")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity deleteDepartmnetById(@RequestParam("id") int id) {
        ResponseDTO<Department> responseDTO = new ResponseDTO();
        departmentService.deleteDepartmentById(id);
        responseDTO.setMessage("delete department successfully");
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

}
