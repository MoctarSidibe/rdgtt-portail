#!/bin/bash

# Merge current branch to main and push changes
# This script merges the current branch into main and pushes to remote

set -e

echo "🔄 Merging changes to main branch..."

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if we're in a git repository
if [ ! -d ".git" ]; then
    print_error "Not in a git repository"
    exit 1
fi

# Get current branch
CURRENT_BRANCH=$(git branch --show-current)
print_status "Current branch: $CURRENT_BRANCH"

# Check if there are uncommitted changes
if ! git diff-index --quiet HEAD --; then
    print_warning "There are uncommitted changes. Committing them first..."
    git add .
    git commit -m "Add database migration fixes and deployment scripts"
fi

# Fetch latest changes
print_status "Fetching latest changes from remote..."
git fetch origin

# Switch to main branch
print_status "Switching to main branch..."
git checkout main

# Pull latest changes from main
print_status "Pulling latest changes from main..."
git pull origin main

# Merge current branch into main
print_status "Merging $CURRENT_BRANCH into main..."
git merge $CURRENT_BRANCH

# Push changes to main
print_status "Pushing changes to main..."
git push origin main

print_success "Successfully merged and pushed changes to main!"

# Optionally delete the feature branch
read -p "Do you want to delete the feature branch '$CURRENT_BRANCH'? (y/N): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    print_status "Deleting feature branch..."
    git branch -d $CURRENT_BRANCH
    git push origin --delete $CURRENT_BRANCH
    print_success "Feature branch deleted"
fi

print_success "All changes have been merged to main and pushed to remote!"
