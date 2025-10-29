package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.Book;
import com.example.springboot.service.BookService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

//标记Controller类
@RestController
@RequestMapping("/book")
public class BookController {
    @Resource
    BookService bookService;
    //RequestMapping映射
@GetMapping("/selectAll")
    public Result selectAll(){
    List<Book> list = bookService.selectAll();
    return Result.success(list);
}
    /**
     * 描述：分页查询
     */
    @GetMapping("/selectPage")
    public Result page(Book book,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize) {
      PageInfo<Book> pageInfo=   bookService.selectPage(book, pageNum, pageSize);
        return Result.success(pageInfo);
    }
    @GetMapping("/filterPage")
    public Result filter(Integer typeId,
                         @RequestParam(defaultValue = "1") Integer pageNum,
                         @RequestParam(defaultValue = "5") Integer pageSize){
        PageInfo<Book> pageInfo = bookService.FilterPage(typeId, pageNum, pageSize);
        return Result.success(pageInfo);
    }
    @PostMapping("/save")
    public Result save(@RequestBody Book book){
        bookService.saveBook(book);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        bookService.deleteById(id);
        return Result.success();
    }
    @PostMapping("/update")
    public Result updateById(@RequestBody Book book) {
        bookService.updateById(book);
        return Result.success();
    }
    @GetMapping("/search")
    public Result searchByName(@RequestParam String name,
                               @RequestParam(defaultValue = "1") Integer pageNum,
                               @RequestParam(defaultValue = "5") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Book> books = bookService.selectBookByName(name);
        PageInfo<Book> pageInfo = PageInfo.of(books);
        return Result.success(pageInfo);
    }

}
