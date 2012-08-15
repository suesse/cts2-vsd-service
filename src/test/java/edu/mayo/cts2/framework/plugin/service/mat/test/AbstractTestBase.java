package edu.mayo.cts2.framework.plugin.service.mat.test;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-mat-context.xml")
public abstract class AbstractTestBase {

	@Resource
	private DataSource dataSource;

	@Before
	public void cleanBefore() {
		this.doClean();
	}

	@After
	public void cleanAfter() {
		this.doClean();
	}

	private void doClean() {
		new JdbcTemplate(this.dataSource)
				.execute("TRUNCATE SCHEMA public AND COMMIT");
	}

}