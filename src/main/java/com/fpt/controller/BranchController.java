package com.fpt.controller;

import com.fpt.dto.BranchDTO;
import com.fpt.payload.PaginatedResponse;
import com.fpt.payload.SuccessNoResponse;
import com.fpt.payload.SuccessResponse;
import com.fpt.service.interfaces.IBranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/v1/branches")
@RequiredArgsConstructor
@Validated
public class BranchController {

    private final IBranchService service;

    @GetMapping
    public List<BranchDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/list")
    public ResponseEntity<PaginatedResponse<BranchDTO>> getAllBranches(
             Pageable pageable,
            @RequestParam(required = false) String search,
             @RequestParam(required = false) Boolean isActive

    ) {
        Page<BranchDTO> dtoPage = service.getAllBranch(pageable, search,isActive);
        PaginatedResponse<BranchDTO> response = new PaginatedResponse<>(dtoPage, HttpServletResponse.SC_OK, "Lấy danh sách chi nhánh thành công");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/list")
    public ResponseEntity<PaginatedResponse<BranchDTO>> getAllBranchesCustomer(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String search
    ) {
        Page<BranchDTO> dtoPage = service.getAllBranchCustomer(pageable, search);
        PaginatedResponse<BranchDTO> response = new PaginatedResponse<>(dtoPage, HttpServletResponse.SC_OK, "Lấy danh sách chi nhánh thành công");
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<BranchDTO>> getById(@PathVariable Long id) {
        BranchDTO dto = service.getById(id);
        SuccessResponse<BranchDTO> response = new SuccessResponse<>(
                HttpServletResponse.SC_OK,
                "Lấy chi tiết chi nhánh thành công!",
                dto
        );
        return ResponseEntity.ok(response);
    }
    

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessNoResponse> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.ok(new SuccessNoResponse(200, "Delete successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new SuccessNoResponse(500, "Delete failed!"));
        }
    }

    @DeleteMapping
    public ResponseEntity<SuccessNoResponse> deleteMore(@RequestBody List<Long> ids) {
        try {
            service.deleteMore(ids);
            return ResponseEntity.ok(new SuccessNoResponse(200, "Delete successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new SuccessNoResponse(500, "Delete failed!"));
        }
    }


}
