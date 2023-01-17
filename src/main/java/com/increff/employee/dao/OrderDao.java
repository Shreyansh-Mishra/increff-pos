package com.increff.employee.dao;

import java.time.Instant;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.OrderPojo;

@Repository
public class OrderDao extends AbstractDao {
	private String delete_id = "delete from OrderPojo o where id=:id";
	private String select_all = "select o from OrderPojo o order by o.id DESC";
//	private String select_date = "select DATE(o.time) as date, count(o.id) as invoiced_orders_count from OrderPojo o where o.time >= :startDate and o.time<=:endDate group by DATE(o.time)";
	private String select_id = "select o from OrderPojo o where id=:id";
	private String select_date = "select o from OrderPojo o where date(time) between date(:startDate) and date(:endDate)";
	
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
		return getSingle(query);
	}
	
	public List<OrderPojo> selectBetweenDates(Instant startDate, Instant endDate){
		TypedQuery<OrderPojo> query = getQuery(select_date, OrderPojo.class);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.getResultList();
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
