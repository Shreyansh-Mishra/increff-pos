package com.increff.employee.dao;

import java.time.Instant;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.increff.employee.controller.SalesReportController;
import com.increff.employee.pojo.OrderPojo;

@Repository
public class OrderDao extends AbstractDao {
	private String delete_id = "delete from OrderPojo o where id=:id";
	private String select_all = "select o from OrderPojo o order by o.id DESC";
//	private String select_date = "select DATE(o.time) as date, count(o.id) as invoiced_orders_count from OrderPojo o where o.time >= :startDate and o.time<=:endDate group by DATE(o.time)";
	private String select_id = "select o from OrderPojo o where id=:id";
	private static final Logger logger = Logger.getLogger(SalesReportController.class);
	
	
	public void insertOrder(OrderPojo o) {
		em().persist(o);
	}
	
	public int delete(int id) {
		Query query = em().createQuery(delete_id);
		query.setParameter("id", id);
		return query.executeUpdate();
	}
	
	public List<OrderPojo> selectAll(){
		TypedQuery<OrderPojo> query = getQuery(select_all, OrderPojo.class);
		return query.getResultList();
	}
	
	public OrderPojo selectId(int id){
		TypedQuery<OrderPojo> query = getQuery(select_id, OrderPojo.class);
		query.setParameter("id", id);
		return query.getSingleResult();
	}
	
	public List<Object[]> selectByDate(Instant startDate,Instant endDate){
		Query query = em().createNativeQuery("	select date(time),"+
			    " sum(bcd.total_revenue),"+
			    " convert(sum(bcd.items_count),signed),"+
			    " count(id)"+
			    " from orderpojo"+
			    " inner join"+ 
			" (select  orderId,convert(count(productId), signed) as items_count,"+
			" sum(quantity*sellingPrice) as total_revenue"+
			" from orderitempojo"+
			" group by orderId) as bcd"+
			" on orderpojo.id=bcd.orderId where date(orderpojo.time) between :startDate and :endDate"+
			" group by date(time)");
//		TypedQuery<Object[]> query = getQuery(select_date, Object[].class);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<Object[]> o = query.getResultList();
		for(Object[] or: o) {
			logger.info(or[0]);
			logger.info(or[1]);
			logger.info(or[2]);
			logger.info(or[3]);
		}
		return o;
	}
	
	public List<Object[]> selectBrandCategorySalesByDate(Instant startDate, Instant endDate){
		Query query = em().createNativeQuery("select brand, category, c.quantity, c.revenue from brandpojo "
				+ "inner join "
				+ "(select b.quantity as quantity, b.revenue as revenue, brand_category from productpojo "
				+ "inner join "
				+ "(select a.productId as productId, convert(sum(a.quantity),signed) as quantity, sum(a.quantity*a.sellingPrice) as revenue from orderpojo "
				+ "inner join "
				+ "(select orderId, productId , quantity, sellingPrice "
				+ "from orderitempojo) as a "
				+ "on orderpojo.id=a.orderId where date(orderpojo.time) between :startDate and :endDate group by productId) as b "
				+ "on productpojo.id=b.productId) as c "
				+ "on brandpojo.id = c.brand_category;");
		
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<Object[]> o = query.getResultList();
		return o;
	}

}
