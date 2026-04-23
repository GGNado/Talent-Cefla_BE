-- 1. Totale Ricavi e Costi per Azienda
SELECT 
    c.description AS azienda, 
    SUM(s.revenues) AS totale_ricavi, 
    SUM(s.cost) AS totale_costi,
    SUM(s.revenues - s.cost) AS profitto
FROM sales s
JOIN companies c ON s.company_id = c.id
GROUP BY c.description
ORDER BY totale_ricavi DESC;

-- 2. I 10 Clienti che hanno generato più fatturato
SELECT 
    cu.description AS cliente, 
    SUM(s.revenues) AS totale_fatturato,
    COUNT(s.id) AS numero_ordini
FROM sales s
JOIN customers cu ON s.customer_id = cu.id
GROUP BY cu.description
ORDER BY totale_fatturato DESC
LIMIT 10;

-- 3. Andamento mensile delle vendite (Ricavi vs Costi)
SELECT 
    DATE_TRUNC('month', s.order_date) AS mese, 
    SUM(s.revenues) AS ricavi, 
    SUM(s.cost) AS costi
FROM sales s
GROUP BY mese
ORDER BY mese;

-- 4. Performance per Business Line (Categoria Prodotto)
SELECT 
    bl.description AS business_line, 
    SUM(s.revenues) AS ricavi,
    AVG(s.revenues - s.cost) AS profitto_medio_per_vendita
FROM sales s
JOIN items i ON s.item_id = i.id
JOIN business_lines bl ON i.business_line_id = bl.id
GROUP BY bl.description
ORDER BY ricavi DESC;

-- 5. Classifica degli Area Manager per profitto generato
SELECT 
    am.description AS area_manager, 
    SUM(s.revenues - s.cost) AS profitto_totale
FROM sales s
JOIN customers cu ON s.customer_id = cu.id
JOIN area_managers am ON cu.area_manager_id = am.id
GROUP BY am.description
ORDER BY profitto_totale DESC;

-- 6. Distribuzione delle vendite per Paese (Country)
SELECT 
    cu.country, 
    COUNT(s.id) AS numero_vendite, 
    SUM(s.revenues) AS totale_ricavi
FROM sales s
JOIN customers cu ON s.customer_id = cu.id
GROUP BY cu.country
ORDER BY totale_ricavi DESC;
