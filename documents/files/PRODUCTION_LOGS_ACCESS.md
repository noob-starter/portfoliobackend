# Production Logs Access Guide

Complete guide for accessing and monitoring logs in production environment.

## 📋 Table of Contents

- [Overview](#overview)
- [Prerequisites](#prerequisites)
- [Method 1: Kibana Dashboard (Recommended)](#method-1-kibana-dashboard-recommended)
- [Method 2: Docker Logs](#method-2-docker-logs)
- [Method 3: File Logs](#method-3-file-logs)
- [Method 4: Log Streaming](#method-4-log-streaming)
- [Troubleshooting](#troubleshooting)
- [Security Best Practices](#security-best-practices)

---

## Overview

Your production environment uses the ELK stack (Elasticsearch, Logstash, Kibana) for comprehensive log management. All application logs are:
- ✅ Written to files (`/app/logs/application.log`)
- ✅ Sent to Docker stdout/stderr
- ✅ Forwarded to Elasticsearch via Logstash
- ✅ Visualized in Kibana

---

## Prerequisites

Before accessing production logs, ensure you have:

1. **SSH Access** to the production server
   ```bash
   ssh your-user@your-production-server.com
   ```

2. **SSH Key Authentication** (recommended)
   ```bash
   # Generate SSH key if you don't have one
   ssh-keygen -t ed25519 -C "your-email@example.com"
   
   # Copy to production server
   ssh-copy-id your-user@your-production-server.com
   ```

3. **Docker Access** on production server (user should be in docker group)
   ```bash
   # Verify docker access
   ssh your-user@your-production-server.com "docker ps"
   ```

4. **Local Tools** (for tunneling)
   - SSH client (built-in on macOS/Linux)
   - Web browser (for Kibana)

---

## Method 1: Kibana Dashboard (Recommended)

### 🎯 Best For
- Visual log analysis
- Creating dashboards and visualizations
- Searching across large log volumes
- Setting up alerts and monitoring

### Setup SSH Tunnel

#### Option A: Using the Provided Script (Easiest)

```bash
# Navigate to scripts directory
cd /Users/pyawalkar/Documents/Personal_Portfolio/backend/portfolio/prod_scripts

# Make script executable
chmod +x kibana-tunnel.sh

# Run the script
./kibana-tunnel.sh
```

**On first run, you'll be prompted to enter your production server details:**
```bash
Enter your production server hostname or IP: prod.example.com
```

**Or provide directly:**
```bash
# With specific server
./kibana-tunnel.sh -s prod.example.com

# With specific user
./kibana-tunnel.sh -s prod.example.com -u ubuntu

# With custom local port
./kibana-tunnel.sh -s prod.example.com -p 5602
```

#### Option B: Manual SSH Tunnel

```bash
# Create tunnel manually
ssh -N -L 5601:localhost:5601 your-user@your-production-server.com

# Keep this terminal open
```

#### Option C: Permanent SSH Config

Add to `~/.ssh/config`:

```ssh
Host prod-kibana
    HostName your-production-server.com
    User your-user
    LocalForward 5601 localhost:5601
    ServerAliveInterval 60
    ServerAliveCountMax 3
```

Then simply run:
```bash
ssh prod-kibana
```

### Access Kibana

1. **Open your browser** and navigate to:
   ```
   http://localhost:5601
   ```

2. **First Time Setup - Create Index Pattern:**
   - Click hamburger menu (☰) → **Management** → **Stack Management**
   - Click **Index Patterns** (under Kibana section)
   - Click **Create index pattern**
   - Enter: `portfolio-logs-*`
   - Click **Next step**
   - Select **@timestamp** as time field
   - Click **Create index pattern**

3. **View Logs:**
   - Click hamburger menu (☰) → **Analytics** → **Discover**
   - Select `portfolio-logs-*` index pattern
   - View and search your logs!

### Useful Kibana Queries (KQL)

```kql
# View only errors
level: "ERROR"

# Errors from your application
level: "ERROR" AND logger_name: com.backend.portfolio.*

# Logs from last hour
@timestamp >= now-1h

# Search in message content
message: *exception* OR message: *failed*

# Specific logger
logger_name: "com.backend.portfolio.controllers.*"

# Errors with stack traces
stack_trace: *

# Production environment logs
environment: "prod"

# Multiple conditions
level: ("ERROR" OR "WARN") AND @timestamp >= now-24h
```

### Creating Dashboards

1. **Go to:** Dashboard → Create dashboard
2. **Add visualizations:**
   - **Log Volume Over Time:** Vertical bar chart with @timestamp
   - **Error Rate:** Line chart filtered by level: "ERROR"
   - **Log Levels Distribution:** Pie chart by level.keyword
   - **Top Error Messages:** Data table with error messages
   - **Response Time:** If you log response times

3. **Save dashboard** for future use

### Setting Up Alerts

1. **Go to:** Stack Management → Rules and Connectors
2. **Create rule:**
   - Trigger: Elasticsearch query
   - Index: `portfolio-logs-*`
   - Query: `level: "ERROR"`
   - Threshold: Count > 10 in last 5 minutes
3. **Configure actions:** Email, Slack, webhook, etc.

---

## Method 2: Docker Logs

### 🎯 Best For
- Quick checks
- Real-time monitoring
- Debugging immediate issues

### Access Docker Logs

```bash
# SSH into production server
ssh your-user@your-production-server.com

# View application logs (last 100 lines)
docker logs --tail 100 portfolio-backend-prod

# Follow logs in real-time
docker logs -f portfolio-backend-prod

# View logs with timestamps
docker logs -t portfolio-backend-prod

# View logs from last hour
docker logs --since 1h portfolio-backend-prod

# View logs from specific time
docker logs --since "2024-01-20T10:00:00" portfolio-backend-prod

# Search for errors
docker logs portfolio-backend-prod 2>&1 | grep -i error

# Search for specific text
docker logs portfolio-backend-prod 2>&1 | grep "your search term"

# Save logs to file
docker logs portfolio-backend-prod > app-logs-$(date +%Y%m%d-%H%M%S).log
```

### View ELK Stack Logs

```bash
# Elasticsearch logs
docker logs -f portfolio-elasticsearch-prod

# Logstash logs (check if logs are being processed)
docker logs -f portfolio-logstash-prod

# Kibana logs
docker logs -f portfolio-kibana-prod
```

### One-Liner for Remote Access

```bash
# View logs without SSH into server
ssh your-user@your-production-server.com "docker logs --tail 100 portfolio-backend-prod"

# Follow logs remotely
ssh your-user@your-production-server.com "docker logs -f portfolio-backend-prod"

# Check errors remotely
ssh your-user@your-production-server.com "docker logs portfolio-backend-prod 2>&1 | grep -i error | tail -20"
```

---

## Method 3: File Logs

### 🎯 Best For
- Persistent log storage
- Downloading logs for analysis
- Backup and archival

### Access File Logs

```bash
# SSH into production server
ssh your-user@your-production-server.com

# Navigate to logs directory
cd /path/to/portfolio/logs

# View logs in real-time
tail -f application.log

# View last 100 lines
tail -n 100 application.log

# View first 100 lines
head -n 100 application.log

# Search for errors
grep -i "error" application.log

# Search with context (5 lines before and after)
grep -i -C 5 "error" application.log

# View logs from today
grep "$(date +%Y-%m-%d)" application.log

# Count errors
grep -i -c "error" application.log

# View only ERROR level logs
grep "ERROR" application.log

# Search for specific exception
grep -i "NullPointerException" application.log
```

### Download Logs to Local Machine

```bash
# Download current log file
scp your-user@your-production-server.com:/path/to/portfolio/logs/application.log ~/Downloads/

# Download all logs
scp -r your-user@your-production-server.com:/path/to/portfolio/logs/ ~/Downloads/production-logs/

# Download compressed
ssh your-user@your-production-server.com "cd /path/to/portfolio/logs && tar -czf - *.log" > production-logs-$(date +%Y%m%d).tar.gz
```

### Analyze Downloaded Logs Locally

```bash
# Search errors in downloaded logs
grep -i "error" ~/Downloads/application.log

# Count log levels
grep -o "ERROR\|WARN\|INFO\|DEBUG" ~/Downloads/application.log | sort | uniq -c

# Find most common errors
grep "ERROR" ~/Downloads/application.log | sort | uniq -c | sort -rn | head -10

# Extract timestamps of errors
grep "ERROR" ~/Downloads/application.log | grep -o "^[0-9-]* [0-9:]*" | sort | uniq -c
```

### Log Rotation

Logs automatically rotate based on configuration in `logback-spring.xml`:
- Max file size: 10MB
- Max history: 30 days
- Total cap: 1GB

Rotated files are named: `application-YYYY-MM-DD.N.log`

---

## Method 4: Log Streaming

### 🎯 Best For
- Continuous monitoring
- CI/CD pipelines
- Automated alerting

### Real-time Log Streaming

```bash
# Stream logs continuously
ssh your-user@your-production-server.com "docker logs -f portfolio-backend-prod" | grep -i --line-buffered "error\|warn"

# Stream with color highlighting
ssh your-user@your-production-server.com "docker logs -f portfolio-backend-prod" | grep -i --color=always --line-buffered "error\|warn\|info"

# Stream to file and display
ssh your-user@your-production-server.com "docker logs -f portfolio-backend-prod" | tee production-stream.log
```

### Create a Monitoring Script

Create `~/monitor-production.sh`:

```bash
#!/bin/bash

SERVER="your-production-server.com"
USER="your-user"
CONTAINER="portfolio-backend-prod"

echo "Monitoring production logs..."
echo "Press Ctrl+C to stop"
echo ""

# Color codes
RED='\033[0;31m'
YELLOW='\033[1;33m'
GREEN='\033[0;32m'
NC='\033[0m'

ssh ${USER}@${SERVER} "docker logs -f ${CONTAINER}" | while read line; do
    if echo "$line" | grep -q "ERROR"; then
        echo -e "${RED}$line${NC}"
    elif echo "$line" | grep -q "WARN"; then
        echo -e "${YELLOW}$line${NC}"
    elif echo "$line" | grep -q "INFO"; then
        echo -e "${GREEN}$line${NC}"
    else
        echo "$line"
    fi
done
```

Make it executable and run:
```bash
chmod +x ~/monitor-production.sh
./monitor-production.sh
```

---

## Troubleshooting

### Issue: Cannot Connect to SSH

**Solution:**
```bash
# Test SSH connection
ssh -v your-user@your-production-server.com

# Check SSH key
ssh-add -l

# Add SSH key if needed
ssh-add ~/.ssh/id_ed25519
```

### Issue: Port 5601 Already in Use

**Solution:**
```bash
# Find process using port
lsof -i :5601

# Kill the process
kill -9 <PID>

# Or use different port
./kibana-tunnel.sh -p 5602
```

### Issue: No Logs in Kibana

**Checklist:**
```bash
# 1. Check if indices exist
curl http://localhost:9200/_cat/indices?v | grep portfolio

# 2. Verify Elasticsearch is running
ssh your-user@your-production-server.com "docker ps | grep elasticsearch"

# 3. Check Logstash is receiving logs
ssh your-user@your-production-server.com "docker logs --tail 50 portfolio-logstash-prod"

# 4. Verify application is sending logs
ssh your-user@your-production-server.com "docker logs --tail 50 portfolio-backend-prod"

# 5. Check Logstash connectivity from app
ssh your-user@your-production-server.com "docker exec portfolio-backend-prod nc -zv logstash 5000"
```

### Issue: Kibana Index Pattern Not Found

**Solution:**
1. Wait 2-3 minutes for logs to be indexed
2. Check if logs are being generated:
   ```bash
   curl -u "pratik:pratik123" https://your-domain.com/admin/test/logs/all
   ```
3. Verify index exists:
   ```bash
   ssh your-user@your-production-server.com "curl -s http://localhost:9200/_cat/indices?v | grep portfolio"
   ```

### Issue: Docker Permission Denied

**Solution:**
```bash
# Add user to docker group (on production server)
sudo usermod -aG docker your-user

# Log out and log back in
exit
ssh your-user@your-production-server.com

# Verify
docker ps
```

### Issue: Logs Directory Empty

**Solution:**
```bash
# Check volume mount
ssh your-user@your-production-server.com "docker inspect portfolio-backend-prod | grep -A 10 Mounts"

# Check if app is writing logs
ssh your-user@your-production-server.com "docker exec portfolio-backend-prod ls -la /app/logs/"

# Check logback configuration
ssh your-user@your-production-server.com "docker exec portfolio-backend-prod cat /app/BOOT-INF/classes/templates/logback-spring.xml"
```

---

## Security Best Practices

### 🔒 SSH Security

1. **Use SSH Keys** (not passwords)
   ```bash
   ssh-keygen -t ed25519 -C "production-access"
   ssh-copy-id your-user@your-production-server.com
   ```

2. **Use SSH Config** with timeouts
   ```ssh
   Host prod-server
       HostName your-production-server.com
       User your-user
       IdentityFile ~/.ssh/id_ed25519_production
       ServerAliveInterval 60
       ServerAliveCountMax 3
       ConnectTimeout 10
   ```

3. **Limit SSH Access**
   - Use jump hosts/bastion servers
   - Restrict SSH to specific IPs
   - Use VPN for additional security

### 🔒 Kibana Security

**IMPORTANT:** Your current setup exposes Kibana on port 5601. Secure it by:

1. **Remove Port Exposure** (Recommended)
   
   Edit `docker-compose.prod.yml`:
   ```yaml
   kibana:
     # Comment out or remove
     # ports:
     #   - "${KIBANA_PORT:-5601}:5601"
   ```
   
   Access only via SSH tunnel (most secure)

2. **Enable Elasticsearch Security** (Alternative)
   
   Edit `docker-compose.prod.yml`:
   ```yaml
   elasticsearch:
     environment:
       - xpack.security.enabled=true
       - ELASTIC_PASSWORD=${ELASTICSEARCH_PASSWORD}
   
   kibana:
     environment:
       - ELASTICSEARCH_USERNAME=${ELASTICSEARCH_USER}
       - ELASTICSEARCH_PASSWORD=${ELASTICSEARCH_PASSWORD}
   ```

3. **Use Reverse Proxy with Auth** (Alternative)
   
   Put Kibana behind Nginx with authentication

### 🔒 Log Data Protection

1. **Sensitive Data Masking**
   - Don't log passwords, tokens, or PII
   - Use logback filters to mask sensitive data

2. **Log Retention**
   - Implement log retention policies
   - Regularly clean old logs
   - Consider encrypted backups

3. **Access Control**
   - Limit who can SSH to production
   - Use audit logs for SSH access
   - Implement least-privilege access

---

## Quick Reference Commands

### Daily Operations

```bash
# View recent errors
ssh prod "docker logs --tail 50 portfolio-backend-prod | grep ERROR"

# Check application health
ssh prod "docker ps | grep portfolio"

# View last 10 minutes of logs
ssh prod "docker logs --since 10m portfolio-backend-prod"

# Monitor errors in real-time
ssh prod "docker logs -f portfolio-backend-prod" | grep --line-buffered ERROR
```

### Emergency Response

```bash
# Immediate error check
ssh prod "docker logs --tail 200 portfolio-backend-prod | grep -i error | tail -20"

# Download crash logs
ssh prod "cd /path/to/logs && tar -czf crash-logs.tar.gz application*.log" && \
scp prod:/path/to/logs/crash-logs.tar.gz ~/Desktop/

# Check container status
ssh prod "docker ps -a | grep portfolio"

# View container resource usage
ssh prod "docker stats --no-stream portfolio-backend-prod"

# Restart application if needed (careful!)
ssh prod "docker restart portfolio-backend-prod"

# View startup logs
ssh prod "docker logs --tail 100 portfolio-backend-prod | head -50"
```

### Aliases for ~/.bashrc or ~/.zshrc

Add these to your shell configuration:

```bash
# Production server alias
alias prod='ssh your-user@your-production-server.com'

# Quick log access
alias prod-logs='ssh your-user@your-production-server.com "docker logs -f portfolio-backend-prod"'
alias prod-errors='ssh your-user@your-production-server.com "docker logs --tail 100 portfolio-backend-prod | grep ERROR"'

# Kibana tunnel
alias prod-kibana='ssh -N -L 5601:localhost:5601 your-user@your-production-server.com'

# Container status
alias prod-status='ssh your-user@your-production-server.com "docker ps | grep portfolio"'
```

Then simply run:
```bash
prod-logs       # View logs
prod-errors     # View errors
prod-kibana     # Start tunnel
```

---

## Additional Resources

- **ELK Quick Start:** [ELK_QUICK_START.md](ELK_Local_Development.md)
- **Local Development:** [RUN_LOCAL.md](RUN_LOCAL.md)
- **Production Deployment:** Production deployment scripts in `/prod_scripts/`
- **Kibana Documentation:** https://www.elastic.co/guide/en/kibana/current/index.html
- **Docker Logs:** https://docs.docker.com/engine/reference/commandline/logs/

---

## Support

If you encounter issues:

1. Check the [Troubleshooting](#troubleshooting) section
2. Verify all services are running: `docker ps`
3. Check service health: `docker-compose ps`
4. Review ELK logs: `docker logs <container-name>`
5. Ensure SSH tunnel is active: `lsof -i :5601`

---

**Last Updated:** November 2025  
**Version:** 1.0.0

