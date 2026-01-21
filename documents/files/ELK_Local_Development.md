# ELK Stack - Quick Start Guide

## 🚀 Get Started in 5 Minutes

### Prerequisites

✅ **Local PostgreSQL** running on `localhost:5432`  
✅ **Docker & Docker Compose** installed  
✅ **Port 8080** available for your application

> **Note:** The application runs locally (not in Docker) and connects to your local PostgreSQL. Only the ELK stack runs in Docker.

---

### Step 1: Start the ELK Stack

```bash
# Navigate to your project directory
cd /Users/pyawalkar/Documents/Personal\ Portfolio/backend/portfolio

# Start ELK stack (Elasticsearch, Logstash, Kibana)
docker-compose up -d

# Check if all services are running
docker-compose ps
```

### Step 2: Wait for Services to Start

The ELK stack takes about 2-3 minutes to fully initialize. Monitor the logs:

```bash
# Watch Elasticsearch
docker-compose logs -f elasticsearch

# Wait for: "Cluster health status changed from [YELLOW] to [GREEN]"
```

### Step 3: Start Your Application

The Spring Boot application runs locally (outside Docker):

```bash
# Using Maven
mvn spring-boot:run -Dspring-boot.run.profiles=local

# Or from your IDE: Run PortfolioApplication.java with profile 'local'
```

The application will automatically connect to Logstash at `localhost:5000`.

### Step 4: Verify Services

**Check Service Health:**

```bash
# Elasticsearch
curl http://localhost:9200

# Logstash
curl http://localhost:9600

# Application
curl http://localhost:8080/actuator/health
```

**Expected Ports:**
- 🟢 Application: http://localhost:8080 (runs locally)
- 🟢 Kibana: http://localhost:5601 (Docker)
- 🟢 Elasticsearch: http://localhost:9200 (Docker)
- 🟢 Logstash: http://localhost:5000 (Docker)
- 🟢 PostgreSQL: localhost:5432 (local)

### Step 5: Configure Kibana

1. **Open Kibana**: http://localhost:5601

2. **Create Data View** (formerly called Index Pattern):
   - Click on the hamburger menu (☰) → **Management** → **Stack Management**
   - Under **Kibana** section, click **Data Views** (in older versions: Index Patterns)
   - Click **Create data view**
   - Enter name: `portfolio-logs`
   - Enter index pattern: `portfolio-logs-*`
   - Select **@timestamp** as the time field
   - Click **Create data view**

### Step 6: Generate Test Logs

Use the test controller to generate logs immediately:

```bash
# Generate all types of logs at once (requires authentication)
curl -u "pratik:pratik123" http://localhost:8080/admin/test/logs/all

# Response includes: DEBUG, INFO, WARN, ERROR, and ERROR with Exception
```

> **Note:** The endpoint requires Basic Authentication. Use your admin credentials (default: pratik/pratik123).

**What logs are generated:**
- ✅ DEBUG level log
- ✅ INFO level log
- ✅ WARN level log
- ✅ ERROR level log
- ✅ ERROR with full stack trace

You can also trigger logs by:
- Making API calls to any endpoint (automatically logged)
- Checking Swagger UI: http://localhost:8080/swagger-ui.html
- Any application activity (startup, shutdown, requests, etc.)

### Step 7: View Logs in Kibana

1. **Open Discover**:
   - Click hamburger menu (☰) → **Analytics** → **Discover**
   - Select `portfolio-logs-*` index pattern
   - You should see logs appearing!

2. **Useful Searches**:
   ```
   level: "ERROR"                    # View only errors
   level: "WARN" OR level: "ERROR"   # Warnings and errors
   message: "test"                   # Search in message
   logger_name: "*Controller"        # Logs from controllers
   ```

## 📊 Quick Visualizations

### View Error Rate

1. Go to **Analytics** → **Dashboard** → **Create dashboard**
2. Click **Create visualization**
3. Select **Area** chart
4. Configure:
   - **Horizontal axis**: @timestamp
   - **Vertical axis**: Count of documents
   - **Filters**: Add `level: "ERROR"`
5. Save as "Error Rate"

### View Log Levels Distribution

1. Click **Create visualization** → **Donut**
2. Configure:
   - **Slice by**: Terms → `level.keyword`
   - **Metric**: Count
3. Save as "Log Levels"

## 🔍 Useful Kibana Queries (KQL)

```kql
# Errors only
level: "ERROR"

# Errors from your application code
level: "ERROR" AND logger_name: com.backend.portfolio.*

# Logs from last 15 minutes
@timestamp >= now-15m

# SQL queries (if enabled)
logger_name: org.hibernate.SQL

# Security logs
logger_name: org.springframework.security.*

# Logs with exceptions
stack_trace: *

# Combine filters
level: "ERROR" AND @timestamp >= now-1h AND environment: "local"
```

