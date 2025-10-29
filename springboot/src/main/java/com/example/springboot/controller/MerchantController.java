package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.Merchant;
import com.example.springboot.service.MerchantService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

//标记Controller类
@RestController
@RequestMapping("/merchant")
public class MerchantController {
    @Resource
    MerchantService merchantService;
    //RequestMapping映射
@GetMapping("/selectAll")
    public Result selectAll(){
    List<Merchant> list = merchantService.selectAll();
    return Result.success(list);
}
    /**
     * 描述：分页查询
     */
    @GetMapping("/selectPage")
    public Result page(Merchant merchant,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize) {
      PageInfo<Merchant> pageInfo=   merchantService.selectPage(merchant, pageNum, pageSize);
        return Result.success(pageInfo);
    }
    @PostMapping("/save")
    public Result save(@RequestBody Merchant merchant){
        merchantService.saveMerchant(merchant);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        merchantService.deleteById(id);
        return Result.success();
    }
    @PostMapping("/update")
    public Result updateById(@RequestBody Merchant merchant) {
        merchantService.updateById(merchant);
        return Result.success();
    }

}
