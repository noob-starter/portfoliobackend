# 🚀 Render Deployment Guide - Portfolio Backend

## ✅ Pre-Deployment Checklist

Your application is **READY** for Render deployment after the fixes applied. Follow this guide step-by-step.

---

## 📋 Required Environment Variables

Configure these in Render Dashboard → Environment tab:

### **Critical (Required)**

```bash
# Spring Profile
SPRING_PROFILES_ACTIVE=prod

# Database Configuration (from Supabase)
# Format: jdbc:postgresql://db.<project-ref>.supabase.co:5432/postgres
DATABASE_URL=jdbc:postgresql://db.your-project-ref.supabase.co:5432/postgres
DATABASE_USERNAME=postgres.your-project-ref
DATABASE_PASSWORD=your-supabase-password

# Port (Render sets this automatically, but good to have)
PORT=8080
```

### **Application Configuration**

```bash
# Server URL (your Render web service URL)
SERVER_URL=https://your-app-name.onrender.com

# CORS - Frontend URL(s) - comma-separated for multiple
CORS_ALLOWED_ORIGINS=https://your-frontend-domain.com,https://www.your-frontend-domain.com

# Frontend URL reference
FRONTEND_URL=https://your-frontend-domain.com
```

### **Optional but Recommended**

```bash
# JVM Memory (if you want to override defaults)
JAVA_OPTS=-Xms256m -Xmx512m -XX:+UseContainerSupport

# Logging Level
LOGGING_LEVEL_ROOT=INFO
LOGGING_LEVEL_APP=INFO
```

---

## 🗄️ Database Setup on Supabase

### Step 1: Get Your Supabase Connection Details

