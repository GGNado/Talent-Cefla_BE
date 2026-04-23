package com.giggi.ceflatalent.repository;

import com.giggi.ceflatalent.dto.response.report.RankingEntryDTO;
import com.giggi.ceflatalent.dto.response.report.GlobalSummaryResponseDTO;
import com.giggi.ceflatalent.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findByOrderNum(Long orderNum);

    @Query("SELECT new com.giggi.ceflatalent.dto.response.report.RankingEntryDTO(" +
           "CAST(c.id AS string), c.description, SUM(s.revenues), SUM(s.revenues - s.cost), COUNT(s.id)) " +
           "FROM Sale s JOIN s.customer c " +
           "GROUP BY c.id, c.description " +
           "ORDER BY SUM(s.revenues) DESC")
    List<RankingEntryDTO> findTopCustomers();

    @Query("SELECT new com.giggi.ceflatalent.dto.response.report.RankingEntryDTO(" +
           "CAST(i.id AS string), i.description, SUM(s.revenues), SUM(s.revenues - s.cost), COUNT(s.id)) " +
           "FROM Sale s JOIN s.item i " +
           "GROUP BY i.id, i.description " +
           "ORDER BY SUM(s.revenues) DESC")
    List<RankingEntryDTO> findTopItems();

    @Query("SELECT new com.giggi.ceflatalent.dto.response.report.RankingEntryDTO(" +
           "CAST(am.id AS string), am.description, SUM(s.revenues), SUM(s.revenues - s.cost), COUNT(s.id)) " +
           "FROM Sale s JOIN s.customer cu JOIN cu.areaManager am " +
           "GROUP BY am.id, am.description " +
           "ORDER BY SUM(s.revenues - s.cost) DESC")
    List<RankingEntryDTO> findTopAreaManagers();

    @Query(value = "SELECT DATE_TRUNC('month', s.order_date) as date, " +
           "SUM(s.revenues) as revenue, SUM(s.cost) as cost, SUM(s.revenues - s.cost) as profit " +
           "FROM Sales s " +
           "GROUP BY date ORDER BY date", nativeQuery = true)
    List<Object[]> findMonthlyTrendNative();

    @Query("SELECT SUM(s.revenues), SUM(s.cost), SUM(s.revenues - s.cost), AVG(s.revenues - s.cost), " +
           "COUNT(s.id), COUNT(DISTINCT s.customer.id), COUNT(DISTINCT s.item.id) " +
           "FROM Sale s")
    Object getGlobalSummaryRaw();

    @Query("SELECT s.customer.description, s.orderDate " +
           "FROM Sale s " +
           "ORDER BY s.customer.id, s.orderDate")
    List<Object[]> findOrderDatesPerCustomer();
}