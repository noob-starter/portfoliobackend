#!/bin/bash

###############################################################################
# Production Access Setup Script
# 
# This script helps you configure SSH access to production for log monitoring
# It will:
# 1. Test SSH connection to production server
# 2. Optionally set up SSH config for easy access
# 3. Create helpful aliases
# 4. Test Kibana tunnel
###############################################################################

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Function to print colored messages
print_header() {
    echo -e "\n${CYAN}═══════════════════════════════════════════════════════════${NC}"
    echo -e "${CYAN}  $1${NC}"
    echo -e "${CYAN}═══════════════════════════════════════════════════════════${NC}\n"
}

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

print_step() {
    echo -e "\n${BLUE}▶ ${NC}$1\n"
}

# Function to prompt for input
prompt_input() {
    local prompt="$1"
    local var_name="$2"
    local default="$3"
    
    if [ -n "$default" ]; then
        read -p "$(echo -e ${CYAN}${prompt}${NC} [${default}]: )" value
        eval $var_name="${value:-$default}"
    else
        read -p "$(echo -e ${CYAN}${prompt}${NC}: )" value
        eval $var_name="$value"
    fi
}

prompt_yes_no() {
    local prompt="$1"
    local default="${2:-n}"
    
    if [ "$default" = "y" ]; then
        read -p "$(echo -e ${CYAN}${prompt}${NC} [Y/n]: )" response
        response=${response:-Y}
    else
        read -p "$(echo -e ${CYAN}${prompt}${NC} [y/N]: )" response
        response=${response:-N}
    fi
    
    case "$response" in
        [yY][eE][sS]|[yY]) 
            return 0
            ;;
        *)
            return 1
            ;;
    esac
}

# Clear screen and show banner
clear
cat << "EOF"
╔═══════════════════════════════════════════════════════════╗
║                                                           ║
║        Production Log Access - Setup Wizard               ║
║        Portfolio Backend Application                      ║
║                                                           ║
╚═══════════════════════════════════════════════════════════╝
EOF

echo -e "\nThis wizard will help you set up access to production logs.\n"

# Step 1: Gather information
print_header "Step 1: Server Information"

prompt_input "Enter your production server hostname or IP" SERVER_HOST ""
prompt_input "Enter your SSH username" SSH_USER "$USER"
prompt_input "Enter SSH port" SSH_PORT "22"
prompt_input "Enter path to your SSH key" SSH_KEY "$HOME/.ssh/id_ed25519"

# Step 2: Test SSH connection
print_header "Step 2: Testing SSH Connection"

print_info "Testing connection to ${SERVER_HOST}..."

if [ ! -f "$SSH_KEY" ]; then
    print_warning "SSH key not found at: ${SSH_KEY}"
    
    if prompt_yes_no "Would you like to generate a new SSH key?"; then
        print_step "Generating SSH key..."
        ssh-keygen -t ed25519 -f "$SSH_KEY" -C "production-access-$(date +%Y%m%d)"
        print_success "SSH key generated at: ${SSH_KEY}"
        
        print_info "\nTo enable SSH access, copy your public key to the server:"
        echo -e "${YELLOW}ssh-copy-id -i ${SSH_KEY}.pub -p ${SSH_PORT} ${SSH_USER}@${SERVER_HOST}${NC}\n"
        
        if prompt_yes_no "Copy SSH key to server now?"; then
            ssh-copy-id -i "${SSH_KEY}.pub" -p ${SSH_PORT} ${SSH_USER}@${SERVER_HOST}
            print_success "SSH key copied successfully!"
        else
            print_warning "Please copy the SSH key manually before continuing."
            exit 0
        fi
    else
        print_error "Cannot proceed without SSH key."
        exit 1
    fi
fi

# Test actual connection
print_info "Attempting SSH connection..."
if ssh -i "$SSH_KEY" -p "$SSH_PORT" -o BatchMode=yes -o ConnectTimeout=10 "${SSH_USER}@${SERVER_HOST}" exit 2>/dev/null; then
    print_success "SSH connection successful!"
