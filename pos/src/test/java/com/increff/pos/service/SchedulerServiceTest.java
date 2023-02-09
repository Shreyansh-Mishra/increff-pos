package com.increff.pos.service;

import com.increff.pos.dao.SchedulerDao;
import com.increff.pos.pojo.SchedulerPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.Assert.assertEquals;
public class SchedulerServiceTest extends AbstractUnitTest{
    @Autowired
    SchedulerService schedulerService;
    @Autowired
    SchedulerDao schedulerDao;

    @Test
    public void insertScheduler(){
        SchedulerPojo scheduler = new SchedulerPojo();
        scheduler.setDate(Instant.now().truncatedTo(ChronoUnit.DAYS));
        scheduler.setRevenue(100.00);
        scheduler.setInvoiced_items_count(10);
        scheduler.setInvoiced_orders_count(2);
        schedulerService.insertScheduler(scheduler);
        List<SchedulerPojo> SchedulerData = schedulerDao.select(Instant.now().minus(1, ChronoUnit.DAYS), Instant.now().plus(1, ChronoUnit.DAYS));
        assertEquals(1, SchedulerData.size());
        assertEquals(100.00, SchedulerData.get(0).getRevenue(), 0.001);
        assertEquals((Integer) 10, SchedulerData.get(0).getInvoiced_items_count());
        assertEquals((Integer) 2, SchedulerData.get(0).getInvoiced_orders_count());
    }

    @Test
    public void testGetScheduler() throws ApiException {
        SchedulerPojo scheduler = new SchedulerPojo();
        scheduler.setDate(Instant.now().truncatedTo(ChronoUnit.DAYS));
        scheduler.setRevenue(100.00);
        scheduler.setInvoiced_items_count(10);
        scheduler.setInvoiced_orders_count(2);
        schedulerDao.insert(scheduler);
        List<SchedulerPojo> SchedulerData = schedulerService.selectSchedulerData(Instant.now().minus(1, ChronoUnit.DAYS), Instant.now().plus(1, ChronoUnit.DAYS));
        assertEquals(1, SchedulerData.size());
        assertEquals(100.00, SchedulerData.get(0).getRevenue(), 0.001);
        assertEquals((Integer) 10, SchedulerData.get(0).getInvoiced_items_count());
        assertEquals((Integer) 2, SchedulerData.get(0).getInvoiced_orders_count());
    }

    @Test(expected = ApiException.class)
    public void testGetSchedulerException() throws ApiException {
        SchedulerPojo scheduler = new SchedulerPojo();
        scheduler.setDate(Instant.now().truncatedTo(ChronoUnit.DAYS));
        scheduler.setRevenue(100.00);
        scheduler.setInvoiced_items_count(10);
        scheduler.setInvoiced_orders_count(2);
        schedulerDao.insert(scheduler);
        schedulerService.selectSchedulerData(Instant.now().plus(1, ChronoUnit.DAYS), Instant.now().minus(1, ChronoUnit.DAYS));
    }
}
