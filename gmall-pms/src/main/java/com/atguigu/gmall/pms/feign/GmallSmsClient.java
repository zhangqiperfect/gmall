package com.atguigu.gmall.pms.feign;

import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.pms.vo.SaleVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author ZQ
 * @create 2019-10-31 17:01
 */
@FeignClient("sms-service")
public interface GmallSmsClient {
    @PostMapping("sms/skubounds/sale")
    public Resp<Object> saveSale(@RequestBody SaleVo saleVo);
}
