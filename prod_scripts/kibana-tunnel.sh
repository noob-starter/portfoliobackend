#!/bin/bash

###############################################################################
# Kibana SSH Tunnel Script for Production Log Access
# 
# This script creates an SSH tunnel to access Kibana running on production
# Usage: ./kibana-tunnel.sh [production-server]
###############################################################################

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
DEFAULT_SERVER="your-production-server.com"
DEFAULT_USER="$USER"
KIBANA_PORT=5601
LOCAL_PORT=5601

# Function to print colored messages
print_info() {
    echo -e "${BLUE}ℹ ${NC}$1"
}

print_success() {
    echo -e "${GREEN}✓ ${NC}$1"
}

print_warning() {
    echo -e "${YELLOW}⚠ ${NC}$1"
}

print_error() {
    echo -e "${RED}✗ ${NC}$1"
}

# Function to display usage
usage() {
    cat << EOF
${GREEN}Kibana SSH Tunnel Script${NC}

${BLUE}Usage:${NC}
    $0 [OPTIONS]

${BLUE}Options:${NC}
    -s, --server SERVER     Production server hostname or IP (default: ${DEFAULT_SERVER})
    -u, --user USER        SSH user (default: current user)
    -p, --port PORT        Local port to forward to (default: ${LOCAL_PORT})
    -h, --help             Display this help message

${BLUE}Examples:${NC}
    # Connect to default server
    $0

    # Connect to specific server
    $0 -s prod.example.com

    # Connect with specific user and port
    $0 -s prod.example.com -u ubuntu -p 5602

${BLUE}After connection:${NC}
    Access Kibana at: ${GREEN}http://localhost:${LOCAL_PORT}${NC}
    Press Ctrl+C to close the tunnel

EOF
}

# Parse command line arguments
SERVER="${DEFAULT_SERVER}"
SSH_USER="${DEFAULT_USER}"

while [[ $# -gt 0 ]]; do
    case $1 in
        -s|--server)
            SERVER="$2"
            shift 2
            ;;
        -u|--user)
            SSH_USER="$2"
            shift 2
            ;;
        -p|--port)
            LOCAL_PORT="$2"
            shift 2
            ;;
        -h|--help)
            usage
            exit 0
            ;;
        *)
            print_error "Unknown option: $1"
            usage
            exit 1
            ;;
    esac
done

# Check if server is set
if [ "$SERVER" = "your-production-server.com" ]; then
    print_warning "Using default server: ${SERVER}"
    print_warning "Please update the SERVER variable in this script or use -s option"
    echo ""
    read -p "Enter your production server hostname or IP: " SERVER
    
    if [ -z "$SERVER" ]; then
        print_error "Server hostname/IP is required"
        exit 1
    fi
fi

# Display connection info
echo ""
print_info "═══════════════════════════════════════════════════════════"
print_info "  Kibana SSH Tunnel - Production Log Access"
print_info "═══════════════════════════════════════════════════════════"
echo ""
print_info "Server:      ${SERVER}"
print_info "SSH User:    ${SSH_USER}"
print_info "Remote Port: ${KIBANA_PORT}"
print_info "Local Port:  ${LOCAL_PORT}"
echo ""

# Check if port is already in use
if lsof -Pi :${LOCAL_PORT} -sTCP:LISTEN -t >/dev/null 2>&1 ; then
    print_error "Port ${LOCAL_PORT} is already in use!"
    print_info "Process using the port:"
    lsof -Pi :${LOCAL_PORT} -sTCP:LISTEN
    echo ""
    print_warning "Please choose a different port with -p option or stop the process using this port"
    exit 1
fi

# Test SSH connection first
print_info "Testing SSH connection..."
if ! ssh -q -o BatchMode=yes -o ConnectTimeout=5 ${SSH_USER}@${SERVER} exit 2>/dev/null; then
    print_warning "SSH key authentication not configured or connection failed"
    print_info "Will prompt for password when connecting..."
fi

# Create SSH tunnel
print_info "Creating SSH tunnel..."
echo ""
print_success "SSH tunnel established successfully!"
echo ""
print_info "═══════════════════════════════════════════════════════════"
print_success "Kibana is now accessible at: ${GREEN}http://localhost:${LOCAL_PORT}${NC}"
print_info "═══════════════════════════════════════════════════════════"
echo ""
print_info "Press ${YELLOW}Ctrl+C${NC} to close the tunnel"
echo ""

# Create the SSH tunnel
# -N: Don't execute remote command
# -L: Local port forwarding
# -v: Verbose (optional, remove if not needed)
ssh -N -L ${LOCAL_PORT}:localhost:${KIBANA_PORT} ${SSH_USER}@${SERVER}

# This line is reached when the tunnel is closed
echo ""
print_warning "SSH tunnel closed"

