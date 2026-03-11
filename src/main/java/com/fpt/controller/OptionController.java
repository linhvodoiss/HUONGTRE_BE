package com.fpt.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpt.constant.ApiPaths;
import com.fpt.constant.ResponseMessage;
import com.fpt.dto.OptionDTO;
import com.fpt.dto.OptionGroupDTO;
import com.fpt.form.OptionCreateRequest;
import com.fpt.form.OptionGroupCreateRequest;
import com.fpt.payload.SuccessNoResponse;
import com.fpt.payload.SuccessResponse;
import com.fpt.service.interfaces.IOptionGroupService;
import com.fpt.service.interfaces.IOptionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(ApiPaths.OPTIONS)
@RequiredArgsConstructor
@Validated
public class OptionController {
    public static final String GROUP_PATH = "/group";
    private final IOptionService optionService;
    private final IOptionGroupService optionGroupService;

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////// OPTION
    ///////////////////////////////////////////////////////////////////////////////////////////////////// GROUP///////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    @PostMapping(GROUP_PATH)
    public ResponseEntity<SuccessResponse<OptionGroupDTO>> createOptionGroup(
            @RequestBody OptionGroupCreateRequest request) {
        OptionGroupDTO dto = optionGroupService.create(request);
        SuccessResponse<OptionGroupDTO> response = new SuccessResponse<>(
                HttpServletResponse.SC_OK,
                ResponseMessage.CREATE_OPTION_GROUP_SUCCESS,
                dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping(GROUP_PATH + "/{id}")
    public ResponseEntity<SuccessResponse<OptionGroupDTO>> updateOptionGroup(@PathVariable Long id,
            @RequestBody OptionGroupCreateRequest request) {
        OptionGroupDTO dto = optionGroupService.update(id, request);
        SuccessResponse<OptionGroupDTO> response = new SuccessResponse<>(
                HttpServletResponse.SC_OK,
                ResponseMessage.UPDATE_OPTION_GROUP_SUCCESS,
                dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping(GROUP_PATH)
    public ResponseEntity<SuccessResponse<List<OptionGroupDTO>>> getList() {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        HttpServletResponse.SC_OK,
                        ResponseMessage.GET_LIST_OPTION_GROUP_SUCCESS,
                        optionGroupService.getList()));
    }

    @GetMapping(GROUP_PATH + "/{id}")
    public ResponseEntity<SuccessResponse<OptionGroupDTO>> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        HttpServletResponse.SC_OK,
                        ResponseMessage.GET_OPTION_GROUP_DETAIL_SUCCESS,
                        optionGroupService.getDetail(id)));
    }

    @DeleteMapping(GROUP_PATH + "/{id}")
    public ResponseEntity<SuccessNoResponse> deleteGroupOption(@PathVariable Long id) {
        optionGroupService.delete(id);
        return ResponseEntity.ok(new SuccessNoResponse(200, ResponseMessage.DELETE_SUCCESS));
    }

    @DeleteMapping(GROUP_PATH)
    public ResponseEntity<SuccessNoResponse> deleteManyGroupOption(@RequestBody List<Long> ids) {
        optionGroupService.deleteMany(ids);
        return ResponseEntity.ok(new SuccessNoResponse(200, ResponseMessage.DELETE_SUCCESS));
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////// OPTION
    ///////////////////////////////////////////////////////////////////////////////////////////////////// ////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    @PostMapping
    public ResponseEntity<SuccessResponse<OptionDTO>> createOption(@RequestBody OptionCreateRequest request) {
        OptionDTO dto = optionService.create(request);
        SuccessResponse<OptionDTO> response = new SuccessResponse<>(
                HttpServletResponse.SC_OK,
                ResponseMessage.CREATE_OPTION_SUCCESS,
                dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<OptionDTO>> updateOption(@RequestBody OptionCreateRequest request,
            @PathVariable Long id) {
        OptionDTO dto = optionService.update(id, request);
        SuccessResponse<OptionDTO> response = new SuccessResponse<>(
                HttpServletResponse.SC_OK,
                ResponseMessage.UPDATE_OPTION_SUCCESS,
                dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessNoResponse> delete(@PathVariable Long id) {
        optionService.delete(id);
        return ResponseEntity.ok(new SuccessNoResponse(200, ResponseMessage.DELETE_SUCCESS));
    }

    @DeleteMapping
    public ResponseEntity<SuccessNoResponse> deleteMany(@RequestBody List<Long> ids) {
        optionService.deleteMany(ids);
        return ResponseEntity.ok(new SuccessNoResponse(200, ResponseMessage.DELETE_SUCCESS));

    }
}
