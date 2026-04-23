# Analisi Vendite - Documentazione API & Database

Questo documento riassume la struttura del database e gli endpoint disponibili per l'analisi dei dati nel progetto Cefla-talent.

## Database Schema

Il database PostgreSQL utilizza un modello relazionale ottimizzato per il reporting:

### Tabelle Principali
- **`sales`**: Cuore del sistema. Contiene i record delle transazioni con `order_date`, `revenues`, `cost` e chiavi esterne verso clienti e prodotti.
- **`customers`**: Anagrafica clienti, collegata a un **Area Manager**.
- **`items`**: Catalogo prodotti, collegati a una **Business Line**.
- **`companies`**: Entità aziendali (Cefla, ecc.).
- **`area_managers`** & **`business_lines`**: Tabelle di classificazione per raggruppare i dati.

---

## Endpoint di Analisi (REST API)

Tutti gli endpoint restituiscono dati in formato JSON.

### 1. Trend e Proiezioni (`/api/reports/trend`)
- **`GET /api/reports/trend`**: Restituisce lo storico mensile e una proiezione basata su **Regressione Lineare** (linea retta).
- **`GET /api/reports/trend/seasonal`**: Restituisce lo storico e una proiezione **Stagionale** (segue i cicli degli anni precedenti).

### 2. Previsioni Avanzate (`/api/reports/forecast/advanced`)
Restituisce un oggetto complesso per la dashboard:
- **Monte Carlo**: Fornisce `revenueMin` e `revenueMax` per mostrare l'area di incertezza statistica.
- **Churn Risk**: Elenca i clienti che non ordinano da troppo tempo rispetto alla loro media storica, con un `riskScore`.

### 3. KPI e Ranking
- **`GET /api/reports/summary`**: Totali globali (Ricavi, Margine, Volume ordini).
- **`GET /api/reports/top-performers/{type}`**: Classifiche. Tipi validi: `TOP_CUSTOMERS`, `TOP_ITEMS`, `TOP_AREA_MANAGERS`.
- **`GET /api/reports/detailed`**: Estrazione completa di tutte le vendite con dettagli testuali (nomi clienti, descrizioni prodotti).

---