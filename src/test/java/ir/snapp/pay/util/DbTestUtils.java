package ir.snapp.pay.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;

@Component
public class DbTestUtils {
	@Autowired
	private DataSource dataSource;

	public void cleanAllTables() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, getAllTables());
	}

	private String[] getAllTables() {
		return new String[]{
				"user_authority",
				"my_user",
		};
	}
}


