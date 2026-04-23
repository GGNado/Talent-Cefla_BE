package com.giggi.ceflatalent;

import com.giggi.ceflatalent.entity.*;
import com.giggi.ceflatalent.entity.AreaManager;
import com.giggi.ceflatalent.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final CompanyRepository companyRepository;
    private final AreaManagerRepository areaManagerRepository;
    private final BusinessLineRepository businessLineRepository;
    private final CustomerRepository customerRepository;
    private final ItemRepository itemRepository;
    private final SaleRepository saleRepository;

    private static final String BASE_PATH = "src/main/java/com/giggi/ceflatalent/daRappresentare/";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Override
    public void run(String... args) throws Exception {
        if (saleRepository.count() > 0) {
            log.info("Database already populated. Skipping data load.");
            return;
        }

        log.info("Starting data load from CSV files...");

        // 1. Load Companies
        Map<String, Company> companiesMap = loadCompanies();
        log.info("Loaded {} companies.", companiesMap.size());

        // 2. Load Area Managers
        Map<String, AreaManager> areaManagersMap = loadAreaManagers();
        log.info("Loaded {} area managers.", areaManagersMap.size());

        // 3. Load Business Lines
        Map<String, BusinessLine> businessLinesMap = loadBusinessLines();
        log.info("Loaded {} business lines.", businessLinesMap.size());

        // 4. Load Customers
        Map<String, Customer> customersMap = loadCustomers(areaManagersMap);
        log.info("Loaded {} customers.", customersMap.size());

        // 5. Load Items
        Map<String, Item> itemsMap = loadItems(businessLinesMap);
        log.info("Loaded {} items.", itemsMap.size());

        // 6. Load Sales
        loadSales(companiesMap, customersMap, itemsMap);
        log.info("Data load completed successfully.");
    }

    private Map<String, Company> loadCompanies() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(BASE_PATH + "COMPANY_LOOKUP.csv"));
        Map<String, Company> map = new HashMap<>();
        for (int i = 1; i < lines.size(); i++) {
            String[] parts = parseCsvLineAndClean(lines.get(i));
            if (parts.length < 2) continue;
            DummyCompany dummy = new DummyCompany(parts[0], parts[1]);
            Company company = Company.builder()
                    .description(dummy.descCompany())
                    .build();
            map.put(dummy.idCompany(), companyRepository.save(company));
        }
        return map;
    }

    private Map<String, AreaManager> loadAreaManagers() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(BASE_PATH + "AREA_MANAGER_LOOKUP.csv"));
        Map<String, AreaManager> map = new HashMap<>();
        for (int i = 1; i < lines.size(); i++) {
            String[] parts = parseCsvLineAndClean(lines.get(i));
            if (parts.length < 2) continue;
            DummyAreaManager dummy = new DummyAreaManager(parts[0], parts[1]);
            AreaManager areaManager = AreaManager.builder()
                    .description(dummy.descAreaManager())
                    .build();
            map.put(dummy.idAreaManager(), areaManagerRepository.save(areaManager));
        }
        return map;
    }

    private Map<String, BusinessLine> loadBusinessLines() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(BASE_PATH + "ITEM_BUSINESS_LINE_LOOKUP.csv"));
        Map<String, BusinessLine> map = new HashMap<>();
        for (int i = 1; i < lines.size(); i++) {
            String[] parts = parseCsvLineAndClean(lines.get(i));
            if (parts.length < 2) continue;
            DummyBusinessLine dummy = new DummyBusinessLine(parts[0], parts[1]);
            BusinessLine businessLine = BusinessLine.builder()
                    .description(dummy.descBusinessLine())
                    .build();
            map.put(dummy.idBusinessLine(), businessLineRepository.save(businessLine));
        }
        return map;
    }

    private Map<String, Customer> loadCustomers(Map<String, AreaManager> areaManagersMap) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(BASE_PATH + "CUSTOMER_LOOKUP.csv"));
        Map<String, Customer> map = new HashMap<>();
        for (int i = 1; i < lines.size(); i++) {
            String[] parts = parseCsvLineAndClean(lines.get(i));
            if (parts.length < 4) continue;
            DummyCustomer dummy = new DummyCustomer(parts[0], parts[1], parts[2], parts[3]);
            
            AreaManager am = areaManagersMap.get(dummy.idAreaManager());
            if (am == null) {
                log.warn("Area Manager not found for ID: {}. Skipping customer {}.", dummy.idAreaManager(), dummy.idsCustomer());
                continue;
            }

            Country country = null;
            try {
                if (dummy.idCountry() != null && !"N/A".equals(dummy.idCountry())) {
                    country = Country.valueOf(dummy.idCountry());
                }
            } catch (IllegalArgumentException e) {
                log.warn("Invalid country code: {}. Setting to null for customer {}.", dummy.idCountry(), dummy.idsCustomer());
            }

            Customer customer = Customer.builder()
                    .description(dummy.descCustomer())
                    .country(country)
                    .areaManager(am)
                    .build();
            map.put(dummy.idsCustomer(), customerRepository.save(customer));
        }
        return map;
    }

    private Map<String, Item> loadItems(Map<String, BusinessLine> businessLinesMap) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(BASE_PATH + "ITEM_LOOKUP.csv"));
        Map<String, Item> map = new HashMap<>();
        for (int i = 1; i < lines.size(); i++) {
            String[] parts = parseCsvLineAndClean(lines.get(i));
            if (parts.length < 3) continue;
            DummyItem dummy = new DummyItem(parts[0], parts[1], parts[2]);

            // Use only the first occurrence for items to avoid duplicates
            if (map.containsKey(dummy.idsItem())) continue;

            BusinessLine bl = businessLinesMap.get(dummy.idBusinessLine());
            if (bl == null) {
                log.warn("Business Line not found for ID: {}. Skipping item {}.", dummy.idBusinessLine(), dummy.idsItem());
                continue;
            }

            Item item = Item.builder()
                    .description(dummy.descItem())
                    .businessLine(bl)
                    .build();
            map.put(dummy.idsItem(), itemRepository.save(item));
        }
        return map;
    }

    private void loadSales(Map<String, Company> companiesMap, Map<String, Customer> customersMap, Map<String, Item> itemsMap) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(BASE_PATH + "SALES.csv"));
        for (int i = 1; i < lines.size(); i++) {
            String[] parts = parseCsvLineAndClean(lines.get(i));
            if (parts.length < 8) continue;
            DummySale dummy = new DummySale(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7]);

            Company company = companiesMap.get(dummy.idCompany());
            Customer customer = customersMap.get(dummy.idsCustomer());
            Item item = itemsMap.get(dummy.idsItem());

            if (company == null || customer == null || item == null) {
                log.warn("Missing references for sale line {}. Skipping.", i);
                continue;
            }

            Sale sale = Sale.builder()
                    .company(company)
                    .orderNum(Long.parseLong(dummy.idOrderNum()))
                    .customer(customer)
                    .item(item)
                    .orderDate(parseDate(dummy.idOrderDate()))
                    .invoiceDate(parseDate(dummy.idInvoiceDate()))
                    .revenues(new BigDecimal(dummy.valRevenues()))
                    .cost(new BigDecimal(dummy.valCost()))
                    .build();
            saleRepository.save(sale);
        }
    }

    private String[] parseCsvLine(String line) {
        // Simple CSV parser that handles quotes
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
    }

    private String cleanValue(String val) {
        if (val == null) return null;
        val = val.trim();
        if (val.startsWith("\"") && val.endsWith("\"")) {
            val = val.substring(1, val.length() - 1);
        }
        return val;
    }
    
    // Override parseCsvLine to also clean values
    private String[] parseCsvLineAndClean(String line) {
        String[] parts = parseCsvLine(line);
        for (int i = 0; i < parts.length; i++) {
            parts[i] = cleanValue(parts[i]);
        }
        return parts;
    }

    // Update methods to use parseCsvLineAndClean
    // (I'll fix this in the actual code below)

    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty() || "0".equals(dateStr)) return null;
        return LocalDate.parse(dateStr, DATE_FORMATTER);
    }
}
