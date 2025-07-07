package com.video1000.task.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.task.entity.TbAnalysisTask;
import com.keensense.task.mapper.TbAnalysisTaskMapper;
import com.keensense.task.service.ITbAnalysisTaskService;

/**
 * 
 * @description:
 * @author: luowei
 * @createDate:2019年5月8日 上午10:02:46
 * @company:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PaginationTest {

	@Resource
	private TbAnalysisTaskMapper mapper;

	@Autowired
	ITbAnalysisTaskService iTbAnalysisTaskService;

	@Test
	public void tests() {
		System.out.println("----- baseMapper 自带分页 ------");
		Page<TbAnalysisTask> page = new Page<>(2, 3);
		IPage<TbAnalysisTask> tbAnalysisTaskIPage = mapper.selectPage(page, new QueryWrapper<TbAnalysisTask>());
		assertThat(page).isSameAs(tbAnalysisTaskIPage);
		System.out.println("总条数 ------> " + tbAnalysisTaskIPage.getTotal());
		System.out.println("当前页数 ------> " + tbAnalysisTaskIPage.getCurrent());
		System.out.println("当前每页显示数 ------> " + tbAnalysisTaskIPage.getSize());
		print(tbAnalysisTaskIPage.getRecords());
		System.out.println("----- baseMapper 自带分页 ------");
	}

	private <T> void print(List<T> list) {
		if (!CollectionUtils.isEmpty(list)) {
			list.forEach(System.out::println);
		}
	}
}
