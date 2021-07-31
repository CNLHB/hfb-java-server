package com.ssk.hfb.controller;

import com.ssk.hfb.common.enums.SucessEnum;
import com.ssk.hfb.common.result.SucessHandler;
import com.ssk.hfb.pojo.Category;
import com.ssk.hfb.service.CategoryService;
import com.ssk.hfb.utils.SmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 获取分类列表
     * @return
     */
    @GetMapping("list")
    public List<Category> queryBrandByPage() {
        return categoryService.queryCategoryList();
    }
    @PostMapping
    public  ResponseEntity<SucessHandler>  saveCategory(@RequestBody Category category){
        categoryService.save(category);
        return ResponseEntity.ok(new SucessHandler(SucessEnum.RESULT_POST_OR_PUT_SUCESS));
    }

    /**
     * 是否显示隐藏分类列表
     * @param ids  id数组
     * @param freeze  true or false
     * @return
     */
    @PutMapping
    public ResponseEntity<SucessHandler> updateCategory( @RequestParam(value = "ids") List<Integer> ids,@RequestParam(value = "freeze") Boolean freeze){
        categoryService.updateCategory(ids,freeze);
        return ResponseEntity.ok(new SucessHandler(SucessEnum.RESULT_POST_OR_PUT_SUCESS));
    }
}