1. **Go to your Supabase Project:**
   - Login to [Supabase Dashboard](https://app.supabase.com)
   - Select your project (or create one if needed)

2. **Get Database Connection Info:**
   - Go to **Settings** → **Database**
   - Scroll down to **Connection string** section
   - Copy the **URI** (Connection Pooling mode recommended)
   
3. **Extract Connection Details:**
   - **Host**: `db.<project-ref>.supabase.co`
   - **Port**: `5432` (Direct) or `6543` (Pooler)
   - **Database**: `postgres`
   - **Username**: `postgres.<project-ref>`
   - **Password**: Your Supabase database password

### Step 2: Initialize Database Schema

**Option A: Using Supabase SQL Editor (Recommended)**

1. Go to **SQL Editor** in Supabase Dashboard
2. Click **"New Query"**
3. Copy and paste each SQL file content in order:
   - `documents/scripts/manages/create/create.sql`
   - `documents/scripts/manages/create/create_admin.sql`
   - `documents/scripts/manages/insert/insert.sql`
   - `documents/scripts/manages/insert/insert_admin.sql`
4. Run each script

**Option B: Using psql Command Line**

```bash
# Connect to Supabase database
psql "postgresql://postgres.[project-ref]:[password]@db.[project-ref].supabase.co:5432/postgres"

# Run scripts in order
\i documents/scripts/manages/create/create.sql
\i documents/scripts/manages/create/create_admin.sql
\i documents/scripts/manages/insert/insert.sql
\i documents/scripts/manages/insert/insert_admin.sql
```

**Option C: Use the init script**

If you have the init script available:
```bash
# Make it executable
chmod +x prod_scripts/init-supabase-db.sh

# Run it (will prompt for Supabase details)
./prod_scripts/init-supabase-db.sh
```

### Step 3: Verify Schema

In Supabase Dashboard → **Table Editor**, verify these tables exist:
- profiles
- addresses
- contacts
- experiences
- experience_points
- projects
- project_points
- technologies
- educations
- achievements
- faqs
- inquires
- admin (for authentication)

---

## 🌐 Web Service Setup on Render

### Step 1: Create Web Service

1. **In Render Dashboard:**
   - Click **"New +"** → **"Web Service"**
   - Connect your GitHub/GitLab repository
   - Select your repository and branch (`main`)

2. **Basic Settings:**
   - **Name**: `portfolio-backend` (or your choice)
   - **Region**: Same as database (e.g., Oregon)
   - **Branch**: `main`
   - **Root Directory**: Leave blank
   - **Runtime**: Docker
   - **Plan**: Free (with limitations) or Starter ($7/month)

3. **Advanced Settings:**
   - **Docker Command**: Leave blank (uses Dockerfile's ENTRYPOINT)
   - **Port**: 8080 (automatically detected)

### Step 2: Configure Environment Variables

Go to **Environment** tab and add all variables from the section above.

**Quick Setup with Supabase:**

```bash
# Application
SPRING_PROFILES_ACTIVE=prod
PORT=8080

# Supabase Database Connection
DATABASE_URL=jdbc:postgresql://db.your-project-ref.supabase.co:5432/postgres
DATABASE_USERNAME=postgres.your-project-ref
DATABASE_PASSWORD=your-supabase-db-password

# Server & CORS
SERVER_URL=https://portfolio-backend.onrender.com
CORS_ALLOWED_ORIGINS=https://your-frontend.com
```

**Important Notes:**
- Replace `your-project-ref` with your actual Supabase project reference
- Use **port 5432** for direct connection (recommended for Render)
- Use **port 6543** for connection pooling (if you have many connections)
- Get password from Supabase → Settings → Database → Database password

### Step 3: Deploy

1. Click **"Create Web Service"**
2. Render will:
   - Clone your repository
   - Build Docker image (takes 5-10 minutes first time)
   - Run container
   - Monitor health checks

3. **Monitor Logs:**
   - Watch the **Logs** tab for build progress
   - Look for: `Started PortfolioApplication in X seconds`

---

## ✅ Post-Deployment Verification

### 1. Health Check

```bash
# Should return: {"status":"UP"}
curl https://your-app-name.onrender.com/actuator/health
```

### 2. Swagger UI

```bash
# Visit in browser (requires ADMIN authentication)
https://your-app-name.onrender.com/swagger-ui.html
```

### 3. Test Public API

```bash
# Should return profile data
curl https://your-app-name.onrender.com/api/v1/profiles
```

### 4. Test CORS

```bash
# Should return CORS headers
curl -H "Origin: https://your-frontend.com" \
     -H "Access-Control-Request-Method: GET" \
     -X OPTIONS \
     https://your-app-name.onrender.com/api/v1/profiles
```

---

## 🔧 Troubleshooting

### Build Fails

**Issue**: Docker build fails

**Solutions:**
- Check logs for Maven dependency errors
- Verify Java 21 compatibility
- Ensure `pom.xml` is valid

### Application Won't Start

**Issue**: Container starts but application crashes

**Solutions:**

1. **Supabase Database Connection:**
   ```bash
   # Verify environment variables are set correctly
   # Format: jdbc:postgresql://db.<project-ref>.supabase.co:5432/postgres
   
   # Common issues:
   # ❌ Missing "jdbc:postgresql://" prefix
   # ❌ Wrong port (use 5432 for direct, 6543 for pooler)
   # ❌ Wrong database name (should be "postgres" not "portfolio")
   # ❌ Wrong username format (should be postgres.<project-ref>)
   ```

2. **Check Logs:**
   - Render Dashboard → Your Service → Logs
   - Look for `Connection refused` or `Authentication failed`
   - If "SSL required", add `?sslmode=require` to DATABASE_URL

3. **Verify Database Schema:**
   - Go to Supabase → Table Editor
   - Ensure all tables are created
   - Check if schema matches JPA entities

4. **Test Supabase Connection:**
   ```bash
   # From your local machine
   psql "postgresql://postgres.[ref]:[password]@db.[ref].supabase.co:5432/postgres"
   
   # List tables
   \dt
   ```

### Health Check Fails

**Issue**: Deploy succeeds but health check fails

**Solutions:**
- Check if PORT environment variable is set
- Verify actuator is accessible: `/actuator/health`
- Check if database connection is working

### CORS Errors

**Issue**: Frontend can't connect to backend

**Solutions:**

1. **Verify CORS_ALLOWED_ORIGINS:**
   ```bash
   # Include all frontend URLs (no trailing slash)
   CORS_ALLOWED_ORIGINS=https://app.vercel.app,https://www.app.vercel.app
   ```

2. **Check Security Config:**
   - Public endpoints should allow GET requests
   - POST to `/api/v1/inquires` should be public

### Slow Cold Starts (Free Tier)

**Issue**: First request takes 30-60 seconds

**Solution:**
- Render free tier spins down after 15 min inactivity
- Use a cron job to ping every 10 minutes:
  ```bash
  # Cron-job.org or similar
  curl https://your-app-name.onrender.com/actuator/health
  ```
- Or upgrade to Starter plan ($7/month) for persistent service

---

## 🔐 Security Best Practices

### ✅ Already Implemented

- ✅ No hardcoded passwords in config
- ✅ Environment variables for secrets
- ✅ Non-root user in Docker
- ✅ CORS configured per environment
- ✅ Rate limiting enabled
- ✅ Swagger UI protected in production
- ✅ Stateless sessions
- ✅ BCrypt password encryption

### Additional Recommendations

1. **Use Render Secret Files** (for sensitive configs):
   - Render Dashboard → Secret Files
   - Add files like application-secrets.yml

2. **Enable Render's Auto-Deploy**:
   - Automatically deploys on git push
   - Can be disabled in Settings if needed

3. **Set Up Health Alerts**:
   - Render Dashboard → Notifications
   - Get notified if service goes down

4. **Supabase Database Backups**:
   - Supabase automatically backs up your database daily
   - Go to Supabase → Database → Backups to restore if needed
   - Free plan: 7-day retention
   - Pro plan: 30-day retention

5. **Supabase Connection Pooling**:
   - For better performance, use pooler port 6543
   - Transaction mode: Best for web apps
   - Session mode: For long-running connections

---

## 📊 Performance Optimization

### Memory Configuration

**Current Settings** (optimized for Render):
```bash
JAVA_OPTS=-Xms256m -Xmx512m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0
```

### Connection Pooling

**Already configured in `application-prod.yml`:**
- Maximum pool size: 10
- Minimum idle: 5
- Connection timeout: 30s

### Rate Limiting

**Already configured:**
- Admin: 30 requests/min
- Public: 60 requests/min

---

## 🎯 Cost Estimates

### Render Costs

**Free Tier:**
- **Web Service**: 750 hours/month (shared across all services)
- **Limitations**:
  - Spins down after 15 min inactivity
  - Slower cold starts (~30-60 seconds)
  - Limited resources

**Starter Plan ($7/month):**
- **Web Service**: Always on, no spin down
- **Benefits**:
  - Instant response times
  - More CPU/memory
  - Persistent service

### Supabase Costs

**Free Tier:**
- **Database**: Up to 500MB
- **API Requests**: Unlimited
- **Bandwidth**: 5GB
- **No credit card required**

**Pro Plan ($25/month):**
- **Database**: 8GB included
- **Daily backups**: 30-day retention
- **Better performance**
- **Priority support**

### Recommended Setup

**For Development/Testing:**
- Render: Free tier
- Supabase: Free tier
- **Total**: $0/month

**For Production:**
- Render: Starter ($7/month) - No cold starts
- Supabase: Free or Pro ($25/month)
- **Total**: $7-32/month

---

## 📞 Support & Resources

### Render Resources
- **Documentation**: https://render.com/docs
- **Status**: https://status.render.com
- **Community**: https://community.render.com

### Application Endpoints
- **Swagger UI**: `https://your-app.onrender.com/swagger-ui.html`
- **API Docs**: `https://your-app.onrender.com/v3/api-docs`
- **Health**: `https://your-app.onrender.com/actuator/health`
- **Metrics**: `https://your-app.onrender.com/actuator/metrics` (ADMIN only)

### Monitoring
- **Render Dashboard**: Real-time logs and metrics
- **Application Logs**: Render → Your Service → Logs
- **Database**: Render → Your Database → Metrics

---

## 🚀 Quick Deploy Commands

```bash
# 1. Ensure Supabase database is ready
# - Tables created
# - Admin user inserted
# - Connection tested

# 2. Commit latest changes
git add .
git commit -m "Ready for Render deployment with Supabase"
git push origin main

# 3. Render will auto-deploy (if enabled)
# Or manually deploy from Render Dashboard

# 4. Verify deployment
curl https://your-app-name.onrender.com/actuator/health

# 5. Check logs
# Render Dashboard → Logs tab
```

---

## ✨ Success Checklist

- [ ] Supabase database is set up and accessible
- [ ] Database schema initialized in Supabase
- [ ] Admin user created in database
- [ ] Supabase connection tested from local machine
- [ ] Web service created on Render
- [ ] All environment variables set (including Supabase credentials)
- [ ] First deployment successful
- [ ] Health check returns `UP`
- [ ] Database connection working (check logs)
- [ ] Swagger UI accessible (with admin credentials)
- [ ] Public API endpoints working
- [ ] CORS working with frontend
- [ ] Admin endpoints protected
- [ ] Frontend successfully connects to backend

---

## 🎉 You're Live!

Once all checks pass, your Portfolio Backend API is successfully deployed on Render!

**Next Steps:**
1. Update frontend with your Render backend URL
2. Test all features end-to-end
3. Monitor logs for any issues
4. Set up uptime monitoring (e.g., UptimeRobot)
5. Configure custom domain (optional, Render supports it)

---

**Made with ❤️ for Render deployment**

For questions or issues, check Render's documentation or open an issue in your repository.

