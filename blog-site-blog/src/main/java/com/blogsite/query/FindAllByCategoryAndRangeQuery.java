package com.blogsite.query;

import java.sql.Date;


import lombok.Data;

@Data
public class FindAllByCategoryAndRangeQuery {
	private String category;
	private Date fromdate;
	private Date todate;
}