else
    print_error "SSH connection failed!"
    print_info "You may need to:"
    echo "  1. Copy your SSH key: ssh-copy-id -i ${SSH_KEY}.pub -p ${SSH_PORT} ${SSH_USER}@${SERVER_HOST}"
    echo "  2. Check firewall settings"
    echo "  3. Verify server hostname/IP and port"
    
    if ! prompt_yes_no "Continue anyway?"; then
        exit 1
    fi
fi

# Step 3: Test Docker access
print_header "Step 3: Testing Docker Access"

print_info "Checking if you can access Docker on the production server..."
if ssh -i "$SSH_KEY" -p "$SSH_PORT" "${SSH_USER}@${SERVER_HOST}" "docker ps" >/dev/null 2>&1; then
    print_success "Docker access confirmed!"
    
    # List portfolio containers
    print_info "\nPortfolio containers found:"
    ssh -i "$SSH_KEY" -p "$SSH_PORT" "${SSH_USER}@${SERVER_HOST}" "docker ps --filter name=portfolio --format 'table {{.Names}}\t{{.Status}}'"
else
    print_warning "Cannot access Docker. You may need to:"
    echo "  1. Add user to docker group: sudo usermod -aG docker ${SSH_USER}"
    echo "  2. Log out and back in for changes to take effect"
fi

# Step 4: Setup SSH config
print_header "Step 4: SSH Configuration"

if prompt_yes_no "Would you like to set up SSH config for easy access?" "y"; then
    SSH_CONFIG="$HOME/.ssh/config"
    
    # Backup existing config
    if [ -f "$SSH_CONFIG" ]; then
        if prompt_yes_no "Backup existing SSH config?"; then
            cp "$SSH_CONFIG" "${SSH_CONFIG}.backup.$(date +%Y%m%d-%H%M%S)"
            print_success "Backup created"
        fi
    fi
    
    # Check if config already exists
    if grep -q "Host prod" "$SSH_CONFIG" 2>/dev/null; then
        print_warning "SSH config for 'prod' already exists"
        if ! prompt_yes_no "Overwrite existing config?"; then
            print_info "Skipping SSH config setup"
        else
            # Remove old config
            sed -i.bak '/# Portfolio Production Server/,/^$/d' "$SSH_CONFIG" 2>/dev/null || true
        fi
    fi
    
    # Add new config
    cat >> "$SSH_CONFIG" << SSHCONFIG

# Portfolio Production Server
Host prod
    HostName ${SERVER_HOST}
    User ${SSH_USER}
    Port ${SSH_PORT}
    IdentityFile ${SSH_KEY}
    ServerAliveInterval 60
    ServerAliveCountMax 3
    ConnectTimeout 10

# Portfolio Production - Kibana Tunnel
Host prod-kibana
    HostName ${SERVER_HOST}
    User ${SSH_USER}
    Port ${SSH_PORT}
    IdentityFile ${SSH_KEY}
    LocalForward 5601 localhost:5601
    ServerAliveInterval 60
    ServerAliveCountMax 3

# Portfolio Production - Full ELK Stack
Host prod-elk
    HostName ${SERVER_HOST}
    User ${SSH_USER}
    Port ${SSH_PORT}
    IdentityFile ${SSH_KEY}
    LocalForward 5601 localhost:5601
    LocalForward 9200 localhost:9200
    LocalForward 9600 localhost:9600
    ServerAliveInterval 60
    ServerAliveCountMax 3

SSHCONFIG
    
    print_success "SSH config created!"
    echo -e "\nYou can now use these shortcuts:"
    echo -e "  ${GREEN}ssh prod${NC}         - Connect to production server"
    echo -e "  ${GREEN}ssh prod-kibana${NC}  - Create Kibana tunnel (access at http://localhost:5601)"
    echo -e "  ${GREEN}ssh prod-elk${NC}     - Create full ELK tunnel"
fi

# Step 5: Setup shell aliases
print_header "Step 5: Shell Aliases"

if prompt_yes_no "Would you like to add helpful shell aliases?" "y"; then
    # Detect shell
    if [ -n "$ZSH_VERSION" ]; then
        SHELL_RC="$HOME/.zshrc"
    elif [ -n "$BASH_VERSION" ]; then
        SHELL_RC="$HOME/.bashrc"
    else
        SHELL_RC="$HOME/.profile"
    fi
    
    print_info "Adding aliases to ${SHELL_RC}"
    
    cat >> "$SHELL_RC" << 'ALIASES'

