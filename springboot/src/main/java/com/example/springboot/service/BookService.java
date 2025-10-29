package com.example.springboot.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.springboot.common.ResultCode;
import com.example.springboot.common.config.JwtTokenUtils;
import com.example.springboot.dao.BookDao;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Book;
import com.example.springboot.exception.ServiceException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BookService {
    @Resource
    BookDao bookDao;

    public List<Book> selectAll() {
        return bookDao.selectAll(null);
    }

    public void updateById(Book book) {
        Book dbBook = bookDao.selectById(book.getId());
        if (ObjectUtil.isNotEmpty(dbBook)&&dbBook.getId()!=book.getId()){
            throw new ServiceException(ResultCode.ERROR);
        }
        bookDao.updateById(book);
    }
    public PageInfo<Book> selectPage(Book book, Integer pageNum, Integer pageSize) {
        Account account = JwtTokenUtils.getCurrentUser();
        if(account.getRole().equals("merchant"))
            book.setMerchantId(account.getId());
        else
            book.setMerchantId(0);
        PageHelper.startPage(pageNum, pageSize);
        List<Book> bookList = bookDao.selectAll(book);
        return PageInfo.of(bookList);
    }
    public PageInfo<Book> FilterPage(Integer typeId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Book> bookList = bookDao.selectByTypeId(typeId);
        return PageInfo.of(bookList);
    }
    public void saveBook(Book book) {
        if (ObjectUtil.isEmpty(book.getPic())) {
            book.setPic("http://localhost:9090/files/default.jpg");
        }
        book.setTime(DateUtil.today());
        book.setMerchantId(JwtTokenUtils.getCurrentUser().getId());
        bookDao.insertBook(book);
    }

    public void deleteById(Integer id) {
        bookDao.deleteById(id);
    }
    public List<Book> selectByTypeId(Integer typeId){
       return bookDao.selectByTypeId(typeId);
    }

    public List<Book> selectBookByName(String name) {
        Book book = new Book();
        book.setName(name);
        Account account = JwtTokenUtils.getCurrentUser();
        if(account.getRole().equals("merchant"))
            book.setMerchantId(account.getId());
        else
            book.setMerchantId(0);
        return bookDao.selectBookByName(book);
    }

}