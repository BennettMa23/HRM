package com.shun.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shun.dto.Response;
import com.shun.dto.ResponseDTO;
import com.shun.entity.Staff;
import com.shun.enums.BusinessStatusEnum;
import com.shun.mapper.StaffMapper;
import com.shun.util.JWTUtil;
import com.shun.util.MD5Util;
import com.shun.vo.StaffDeptVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author : shun
 * @Date : 2022/1/30
 */

@Service
public class LoginService extends ServiceImpl<StaffMapper, Staff> {

    @Resource
    private StaffMapper staffMapper;

    public ResponseDTO login(Staff staff) {
        String password = MD5Util.MD55(staff.getPassword());
        StaffDeptVO staffDeptVO = this.staffMapper.findStaffInfo(staff.getCode(), password);
        if (staffDeptVO != null) {
            // 验证用户状态
            if (staffDeptVO.getStatus() == 1) {
                String token = JWTUtil.generateToken(staffDeptVO.getId(),password);
                return Response.success(staffDeptVO, token); // 返回员工信息和token
            }
            return Response.error(BusinessStatusEnum.STAFF_STATUS_ERROR);
        }
        return Response.error("用户名或密码错误!");
    }
}