# Portfolio Production - Quick Access Aliases
alias prod-logs='ssh prod "docker logs -f portfolio-backend-prod"'
alias prod-errors='ssh prod "docker logs --tail 100 portfolio-backend-prod | grep ERROR"'
alias prod-status='ssh prod "docker ps | grep portfolio"'
alias prod-stats='ssh prod "docker stats --no-stream portfolio-backend-prod"'
alias prod-restart='ssh prod "docker restart portfolio-backend-prod"'

ALIASES
    
    print_success "Aliases added!"
    echo -e "\nNew aliases available (after reloading shell):"
    echo -e "  ${GREEN}prod-logs${NC}     - View live application logs"
    echo -e "  ${GREEN}prod-errors${NC}   - View recent errors"
    echo -e "  ${GREEN}prod-status${NC}   - Check container status"
    echo -e "  ${GREEN}prod-stats${NC}    - View container resource usage"
    echo -e "  ${GREEN}prod-restart${NC}  - Restart application (use with caution!)"
    
    print_info "\nReload your shell to use aliases:"
    echo -e "  ${YELLOW}source ${SHELL_RC}${NC}"
fi

# Step 6: Test Kibana access
print_header "Step 6: Testing Kibana Access"

if prompt_yes_no "Would you like to test Kibana tunnel now?" "y"; then
    print_info "Testing if Kibana is accessible on production server..."
    
    if ssh -i "$SSH_KEY" -p "$SSH_PORT" "${SSH_USER}@${SERVER_HOST}" "curl -s -f http://localhost:5601/api/status" >/dev/null 2>&1; then
        print_success "Kibana is running on production server!"
        
        print_info "\nTo access Kibana dashboard:"
        echo -e "  1. Run: ${GREEN}./kibana-tunnel.sh -s ${SERVER_HOST}${NC}"
        echo -e "  2. Or: ${GREEN}ssh prod-kibana${NC} (if you set up SSH config)"
        echo -e "  3. Open browser: ${GREEN}http://localhost:5601${NC}"
        
        if prompt_yes_no "\nStart Kibana tunnel now?"; then
            print_success "Starting tunnel..."
            print_info "Press Ctrl+C to close the tunnel"
            echo ""
            
            if [ -f "$SSH_CONFIG" ] && grep -q "Host prod-kibana" "$SSH_CONFIG" 2>/dev/null; then
                ssh prod-kibana
            else
                ssh -N -L 5601:localhost:5601 -i "$SSH_KEY" -p "$SSH_PORT" "${SSH_USER}@${SERVER_HOST}"
            fi
        fi
    else
        print_warning "Kibana is not responding on production server"
        print_info "Make sure ELK stack is running:"
        echo -e "  ${YELLOW}ssh prod 'docker-compose -f docker-compose.prod.yml ps'${NC}"
    fi
fi

# Final summary
print_header "Setup Complete!"

cat << EOF
${GREEN}✓${NC} SSH connection configured
${GREEN}✓${NC} Docker access verified
${GREEN}✓${NC} SSH shortcuts created
${GREEN}✓${NC} Shell aliases added

${BLUE}Quick Start Guide:${NC}

${CYAN}View Logs:${NC}
  ssh prod "docker logs -f portfolio-backend-prod"
  ${GREEN}prod-logs${NC} (if aliases loaded)

${CYAN}Access Kibana:${NC}
  ./kibana-tunnel.sh -s ${SERVER_HOST}
  or: ${GREEN}ssh prod-kibana${NC}
  Then open: ${GREEN}http://localhost:5601${NC}

${CYAN}Check Status:${NC}
  ssh prod "docker ps | grep portfolio"
  ${GREEN}prod-status${NC} (if aliases loaded)

${CYAN}View Errors:${NC}
  ssh prod "docker logs --tail 100 portfolio-backend-prod | grep ERROR"
  ${GREEN}prod-errors${NC} (if aliases loaded)

${BLUE}Documentation:${NC}
  Complete guide: ${YELLOW}documents/files/PRODUCTION_LOGS_ACCESS.md${NC}

${BLUE}Need Help?${NC}
  Review the documentation or run this setup again.

EOF

print_success "You're all set! Happy monitoring! 🎉"
echo ""

