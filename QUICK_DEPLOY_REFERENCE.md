# ⚡ Quick Deploy Reference - Supabase + Render

**Ultra-fast deployment checklist and commands**

---

## 🎯 Prerequisites (One-Time Setup)

### ✅ Supabase Database Ready
```bash
# 1. Supabase project created
# 2. Database schema initialized (all SQL scripts run)
# 3. Admin user created
# 4. Connection tested
```

### ✅ GitHub Repository Ready
```bash
git status  # All changes committed
git push origin main  # Code pushed
```

---

## 🚀 Deploy in 3 Steps

### Step 1: Get Supabase Connection Details

**From Supabase Dashboard → Settings → Database:**

```plaintext
Host:     db.[YOUR-PROJECT-REF].supabase.co
Port:     5432
Database: postgres
User:     postgres.[YOUR-PROJECT-REF]
Password: [YOUR-DATABASE-PASSWORD]
```

**Format for Render:**
```bash
DATABASE_URL=jdbc:postgresql://db.[PROJECT-REF].supabase.co:5432/postgres
DATABASE_USERNAME=postgres.[PROJECT-REF]
DATABASE_PASSWORD=[YOUR-PASSWORD]
```

---

### Step 2: Create Web Service on Render

**Render Dashboard → New → Web Service**

| Setting | Value |
|---------|-------|
| **Repository** | Your GitHub repo |
| **Branch** | `main` |
| **Runtime** | Docker |
| **Region** | Choose closest to users |
| **Plan** | Free or Starter ($7/month) |

---

### Step 3: Set Environment Variables

**Render Dashboard → Your Service → Environment**

**Copy-paste these (update with your values):**

```bash
# Required
SPRING_PROFILES_ACTIVE=prod
DATABASE_URL=jdbc:postgresql://db.YOUR-PROJECT-REF.supabase.co:5432/postgres
DATABASE_USERNAME=postgres.YOUR-PROJECT-REF
DATABASE_PASSWORD=your-supabase-password
PORT=8080

# Application URLs
SERVER_URL=https://your-app-name.onrender.com
CORS_ALLOWED_ORIGINS=https://your-frontend.com

# Optional
JAVA_OPTS=-Xms256m -Xmx512m -XX:+UseContainerSupport
```

**Replace:**
- `YOUR-PROJECT-REF` → Your Supabase project reference
- `your-supabase-password` → Your database password
- `your-app-name` → Your Render service name
- `your-frontend.com` → Your frontend URL

---

## ✅ Verification Commands

### Health Check
```bash
curl https://your-app-name.onrender.com/actuator/health
# Expected: {"status":"UP"}
```

### Test Public API
```bash
curl https://your-app-name.onrender.com/api/v1/profiles
# Should return profile data
```

### Test Swagger UI
```
https://your-app-name.onrender.com/swagger-ui.html
# Requires ADMIN authentication
```

---

## 🔧 Common Issues & Quick Fixes

### ❌ Database Connection Failed

**Check:**
```bash
# 1. Format correct?
DATABASE_URL=jdbc:postgresql://db.[REF].supabase.co:5432/postgres
#             ^^^^^^^^^^^^^^^ Must include "jdbc:postgresql://"

# 2. Username correct?
DATABASE_USERNAME=postgres.[REF]
#                 ^^^^^^^^^^^^^^^ Must include project ref

# 3. Password correct?
# Copy from Supabase → Settings → Database

# 4. Add SSL if needed
DATABASE_URL=jdbc:postgresql://db.[REF].supabase.co:5432/postgres?sslmode=require
```

### ❌ Health Check Fails

**Check Render Logs:**
```
Render Dashboard → Logs → Look for:
- "Started PortfolioApplication" ✅ (Good)
- "Connection refused" ❌ (Database issue)
- "Authentication failed" ❌ (Wrong password)
```

### ❌ CORS Errors

**Check:**
```bash
# Must match EXACTLY (no trailing slash)
CORS_ALLOWED_ORIGINS=https://your-frontend.com

# Multiple origins:
CORS_ALLOWED_ORIGINS=https://app.com,https://www.app.com
```

---

## 📊 Deployment Timeline

| Phase | Duration | What's Happening |
|-------|----------|------------------|
| **Push to GitHub** | 5 seconds | Code uploaded |
| **Render Build** | 8-12 min | First time (cached after) |
| **Container Start** | 1-2 min | App starting |
| **Health Check** | 30 sec | Verifying app is up |
| **Total** | **~10-15 min** | First deployment |

---

## 🎯 Post-Deployment Checklist

- [ ] Health check returns `{"status":"UP"}`
- [ ] Public endpoints work (GET /api/v1/profiles)
- [ ] Swagger UI loads (requires ADMIN auth)
- [ ] Database connection successful (check logs)
- [ ] CORS working with frontend
- [ ] Admin endpoints protected
- [ ] No errors in Render logs

---

## 📞 Quick Links

| Resource | URL |
|----------|-----|
| **Supabase Dashboard** | https://app.supabase.com |
| **Render Dashboard** | https://dashboard.render.com |
| **Your App Health** | https://your-app.onrender.com/actuator/health |
| **Your App Swagger** | https://your-app.onrender.com/swagger-ui.html |
| **Render Logs** | Dashboard → Your Service → Logs |
| **Supabase SQL Editor** | Dashboard → SQL Editor |

---

## 🔄 Redeployment (Updates)

```bash
# Make changes to code
git add .
git commit -m "Your update message"
git push origin main

# Render auto-deploys (if enabled)
# Build time: 3-5 min (cached dependencies)
```

---

## 💡 Pro Tips

1. **Keep Warm (Free Tier):**
   ```bash
   # Use cron-job.org to ping every 10 minutes
   curl https://your-app.onrender.com/actuator/health
   ```

2. **Monitor Logs:**
   - Render Dashboard → Logs (real-time)
   - Filter for "ERROR" or "WARN"

3. **Test Locally First:**
   ```bash
   # Before deploying
   export DATABASE_URL=jdbc:postgresql://db.[REF].supabase.co:5432/postgres
   export DATABASE_USERNAME=postgres.[REF]
   export DATABASE_PASSWORD=[PASS]
   export SPRING_PROFILES_ACTIVE=prod
   
   ./mvnw spring-boot:run
   ```

4. **Database Backups:**
   - Supabase auto-backs up daily
   - Free: 7-day retention
   - Restore: Dashboard → Database → Backups

---

## 🆘 Need More Help?

- **Full Guide**: See `RENDER_DEPLOYMENT_GUIDE.md`
- **Supabase Setup**: See `SUPABASE_SETUP.md`
- **Render Docs**: https://render.com/docs
- **Supabase Docs**: https://supabase.com/docs

---

**Last Updated**: January 21, 2026  
**Stack**: Spring Boot + Supabase + Render  
**Status**: Production Ready ✅

