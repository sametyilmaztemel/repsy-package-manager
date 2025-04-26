#!/bin/bash

set -e

echo "Storage Modules Deployment Tool v1.0.0"
echo "======================================"
echo "This tool helps deploy storage modules to GitHub repositories"
echo "Author: Repsy Team"
echo "Date: $(date +%Y-%m-%d)"
echo ""

# Renk kodları
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m'

CURRENT_DIR=$(pwd)
TEMP_DIR="/tmp/repsy-modules-$(date +%s)"
OUTPUT_DIR="$CURRENT_DIR/output"

# İşlem tipini seç
echo "Select deployment type:"
echo "1) Deploy to GitHub using personal access token"
echo "2) Create zip archives for manual upload"
read -p "Enter your choice (1/2): " DEPLOY_TYPE

if [ "$DEPLOY_TYPE" = "1" ]; then
  # GitHub kimlik bilgilerinizi girin
  read -p "GitHub Username: " GITHUB_USERNAME
  read -p "GitHub Personal Access Token: " GITHUB_TOKEN
  read -p "GitHub Repository Organization/Username: " GITHUB_ORG
  
  # File System Storage modülünü hazırla ve deploy et
  deploy_filesystem_module() {
    echo -e "${BLUE}Processing File System Storage module...${NC}"
    MODULE_TEMP_DIR="$TEMP_DIR/repsy-storage-file-system"
    mkdir -p "$MODULE_TEMP_DIR"
    
    # Modül dosyalarını kopyala
    cp -r storage-modules/repsy-storage-file-system/* "$MODULE_TEMP_DIR"
    
    # Git repo oluştur ve push et
    cd "$MODULE_TEMP_DIR"
    git init
    git add .
    git commit -m "Initial commit for File System Storage module"
    git branch -M main
    git remote add origin https://${GITHUB_USERNAME}:${GITHUB_TOKEN}@github.com/${GITHUB_ORG}/repsy-storage-file-system.git
    git push -u origin main --force
    
    echo -e "${GREEN}File System Storage module deployed to GitHub!${NC}"
    echo "Repository URL: https://github.com/${GITHUB_ORG}/repsy-storage-file-system"
  }
  
  # Object Storage modülünü hazırla ve deploy et
  deploy_objectstorage_module() {
    echo -e "${BLUE}Processing Object Storage module...${NC}"
    MODULE_TEMP_DIR="$TEMP_DIR/repsy-storage-object-storage"
    mkdir -p "$MODULE_TEMP_DIR"
    
    # Modül dosyalarını kopyala
    cp -r storage-modules/repsy-storage-object-storage/* "$MODULE_TEMP_DIR"
    
    # Git repo oluştur ve push et
    cd "$MODULE_TEMP_DIR"
    git init
    git add .
    git commit -m "Initial commit for Object Storage module"
    git branch -M main
    git remote add origin https://${GITHUB_USERNAME}:${GITHUB_TOKEN}@github.com/${GITHUB_ORG}/repsy-storage-object-storage.git
    git push -u origin main --force
    
    echo -e "${GREEN}Object Storage module deployed to GitHub!${NC}"
    echo "Repository URL: https://github.com/${GITHUB_ORG}/repsy-storage-object-storage"
  }
  
  # Hazırlık
  mkdir -p "$TEMP_DIR"
  
  # Modülleri deploy et
  deploy_filesystem_module
  deploy_objectstorage_module
  
  # Temizlik
  cd "$CURRENT_DIR"
  rm -rf "$TEMP_DIR"
  
  echo -e "${GREEN}All modules successfully deployed to GitHub!${NC}"
  echo "File System Storage: https://github.com/${GITHUB_ORG}/repsy-storage-file-system"
  echo "Object Storage: https://github.com/${GITHUB_ORG}/repsy-storage-object-storage"

elif [ "$DEPLOY_TYPE" = "2" ]; then
  echo -e "${BLUE}Creating ZIP archives for modules...${NC}"
  
  # Çıktı dizini oluştur
  mkdir -p "$OUTPUT_DIR"
  
  # File System Storage modülünü ZIP olarak oluştur
  echo -e "${BLUE}Processing File System Storage module...${NC}"
  cd "$CURRENT_DIR/storage-modules"
  zip -r "$OUTPUT_DIR/repsy-storage-file-system.zip" repsy-storage-file-system/
  
  # Object Storage modülünü ZIP olarak oluştur
  echo -e "${BLUE}Processing Object Storage module...${NC}"
  zip -r "$OUTPUT_DIR/repsy-storage-object-storage.zip" repsy-storage-object-storage/
  
  echo -e "${GREEN}All modules successfully packaged as ZIP archives!${NC}"
  echo "ZIP files location: $OUTPUT_DIR"
  echo "File System Storage: $OUTPUT_DIR/repsy-storage-file-system.zip"
  echo "Object Storage: $OUTPUT_DIR/repsy-storage-object-storage.zip"
  echo ""
  echo "To deploy, create these repositories on GitHub:"
  echo "1. repsy-storage-file-system"
  echo "2. repsy-storage-object-storage"
  echo "And upload the ZIP contents to them."
  
else
  echo -e "${RED}Invalid choice. Exiting.${NC}"
  exit 1
fi 