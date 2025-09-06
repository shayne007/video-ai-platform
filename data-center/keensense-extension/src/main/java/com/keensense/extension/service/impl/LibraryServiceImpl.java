package com.keensense.extension.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.extension.entity.LibraryInfo;
import com.keensense.extension.mapper.LibraryMapper;
import com.keensense.extension.service.ILibraryService;
import org.springframework.stereotype.Service;

/***
 * @description:
 * @author jingege
 * @return:
 */
@Service
public class LibraryServiceImpl extends ServiceImpl<LibraryMapper,LibraryInfo> implements
	ILibraryService {
}
