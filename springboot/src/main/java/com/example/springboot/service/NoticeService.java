package com.example.springboot.service;
import cn.hutool.core.date.DateUtil;
import com.example.springboot.common.config.JwtTokenUtils;
import com.example.springboot.dao.NoticeDao;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Notice;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class NoticeService {
    @Resource
    NoticeDao noticeDao;

    public List<Notice> selectAll() {
        return noticeDao.selectAll(null);
    }

    public void updateById(Notice notice) {
        noticeDao.updateById(notice);
    }

    public PageInfo<Notice> selectPage(Notice notice, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Notice> noticeList = noticeDao.selectAll(notice);
        return PageInfo.of(noticeList);
    }

    public void saveNotice(Notice notice) {
        notice.setTime(DateUtil.today());
        Account account= JwtTokenUtils.getCurrentUser();
        notice.setUserId(account.getId());
        noticeDao.insertNotice(notice);
    }

    public void deleteById(Integer id) {
        noticeDao.deleteById(id);
    }

}