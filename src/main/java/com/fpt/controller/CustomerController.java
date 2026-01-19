package com.fpt.controller;

import com.fpt.dto.CustomerDTO;
import com.fpt.dto.OptionDTO;
import com.fpt.payload.PaginatedResponse;
import com.fpt.payload.SuccessNoResponse;
import com.fpt.payload.SuccessResponse;
import com.fpt.service.interfaces.ICustomerService;
import com.fpt.service.interfaces.IOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Validated
public class CustomerController {

    private final ICustomerService customerService;
    @GetMapping
    public ResponseEntity<PaginatedResponse<CustomerDTO>> getAll(
            Pageable pageable,
            @RequestParam(required = false) String search
    ) {
        Page<CustomerDTO> dtoPage = customerService.getAllCustomer(pageable, search);
        PaginatedResponse<CustomerDTO> response = new PaginatedResponse<>(dtoPage, HttpServletResponse.SC_OK, "Lấy danh sách khách hàng thành công");
        return ResponseEntity.ok(response);
    }
    @GetMapping("/list")
    public ResponseEntity<SuccessResponse<List<CustomerDTO>>> getListAll() {
        List<CustomerDTO> list = customerService.getAll();
        return ResponseEntity.ok(new SuccessResponse<>(200, "Lấy danh sách khách hàng thành công!", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<CustomerDTO>> getById(@PathVariable Long id) {
        CustomerDTO dto = customerService.getById(id);
        return ResponseEntity.ok(new SuccessResponse<>(200, "Lấy thông tin chi tiết khách hàng thành công!", dto));
    }

}
