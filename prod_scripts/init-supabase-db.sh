#!/bin/bash

# ================================================
# Supabase Database Initialization Script
# ================================================
# This script initializes your Supabase database with all necessary tables and data
# 
# Prerequisites:
# 1. PostgreSQL client (psql) installed: brew install postgresql
# 2. Supabase project created
# 3. Connection details from Supabase dashboard

set -e  # Exit on error

echo "================================================"
echo "Supabase Database Initialization"
echo "================================================"
echo ""

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check if psql is installed
if ! command -v psql &> /dev/null; then
    echo -e "${RED}❌ Error: PostgreSQL client (psql) is not installed${NC}"
    echo "Install it with: brew install postgresql"
    exit 1
fi

# Check if .env.prod file exists
if [ ! -f .env.prod ]; then
    echo -e "${YELLOW}⚠️  Warning: .env file not found${NC}"
    echo "Please create a .env file with your Supabase credentials"
    echo "Use .env.supabase.example as a template"
    echo ""
    echo "Required variables:"
    echo "  - SUPABASE_DB_URL"
    echo "  - SUPABASE_DB_USER"
    echo "  - SUPABASE_DB_PASSWORD"
    echo ""
    read -p "Do you want to enter credentials manually? (y/n): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
    
    # Get credentials manually
    echo ""
    echo "Enter your Supabase connection details:"
    echo "(Find these in Supabase Dashboard > Settings > Database)"
    echo ""
    
    read -p "Host (e.g., aws-0-us-east-1.pooler.supabase.com): " SUPABASE_HOST
    read -p "Port (default 6543): " SUPABASE_PORT
    SUPABASE_PORT=${SUPABASE_PORT:-6543}
    read -p "Database (default postgres): " SUPABASE_DB
    SUPABASE_DB=${SUPABASE_DB:-postgres}
    read -p "Username (e.g., postgres.xxxxxxxxxxxxx): " SUPABASE_USER
    read -sp "Password: " SUPABASE_PASSWORD
    echo ""
    
    # Construct connection string
    PGPASSWORD="$SUPABASE_PASSWORD"
    CONNECTION_STRING="postgresql://${SUPABASE_USER}:${SUPABASE_PASSWORD}@${SUPABASE_HOST}:${SUPABASE_PORT}/${SUPABASE_DB}"
else
    # Load from .env.prod file
    echo "Loading credentials from .env file..."
    source .env.prod
    
    # Extract connection details from JDBC URL
    # Format: jdbc:postgresql://host:port/database?...
    SUPABASE_HOST=$(echo $SUPABASE_DB_URL | sed -n 's|.*://\([^:]*\):.*|\1|p')
    SUPABASE_PORT=$(echo $SUPABASE_DB_URL | sed -n 's|.*:\([0-9]*\)/.*|\1|p')
    SUPABASE_DB=$(echo $SUPABASE_DB_URL | sed -n 's|.*/\([^?]*\).*|\1|p')
    SUPABASE_USER=$SUPABASE_DB_USER
    SUPABASE_PASSWORD=$SUPABASE_DB_PASSWORD
    
    PGPASSWORD="$SUPABASE_PASSWORD"
    CONNECTION_STRING="postgresql://${SUPABASE_USER}:${SUPABASE_PASSWORD}@${SUPABASE_HOST}:${SUPABASE_PORT}/${SUPABASE_DB}"
fi

export PGPASSWORD

echo ""
echo -e "${GREEN}✓${NC} Credentials loaded"
echo "Host: $SUPABASE_HOST"
echo "Port: $SUPABASE_PORT"
echo "Database: $SUPABASE_DB"
echo "User: $SUPABASE_USER"
echo ""

# Test connection
echo "Testing connection to Supabase..."
if psql "$CONNECTION_STRING" -c "SELECT version();" > /dev/null 2>&1; then
    echo -e "${GREEN}✓${NC} Connection successful!"
else
    echo -e "${RED}❌ Connection failed!${NC}"
    echo "Please check your credentials and try again."
    exit 1
fi

echo ""
echo -e "${YELLOW}⚠️  WARNING: This will DROP all existing tables and data!${NC}"
read -p "Are you sure you want to continue? (yes/no): " -r
if [[ ! $REPLY =~ ^[Yy][Ee][Ss]$ ]]; then
    echo "Aborted."
    exit 0
fi

echo ""
echo "================================================"
echo "Running Database Initialization Scripts"
echo "================================================"
echo ""

# Run scripts in order
echo "1/5 Running drop.sql..."
psql "$CONNECTION_STRING" -f documents/scripts/manages/drop/drop.sql
echo -e "${GREEN}✓${NC} Drop completed"

echo ""
echo "2/5 Running create.sql..."
psql "$CONNECTION_STRING" -f documents/scripts/manages/create/create.sql
echo -e "${GREEN}✓${NC} Tables created"

echo ""
echo "3/5 Running create_admin.sql..."
psql "$CONNECTION_STRING" -f documents/scripts/manages/create/create_admin.sql
echo -e "${GREEN}✓${NC} Admin tables created"

echo ""
echo "4/5 Running insert.sql..."
psql "$CONNECTION_STRING" -f documents/scripts/manages/insert/insert.sql
echo -e "${GREEN}✓${NC} Data inserted"

echo ""
echo "5/5 Running insert_admin.sql..."
psql "$CONNECTION_STRING" -f documents/scripts/manages/insert/insert_admin.sql
echo -e "${GREEN}✓${NC} Admin data inserted"

echo ""
echo "================================================"
echo "Verification"
echo "================================================"
echo ""

echo "Checking tables..."
psql "$CONNECTION_STRING" -c "\dt"

echo ""
echo "Checking profile data..."
psql "$CONNECTION_STRING" -c "SELECT id, fname, lname, email FROM profiles;" 2>/dev/null || \
psql "$CONNECTION_STRING" -c "SELECT id, fname, lname FROM profiles;"

echo ""
echo "================================================"
echo -e "${GREEN}✅ Database initialization complete!${NC}"
echo "================================================"
echo ""
echo "Next steps:"
echo "1. Update your .env file with Supabase credentials (if not done)"
echo "2. Start your application: docker-compose -f docker-compose.prod.yml up -d"
echo "3. Check logs: docker-compose -f docker-compose.prod.yml logs -f app"
echo ""

# Unset password
unset PGPASSWORD

