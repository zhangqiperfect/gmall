package com.atguigu.gmall.pms;

import com.atguigu.gmall.pms.dao.BrandDao;
import com.atguigu.gmall.pms.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GmallPmsApplicationTests {
    @Autowired
    BrandDao brandDao;
    @Autowired
    BrandService brandService;
    @Test
    void contextLoads() {
    }
    @Test
    public void test(){
//        BrandEntity brandEntity = new BrandEntity();
//        brandEntity.setDescript("java");
//        brandEntity.setFirstLetter("j");
//        brandEntity.setLogo("http://www.baidu.con");
//        brandEntity.setShowStatus(0);
//        brandEntity.setSort(1);
//        brandEntity.setName("尚硅谷大学2");
//        brandDao.insert(brandEntity);
//        Map<String, Object> map = new HashMap<>();
//        map.put("name","fds发多少");
//        brandDao.deleteByMap(map);
//        BrandEntity brandEntity = brandDao.selectById(5);
//        System.out.println(brandEntity);
//        BrandEntity brandEntity = new BrandEntity();
//        brandEntity.setBrandId(5L);
//        brandEntity.setName("华为");
//        brandDao.updateById(brandEntity);
//        IPage<BrandEntity> brandEntityIPage = brandDao.selectPage(new Page<BrandEntity>(2, 2), new QueryWrapper<BrandEntity>());
//        System.out.println(brandEntityIPage.getRecords());
//        System.out.println(brandEntityIPage.getTotal());
//        System.out.println(brandEntityIPage.getPages());

    }
}
