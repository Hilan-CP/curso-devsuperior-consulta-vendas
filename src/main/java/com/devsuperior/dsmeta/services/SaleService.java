package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.dto.SaleReportDTO;
import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;
	
	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

	public List<SaleReportDTO> getReport(String minDate, String maxDate, String name) {
		LocalDate endDate = parseMaxDate(maxDate);
		LocalDate beginDate = parseMinDate(minDate, endDate);
		List<Sale> result = repository.searchBySellerAndDate(beginDate, endDate, name);
		return result.stream().map(sale -> new SaleReportDTO(sale)).toList();
	}

	public List<SaleSummaryDTO> getSummary(String minDate, String maxDate) {
		LocalDate endDate = parseMaxDate(maxDate);
		LocalDate beginDate = parseMinDate(minDate, endDate);
		List<SaleSummaryDTO> result = repository.searchSaleSummary(beginDate, endDate);
		return result;
	}
	
	private LocalDate parseMaxDate(String maxDate) {
		if(maxDate.equals("")) {
			return LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
		}
		else {
			return LocalDate.parse(maxDate);
		}
	}
	
	private LocalDate parseMinDate(String minDate, LocalDate maxDate) {
		if(minDate.equals("")) {
			return maxDate.minusYears(1L);
		}
		else {
			return LocalDate.parse(minDate);
		}
	}
}
