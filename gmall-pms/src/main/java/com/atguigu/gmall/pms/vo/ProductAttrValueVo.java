package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.entity.ProductAttrValueEntity;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * @author ZQ
 * @create 2019-10-31 13:54
 */
@Data
public class ProductAttrValueVo extends ProductAttrValueEntity {
    //用对象接收传递参数的实质是调用对象的setter方法进行注入
    public void setValueSelected(List<String> valueSelected){
        this.setAttrValue(StringUtils.join(valueSelected,","));
    }
}
