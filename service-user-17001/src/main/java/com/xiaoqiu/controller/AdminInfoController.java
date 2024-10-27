package com.xiaoqiu.controller;

import com.github.pagehelper.PageInfo;
import com.xiaoqiu.bo.CreateAdminBO;
import com.xiaoqiu.bo.ResetPwdBO;
import com.xiaoqiu.pojo.Admin;
import com.xiaoqiu.resp.R;
import com.xiaoqiu.service.IAdminService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author xiaoqiu
 */
@RestController
@RequestMapping("admin_info")
@Slf4j
public class AdminInfoController {

    @Autowired
    private IAdminService adminService;

    @PostMapping("create")
    public R<Void> create(@Valid @RequestBody CreateAdminBO createAdminBO) {
        adminService.createAdmin(createAdminBO);
        return R.ok();
    }

    @PostMapping("list")
    public R<PageInfo<Admin>> list(@RequestParam(value = "accountName") String accountName,
                                   @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                   @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        return R.ok(adminService.getAdminList(accountName, pageNo, pageSize));
    }

    @PostMapping("delete")
    public R<Void> delete(String username) {
        adminService.deleteAdmin(username);
        return R.ok();
    }

    @PostMapping("resetPwd")
    public R<Void> resetPwd(@RequestBody ResetPwdBO resetPwdBO) {
        adminService.resetPwd(resetPwdBO);
        return R.ok();
    }

}
