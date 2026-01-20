package com.fpt.controller;

import com.fpt.dto.OptionDTO;
import com.fpt.dto.OptionGroupDTO;
import com.fpt.dto.ProductDTO;
import com.fpt.form.OptionCreateRequest;
import com.fpt.form.OptionGroupCreateRequest;
import com.fpt.payload.PaginatedResponse;
import com.fpt.payload.SuccessNoResponse;
import com.fpt.payload.SuccessResponse;
import com.fpt.service.interfaces.IOptionGroupService;
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
@RequestMapping("/api/v1/options")
@RequiredArgsConstructor
@Validated
public class OptionController {
    public static final String GROUP_PATH = "/group";
    private final IOptionService optionService;
    private final IOptionGroupService optionGroupService;
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////OPTION GROUP///////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    @PostMapping(GROUP_PATH)
    public ResponseEntity<SuccessResponse<OptionGroupDTO>> createOptionGroup(@RequestBody OptionGroupCreateRequest request) {
        OptionGroupDTO dto= optionGroupService.create(request);
        SuccessResponse<OptionGroupDTO> response = new SuccessResponse<>(
                HttpServletResponse.SC_OK,
                "Thêm mới nhóm lựa chọn thành công!",
                dto
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping(GROUP_PATH + "/{id}")
    public ResponseEntity<SuccessResponse<OptionGroupDTO>> updateOptionGroup(@PathVariable Long id,@RequestBody OptionGroupCreateRequest request) {
        OptionGroupDTO dto= optionGroupService.update(id,request);
        SuccessResponse<OptionGroupDTO> response = new SuccessResponse<>(
                HttpServletResponse.SC_OK,
                "Cập nhật nhóm lựa chọn thành công!",
                dto
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping(GROUP_PATH)
    public ResponseEntity<SuccessResponse<List<OptionGroupDTO>>> getList() {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        HttpServletResponse.SC_OK,
                        "Lấy danh sách nhóm lựa chọn thành công!",
                        optionGroupService.getList()
                )
        );
    }

    @GetMapping(GROUP_PATH + "/{id}")
    public ResponseEntity<SuccessResponse<OptionGroupDTO>> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        HttpServletResponse.SC_OK,
                        "Lấy chi tiết danh sách nhóm lựa chọn thành công!",
                        optionGroupService.getDetail(id)
                )
        );
    }

    @DeleteMapping(GROUP_PATH + "/{id}")
    public ResponseEntity<SuccessNoResponse> deleteGroupOption(@PathVariable Long id) {
        optionGroupService.delete(id);
        return ResponseEntity.ok(new SuccessNoResponse(200, "Delete successfully!"));
    }

    @DeleteMapping(GROUP_PATH)
    public ResponseEntity<SuccessNoResponse> deleteManyGroupOption(@RequestBody List<Long> ids) {
        optionGroupService.deleteMany(ids);
        return ResponseEntity.ok(new SuccessNoResponse(200, "Delete successfully!"));
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////OPTION ////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    @PostMapping
    public ResponseEntity<SuccessResponse<OptionDTO>> createOption(@RequestBody OptionCreateRequest request) {
        OptionDTO dto= optionService.create(request);
        SuccessResponse<OptionDTO> response = new SuccessResponse<>(
                HttpServletResponse.SC_OK,
                "Thêm mới lựa chọn thành công!",
                dto
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<OptionDTO>> updateOption(@RequestBody OptionCreateRequest request,@PathVariable Long id) {
        OptionDTO dto= optionService.update(id, request);
        SuccessResponse<OptionDTO> response = new SuccessResponse<>(
                HttpServletResponse.SC_OK,
                "Cập nhật lựa chọn thành công!",
                dto
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessNoResponse> delete(@PathVariable Long id) {
        optionService.delete(id);
        return ResponseEntity.ok(new SuccessNoResponse(200, "Delete successfully!"));
    }

    @DeleteMapping
    public ResponseEntity<SuccessNoResponse> deleteMany(@RequestBody List<Long> ids) {
        optionService.deleteMany(ids);
        return ResponseEntity.ok(new SuccessNoResponse(200, "Delete successfully!"));
    }
}
