package com.fu.fuatsbe.serviceImp;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.fu.fuatsbe.DTO.RecruitmentRequestCreateDTO;
import com.fu.fuatsbe.DTO.RecruitmentRequestUpdateDTO;
import com.fu.fuatsbe.constant.employee.EmployeeErrorMessage;
import com.fu.fuatsbe.constant.planDetail.PlanDetailErrorMessage;
import com.fu.fuatsbe.constant.planDetail.PlanDetailStatus;
import com.fu.fuatsbe.constant.postion.PositionErrorMessage;
import com.fu.fuatsbe.constant.recruitmentRequest.RecruitmentRequestErrorMessage;
import com.fu.fuatsbe.constant.recruitmentRequest.RecruitmentRequestStatus;
import com.fu.fuatsbe.entity.Employee;
import com.fu.fuatsbe.entity.PlanDetail;
import com.fu.fuatsbe.entity.Position;
import com.fu.fuatsbe.entity.RecruitmentRequest;
import com.fu.fuatsbe.exceptions.ListEmptyException;
import com.fu.fuatsbe.exceptions.NotFoundException;
import com.fu.fuatsbe.exceptions.NotValidException;
import com.fu.fuatsbe.repository.EmployeeRepository;
import com.fu.fuatsbe.repository.PlanDetailRepository;
import com.fu.fuatsbe.repository.PositionRepository;
import com.fu.fuatsbe.repository.RecruitmentRequestRepository;
import com.fu.fuatsbe.response.RecruitmentRequestResponse;
import com.fu.fuatsbe.service.RecruitmentRequestService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecruitmentRequestServiceImp implements RecruitmentRequestService {

    private final RecruitmentRequestRepository recruitmentRequestRepository;
    private final ModelMapper modelMapper;
    private final EmployeeRepository employeeRepository;
    private final PlanDetailRepository planDetailRepository;
    private final PositionRepository positionRepository;

    @Override
    public List<RecruitmentRequestResponse> getAllRecruitmentRequests() {
        List<RecruitmentRequest> list = recruitmentRequestRepository.findAll();
        List<RecruitmentRequestResponse> result = new ArrayList<RecruitmentRequestResponse>();
        if (list.size() > 0) {
            for (RecruitmentRequest recruitmentRequest : list) {
                RecruitmentRequestResponse response = modelMapper.map(recruitmentRequest,
                        RecruitmentRequestResponse.class);
                result.add(response);
            }
        } else
            throw new ListEmptyException(RecruitmentRequestErrorMessage.LIST_RECRUITMENT_REQUEST_EMPTY_EXCEPTION);
        return result;
    }

    @Override
    public RecruitmentRequestResponse getRecruitmentRequestById(int id) {
        RecruitmentRequest recruitmentRequest = recruitmentRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        RecruitmentRequestErrorMessage.RECRUITMENT_REQUEST_NOT_FOUND_EXCEPTION));
        RecruitmentRequestResponse response = modelMapper.map(recruitmentRequest, RecruitmentRequestResponse.class);
        return response;
    }

    @Override
    public List<RecruitmentRequestResponse> getAllOpenRecruitmentRequest() {
        List<RecruitmentRequest> list = recruitmentRequestRepository.findByStatus(RecruitmentRequestStatus.OPENING);
        List<RecruitmentRequestResponse> result = new ArrayList<RecruitmentRequestResponse>();
        if (list.size() > 0) {
            for (RecruitmentRequest recruitmentRequest : list) {
                RecruitmentRequestResponse response = modelMapper.map(recruitmentRequest,
                        RecruitmentRequestResponse.class);
                result.add(response);
            }
        } else
            throw new ListEmptyException(RecruitmentRequestErrorMessage.LIST_RECRUITMENT_REQUEST_EMPTY_EXCEPTION);
        return result;
    }

    @Override
    public List<RecruitmentRequestResponse> getAllFilledRecruitmentRequest() {
        List<RecruitmentRequest> list = recruitmentRequestRepository.findByStatus(RecruitmentRequestStatus.FILLED);
        List<RecruitmentRequestResponse> result = new ArrayList<RecruitmentRequestResponse>();
        if (list.size() > 0) {
            for (RecruitmentRequest recruitmentRequest : list) {
                RecruitmentRequestResponse response = modelMapper.map(recruitmentRequest,
                        RecruitmentRequestResponse.class);
                result.add(response);
            }
        } else
            throw new ListEmptyException(RecruitmentRequestErrorMessage.LIST_RECRUITMENT_REQUEST_EMPTY_EXCEPTION);
        return result;
    }

    @Override
    public List<RecruitmentRequestResponse> getAllClosedRecruitmentRequest() {
        List<RecruitmentRequest> list = recruitmentRequestRepository.findByStatus(RecruitmentRequestStatus.CLOSED);
        List<RecruitmentRequestResponse> result = new ArrayList<RecruitmentRequestResponse>();
        if (list.size() > 0) {
            for (RecruitmentRequest recruitmentRequest : list) {
                RecruitmentRequestResponse response = modelMapper.map(recruitmentRequest,
                        RecruitmentRequestResponse.class);
                result.add(response);
            }
        } else
            throw new ListEmptyException(RecruitmentRequestErrorMessage.LIST_RECRUITMENT_REQUEST_EMPTY_EXCEPTION);
        return result;
    }

    @Override
    public RecruitmentRequestResponse updateRecruitmentRequest(int id, RecruitmentRequestUpdateDTO updateDTO) {
        RecruitmentRequest recruitmentRequest = recruitmentRequestRepository.findById(id).orElseThrow(
                () -> new NotFoundException(RecruitmentRequestErrorMessage.RECRUITMENT_REQUEST_NOT_FOUND_EXCEPTION));
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(PositionErrorMessage.POSITION_NOT_EXIST));
        recruitmentRequest.setAmount(updateDTO.getAmount());
        recruitmentRequest.setExpiryDate(updateDTO.getExpiryDate());
        recruitmentRequest.setIndustry(updateDTO.getIndustry());
        recruitmentRequest.setJobLevel(updateDTO.getJobLevel());
        recruitmentRequest.setExperience(updateDTO.getExperience());
        recruitmentRequest.setSalary(updateDTO.getSalary());
        recruitmentRequest.setTypeOfWork(updateDTO.getTypeOfWork());
        recruitmentRequest.setDescription(updateDTO.getDescription());
        recruitmentRequest.setPosition(position);

        RecruitmentRequest recruitmentRequestSaved = recruitmentRequestRepository.save(recruitmentRequest);
        RecruitmentRequestResponse response = modelMapper.map(recruitmentRequestSaved,
                RecruitmentRequestResponse.class);
        return response;

    }

    @Override
    public RecruitmentRequestResponse createRecruitmentRequest(RecruitmentRequestCreateDTO createDTO) {
        Optional<Employee> optionalCreator = employeeRepository.findById(createDTO.getEmployeeId());
        PlanDetail planDetail = planDetailRepository.findById(createDTO.getPlanDetailId())
                .orElseThrow(() -> new NotFoundException(PlanDetailErrorMessage.PLAN_DETAIL_NOT_FOUND_EXCEPTION));
        Position position = positionRepository.findById(createDTO.getPositionId())
                .orElseThrow(() -> new NotFoundException(PositionErrorMessage.POSITION_NOT_EXIST));
        if (!planDetail.getStatus().equalsIgnoreCase(PlanDetailStatus.APPROVED))
            throw new NotValidException(PlanDetailErrorMessage.PLAN_DETAIL_NOT_APPROVED_EXCEPTION);
        if (optionalCreator.isPresent()) {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));
            LocalDate dateFormat = LocalDate.parse(date.toString(), format);
            LocalDate expiryDate = LocalDate.parse(createDTO.getExpiryDate().toString(), format);

            RecruitmentRequest request = RecruitmentRequest.builder().date(Date.valueOf(dateFormat))
                    .expiryDate(Date.valueOf(expiryDate)).industry(createDTO.getIndustry())
                    .amount(createDTO.getAmount()).jobLevel(createDTO.getJobLevel())
                    .status(RecruitmentRequestStatus.OPENING).experience(createDTO.getExperience())
                    .salary(createDTO.getSalary()).typeOfWork(createDTO.getTypeOfWork())
                    .description(createDTO.getDescription()).creator(optionalCreator.get()).planDetail(planDetail)
                    .position(position).build();
            recruitmentRequestRepository.save(request);
            RecruitmentRequestResponse response = modelMapper.map(request, RecruitmentRequestResponse.class);
            return response;
        } else {
            throw new NotFoundException(EmployeeErrorMessage.EMPLOYEE_NOT_FOUND_EXCEPTION);
        }
    }

    @Override
    public RecruitmentRequestResponse closeRecruitmentRequest(int id) {
        RecruitmentRequest recruitmentRequest = recruitmentRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        RecruitmentRequestErrorMessage.RECRUITMENT_REQUEST_NOT_FOUND_EXCEPTION));
        recruitmentRequest.setStatus(RecruitmentRequestStatus.CLOSED);
        RecruitmentRequest recruitmentRequestSaved = recruitmentRequestRepository.save(recruitmentRequest);
        RecruitmentRequestResponse response = modelMapper.map(recruitmentRequestSaved,
                RecruitmentRequestResponse.class);
        return response;
    }

    @Override
    public List<RecruitmentRequestResponse> getAllRecruitmentRequestByCreator(int id) {
        List<RecruitmentRequest> list = recruitmentRequestRepository.findByCreatorId(id);
        List<RecruitmentRequestResponse> result = new ArrayList<RecruitmentRequestResponse>();
        if (list.size() > 0) {
            for (RecruitmentRequest recruitmentRequest : list) {
                RecruitmentRequestResponse response = modelMapper.map(recruitmentRequest,
                        RecruitmentRequestResponse.class);
                result.add(response);
            }
        } else
            throw new ListEmptyException(RecruitmentRequestErrorMessage.LIST_RECRUITMENT_REQUEST_EMPTY_EXCEPTION);
        return result;
    }

}