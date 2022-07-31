package com.lkw.springbootrj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lkw.springbootrj.entity.AddressBook;
import com.lkw.springbootrj.mapper.AddressBookMapper;
import com.lkw.springbootrj.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
