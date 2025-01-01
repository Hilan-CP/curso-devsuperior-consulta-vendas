package com.devsuperior.dsmeta.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import com.devsuperior.dsmeta.entities.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {

	@Query("SELECT new com.devsuperior.dsmeta.dto.SaleSummaryDTO(s.seller.name, SUM(s.amount)) "
			+ "FROM Sale s "
			+ "WHERE s.date BETWEEN :minDate AND :maxDate "
			+ "GROUP BY s.seller")
	List<SaleSummaryDTO> searchSaleSummary(LocalDate minDate, LocalDate maxDate);
	
	@Query(value = "SELECT s "
			+ "FROM Sale s "
			+ "JOIN FETCH s.seller "
			+ "WHERE UPPER(s.seller.name) LIKE UPPER(CONCAT('%', :sellerName,'%')) "
				+ "AND s.date BETWEEN :minDate AND :maxDate",
			countQuery = "SELECT COUNT(s) FROM Sale s JOIN s.seller "
					+ "WHERE UPPER(s.seller.name) LIKE UPPER(CONCAT('%', :sellerName,'%')) "
					+ "AND s.date BETWEEN :minDate AND :maxDate")
	Page<Sale> searchBySellerAndDate(LocalDate minDate, LocalDate maxDate, String sellerName, Pageable pageable);
}
