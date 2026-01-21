#!/bin/bash

# Production Deployment Script for Portfolio Backend with ELK Stack
# This script deploys the application in production mode with proper configurations

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

# Script directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")/portfolio"

echo "🚀 Production Deployment - Portfolio Backend with ELK Stack"
echo "========================================================"
echo ""

# Function to print colored messages
print_success() { echo -e "${GREEN}✓ $1${NC}"; }
print_warning() { echo -e "${YELLOW}⚠ $1${NC}"; }
print_error() { echo -e "${RED}✗ $1${NC}"; }
print_info() { echo "ℹ $1"; }

# Check if running as root (some production environments require this)
if [[ $EUID -eq 0 ]] && [[ "${ALLOW_ROOT:-false}" != "true" ]]; then
   print_warning "Running as root. Set ALLOW_ROOT=true if this is intentional."
fi

# Verify we're in the correct directory
cd "$PROJECT_ROOT" || {
    print_error "Cannot navigate to project root: $PROJECT_ROOT"
    exit 1
}

print_success "Working directory: $PROJECT_ROOT"

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    print_error "Docker is not running. Please start Docker and try again."
    exit 1
fi
print_success "Docker is running"

# Check required environment variables
print_info "Checking required environment variables..."
REQUIRED_VARS=(
    "SUPABASE_DB_URL"
    "SUPABASE_DB_USER"
    "SUPABASE_DB_PASSWORD"
)

MISSING_VARS=()
for var in "${REQUIRED_VARS[@]}"; do
    if [[ -z "${!var}" ]]; then
        MISSING_VARS+=("$var")
    fi
done

if [[ ${#MISSING_VARS[@]} -gt 0 ]]; then
    print_error "Missing required environment variables:"
    for var in "${MISSING_VARS[@]}"; do
        echo "  - $var"
    done
    echo ""
    echo "Please set these variables in your environment or .env file"
    exit 1
fi

print_success "All required environment variables are set"

# Check if .env.prod file exists and load it
if [[ -f .env.prod.prod ]]; then
    print_info "Loading production environment variables from .env.prod"
    export $(grep -v '^#' .env.prod.prod | xargs)
    print_success "Environment variables loaded"
else
    print_warning "No .env.prod file found. Using system environment variables."
fi

# Verify docker-compose.prod.yml exists
if [[ ! -f docker-compose.prod.yml ]]; then
    print_error "docker-compose.prod.yml not found in $PROJECT_ROOT"
    exit 1
fi
print_success "Production docker-compose file found"

# Check if services are already running
if docker-compose -f docker-compose.prod.yml ps | grep -q "Up"; then
    print_warning "Some services are already running."
    read -p "Do you want to restart them? (y/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        print_info "Stopping existing services..."
        docker-compose -f docker-compose.prod.yml down
        print_success "Services stopped"
    else
        print_info "Keeping existing services running"
        exit 0
    fi
fi

# Pull latest images
print_info "Pulling latest Docker images..."
docker-compose -f docker-compose.prod.yml pull

# Build application image
print_info "Building application image..."
docker-compose -f docker-compose.prod.yml build app

# Start services
print_info "Starting production services..."
docker-compose -f docker-compose.prod.yml up -d

echo ""
print_info "Waiting for services to become healthy..."
echo ""

# Wait for services with timeout
wait_for_service() {
    local service=$1
    local url=$2
    local max_attempts=$3
    local attempt=1
    
    while [[ $attempt -le $max_attempts ]]; do
        if curl -sf "$url" > /dev/null 2>&1; then
            print_success "$service is healthy"
            return 0
        fi
        sleep 5
        ((attempt++))
    done
    
    print_error "$service failed to start within expected time"
    print_info "Check logs: docker-compose -f docker-compose.prod.yml logs $service"
    return 1
}

# Wait for each service
wait_for_service "Elasticsearch" "http://localhost:9200/_cluster/health" 36 || exit 1
wait_for_service "Logstash" "http://localhost:9600/_node/stats" 36 || exit 1
wait_for_service "Kibana" "http://localhost:5601/api/status" 36 || exit 1
wait_for_service "Application" "http://localhost:8080/actuator/health" 24 || exit 1

echo ""
echo "========================================================"
print_success "Production deployment completed successfully!"
echo "========================================================"
echo ""

# Display service information
print_info "Service Status:"
docker-compose -f docker-compose.prod.yml ps

echo ""
print_info "Service URLs:"
echo "  • Application Health: http://localhost:${APP_PORT:-8080}/actuator/health"
echo "  • Swagger UI:         http://localhost:${APP_PORT:-8080}/swagger-ui.html"
echo "  • Kibana:             http://localhost:${KIBANA_PORT:-5601}"
echo "  • Elasticsearch:      http://localhost:9200"
echo ""

print_info "View logs:"
echo "  docker-compose -f docker-compose.prod.yml logs -f [service]"
echo ""

print_info "Stop services:"
echo "  docker-compose -f docker-compose.prod.yml down"
echo ""

# Optional: Create Kibana index pattern automatically
if command -v jq &> /dev/null; then
    print_info "Attempting to create Kibana index pattern..."
    sleep 10  # Give Kibana more time to fully initialize
    
    # This is a placeholder - actual Kibana API calls would go here
    # Example: curl -X POST "http://localhost:5601/api/saved_objects/index-pattern/portfolio-logs-*" ...
    print_warning "Kibana index pattern creation requires manual setup or Kibana API authentication"
fi

print_success "Deployment complete! Monitor logs for any issues."
echo ""

