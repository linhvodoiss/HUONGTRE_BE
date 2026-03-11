package com.fpt.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fpt.dto.UserDTO;
import com.fpt.dto.UserListDTO;
import com.fpt.entity.User;
import com.fpt.form.ChangePasswordForm;
import com.fpt.payload.PaginatedResponse;
import com.fpt.payload.SuccessNoResponse;
import com.fpt.payload.SuccessResponse;
import com.fpt.service.interfaces.IPaymentOrderService;
import com.fpt.service.interfaces.IUserService;

import com.fpt.constant.ApiPaths;
import com.fpt.constant.ResponseMessage;

@CrossOrigin("*")
@RestController
@RequestMapping(value = ApiPaths.ACCOUNT)
// @PreAuthorize("hasRole('ADMIN')")
@Validated
public class AdminController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IPaymentOrderService paymentOrderService;

    @GetMapping
    public ResponseEntity<PaginatedResponse<UserListDTO>> getAllUsers(
            Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Boolean isActive) {
        Page<User> entityPages = userService.getAllUser(pageable, search, status, isActive);
        List<UserListDTO> dtos = userService.convertToDto(entityPages.getContent());
        Page<UserListDTO> dtoPage = new PageImpl<>(dtos, pageable, entityPages.getTotalElements());

        PaginatedResponse<UserListDTO> response = new PaginatedResponse<>(
                dtoPage,
                HttpServletResponse.SC_OK,
                ResponseMessage.GET_LIST_USER_SUCCESS);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<UserDTO>> createUser(@RequestBody @Valid UserDTO userDTO) {
        try {
            UserDTO createdUser = userService.addUserByAdmin(userDTO);
            return ResponseEntity.ok(new SuccessResponse<>(200, ResponseMessage.USER_CREATED_SUCCESS, createdUser));
        } catch (Exception e) {
            return ResponseEntity
                    .status(400)
                    .body(new SuccessResponse<>(400, e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<UserDTO>> updateUser(
            @PathVariable Long id,
            @RequestBody @Valid UserDTO userDTO) {
        try {
            UserDTO updatedUser = userService.updateUserByAdmin(id, userDTO);
            return ResponseEntity.ok(new SuccessResponse<>(200, ResponseMessage.USER_UPDATED_SUCCESS, updatedUser));
        } catch (Exception e) {
            return ResponseEntity
                    .status(400)
                    .body(new SuccessResponse<>(400, e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessNoResponse> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok(new SuccessNoResponse(200, ResponseMessage.DELETE_SUCCESS));
    }

    @DeleteMapping
    public ResponseEntity<SuccessNoResponse> deleteMany(@RequestBody List<Long> ids) {
        userService.deleteMany(ids);
        return ResponseEntity.ok(new SuccessNoResponse(200, ResponseMessage.DELETE_SUCCESS));
    }

    @PatchMapping("/ban/{id}")
    public ResponseEntity<SuccessNoResponse> toggleBanUser(@PathVariable Long id) {
        try {
            boolean isActive = userService.updateActiveStatus(id);
            String message = isActive ? ResponseMessage.UNBANNED_ACCOUNT : ResponseMessage.BANNED_ACCOUNT;

            return ResponseEntity.ok(new SuccessNoResponse(200, message));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new SuccessNoResponse(500, ResponseMessage.SYSTEM_ERROR));
        }
    }

    @PatchMapping("/changePassword/{userId}")
    public ResponseEntity<?> changePasswordAdmin(
            @PathVariable Long userId,
            @RequestBody ChangePasswordForm request) {
        try {
            userService.changePasswordAdmin(userId, request);
            Map<String, Object> response = new HashMap<>();
            response.put("code", HttpStatus.OK.value());
            response.put("message", ResponseMessage.CHANGE_PASSWORD_SUCCESS);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", HttpStatus.BAD_REQUEST.value());
            response.put("message", ResponseMessage.CHANGE_PASSWORD_FAILED + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

    }

    @GetMapping("/dashboard")
    public ResponseEntity<SuccessResponse<Map<String, Object>>> getAdminDashboard() {
        long totalCustomers = userService.countCustomerAccounts();
        long totalOrders = paymentOrderService.countTotalOrders();
        double totalRevenue = paymentOrderService.getTotalRevenue();
        Map<String, Long> ordersByStatus = paymentOrderService.countOrdersByStatus();

        Map<String, Long> ordersByPaymentMethod = paymentOrderService.countOrdersByPaymentMethod();
        Map<String, Double> revenueByPaymentMethod = paymentOrderService.revenueByPaymentMethod();

        Map<String, Object> data = new HashMap<>();
        data.put("totalCustomers", totalCustomers);
        data.put("totalOrders", totalOrders);
        data.put("totalRevenue", totalRevenue);
        data.put("ordersByStatus", ordersByStatus);
        data.put("ordersByPaymentMethod", ordersByPaymentMethod);
        data.put("revenueByPaymentMethod", revenueByPaymentMethod);

        SuccessResponse<Map<String, Object>> response = new SuccessResponse<>(
                HttpServletResponse.SC_OK,
                ResponseMessage.GET_DASHBOARD_SUCCESS,
                data);

        return ResponseEntity.ok(response);
    }

}