## 🛠️ Troubleshooting

### No Logs in Kibana?

1. **Check if indices exist**:
   ```bash
   curl http://localhost:9200/_cat/indices?v
   ```
   Look for `portfolio-logs-*` indices.

2. **Check Logstash is receiving logs**:
   ```bash
   docker-compose logs -f logstash | grep -i "portfolio"
   ```

3. **Verify application is running**:
   ```bash
   # Check if app is running on port 8080
   lsof -i :8080
   
   # Check app logs for Logstash connection
   # If running via Maven, check console output
   # If running via IDE, check IDE console
   ```

4. **Generate test logs** to verify pipeline:
   ```bash
   curl -u "pratik:pratik123" http://localhost:8080/admin/test/logs/all
   ```

5. **Check application can reach Logstash**:
   ```bash
   # Verify Logstash is listening on port 5000
   nc -zv localhost 5000
   ```

### ELK Stack Not Starting?

```bash
# Check container status
docker-compose ps

# Check logs for errors
docker-compose logs elasticsearch
docker-compose logs logstash
docker-compose logs kibana

# Restart ELK services
docker-compose restart

# If still failing, start fresh
docker-compose down -v
docker-compose up -d
```

### Application Won't Start?

```bash
# Check if PostgreSQL is running
lsof -i :5432

# Start PostgreSQL if needed (macOS)
brew services start postgresql

# Check if port 8080 is in use
lsof -i :8080

# Kill process if needed
kill -9 <PID>

# Verify database exists
psql -U postgres -l | grep portfolio
```

See [RUN_LOCAL.md](RUN_LOCAL.md) for complete local setup guide.

### Memory Issues?

The ELK stack requires at least 4GB RAM. If you have limited memory:

1. **Memory is already optimized** in `docker-compose.yml`:
   ```yaml
   # Elasticsearch: 512MB
   - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
   
   # Logstash: 256MB
   - "LS_JAVA_OPTS=-Xms256m -Xmx256m"
   ```

2. **Check Docker Desktop memory allocation**:
   - Docker Desktop → Settings → Resources
   - Allocate at least 4GB RAM total

## 📝 Test Endpoints Reference

| Endpoint | Method | Auth Required | Description |
|----------|--------|---------------|-------------|
| `/admin/test/logs/all` | GET | Yes (Basic Auth) | Generate all log levels (DEBUG, INFO, WARN, ERROR, ERROR with Exception) |

**Authentication:**
- Username: `pratik`
- Password: `pratik123`

**Example:**
```bash
curl -u "pratik:pratik123" http://localhost:8080/admin/test/logs/all
```

**Other Ways to Generate Logs:**
- Access any API endpoint (automatically logged)
- Application startup/shutdown (automatically logged)
- Database operations (SQL queries logged in DEBUG mode)
- Security events (login attempts, authorization, etc.)

## 🎯 Next Steps

1. ✅ **Explore Kibana**: Create dashboards and visualizations
2. ✅ **Review Configuration**: Check `logback-spring.xml` for logging patterns
3. ✅ **Test with Real Traffic**: Use your API endpoints to generate meaningful logs
4. ✅ **Set Up Alerts**: Configure alerts for critical errors in Kibana
5. ✅ **Customize Logging**: Adjust log levels in `application-local.yml`
6. ✅ **Production Setup**: See [PRODUCTION_READY.md](../../PRODUCTION_READY.md) for deployment

## 📚 Resources

- **Local Setup Guide**: [RUN_LOCAL.md](RUN_LOCAL.md)
- **Kibana**: http://localhost:5601
- **Elasticsearch API**: http://localhost:9200
- **Application API**: http://localhost:8080/swagger-ui.html
- **Logback Config**: `src/main/resources/templates/logback-spring.xml`

## 🔄 Common Commands

```bash
# Start ELK stack
docker-compose up -d

# Stop ELK stack
docker-compose down

# View logs
docker-compose logs -f

# Restart a specific service
docker-compose restart elasticsearch
docker-compose restart logstash
docker-compose restart kibana

# Clean up everything (removes volumes)
docker-compose down -v

# Check disk usage
docker system df
```

---

**Need Help?** 
- 📖 See [RUN_LOCAL.md](RUN_LOCAL.md) for complete local development guide
- 🐳 Check Docker logs: `docker-compose logs`
- 🔍 Review application logs in your IDE console or `logs/application.log`
