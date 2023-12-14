package com.rjx.regis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rjx.regis.entity.AddressBook;
import com.rjx.regis.mapper.AddressBookMapper;
import com.rjx.regis.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
