package com.ssk.hfb.service;

import com.ssk.hfb.mapper.CategoryMapper;
import com.ssk.hfb.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CategoryService {


    @Autowired
    private CategoryMapper categoryMapper;


    public List<Category> queryCategoryList(){
        List<Category> categories = categoryMapper.selectAll();
        return categories;
    }


    public String queryCNameById(Integer cId) {
        Category c = new Category();
        c.setId(cId);
        Category category = categoryMapper.selectByPrimaryKey(c);
        if(category==null){
            return "";
        }
        return category.getName();
    }

    public void save(Category category) {
        category.setCreateTime(new Date());
        category.setStatus(true);
        categoryMapper.insert(category);
    }

    public void updateCategory(List<Integer> ids,Boolean freeze) {
        for(Integer id:ids){
            Category category = new Category();
            category.setId(id);
            category.setStatus(freeze);
            categoryMapper.updateByPrimaryKeySelective(category);
        }
    }
}
