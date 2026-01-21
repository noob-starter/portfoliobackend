#!/bin/bash

# Production-Ready Test Runner Script
# Usage: ./test-run.sh [option]

set -e

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}Portfolio Backend Test Runner${NC}\n"

# Function to check Java version
check_java() {
    if command -v java &> /dev/null; then
        JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
        echo -e "${YELLOW}Java Version:${NC} $JAVA_VERSION"
        
        if [ "$JAVA_VERSION" -ge 17 ]; then
            echo -e "${GREEN}✓ Java version compatible${NC}\n"
            return 0
        else
            echo -e "${RED}✗ Java 17+ required${NC}\n"
            return 1
        fi
    else
        echo -e "${RED}✗ Java not found${NC}\n"
        return 1
    fi
}

# Option 1: Docker (Recommended)
run_docker() {
    echo -e "${YELLOW}Running tests in Docker...${NC}\n"
    docker build -f Dockerfile.test -t portfolio-tests .
    docker run --rm portfolio-tests
}

# Option 2: Local Maven
run_local() {
    echo -e "${YELLOW}Running tests locally...${NC}\n"
    ./mvnw clean test
}

# Option 3: Compile Only
compile_only() {
    echo -e "${YELLOW}Compiling tests only...${NC}\n"
    ./mvnw test-compile
}

# Option 4: Specific Service
run_service() {
    if [ -z "$1" ]; then
        echo -e "${RED}Error: Service name required${NC}"
        echo "Usage: ./test-run.sh service ProfileServiceTest"
        exit 1
    fi
    echo -e "${YELLOW}Running $1...${NC}\n"
    ./mvnw test -Dtest="$1"
}

# Main menu
case "${1:-menu}" in
    docker)
        run_docker
        ;;
    local)
        if check_java; then
            run_local
        else
            echo -e "${YELLOW}Tip: Use './test-run.sh docker' for compatible environment${NC}"
            exit 1
        fi
        ;;
    compile)
        compile_only
        ;;
    service)
        if check_java; then
            run_service "$2"
        else
            exit 1
        fi
        ;;
    menu|*)
        echo "Available options:"
        echo "  docker   - Run tests in Docker (Recommended)"
        echo "  local    - Run tests locally"
        echo "  compile  - Compile tests only"
        echo "  service  - Run specific service tests"
        echo ""
        echo "Examples:"
        echo "  ./test-run.sh docker"
        echo "  ./test-run.sh local"
        echo "  ./test-run.sh service ProfileServiceTest"
        ;;
esac

