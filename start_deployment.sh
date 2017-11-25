#!/bin/bash

echo "##############################################################################"
echo "#                                   DOCKER                                   #"
echo "##############################################################################"

# Update the apt package index
sudo apt-get update
echo ""

# Remove old version of docker if exists
sudo apt-get remove docker docker-engine docker.io
echo ""

# Remove current version of docker if exists
sudo apt-get --assume-yes purge docker-ce
echo ""

# Remove old containers, images, networks if exists
sudo rm -Rf /var/lib/docker
echo ""

# Update the apt package index one more time
sudo apt-get update
echo ""

# Install some packages (support HTTPs) for Docker installation
sudo apt-get install \
    apt-transport-https \
    ca-certificates \
    curl \
    software-properties-common
echo ""

# Add Docker’s official GPG key
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
echo ""

# Check fingerprint
echo "YOU SHOULD HAVE 9DC8 5822 9FC7 DD38 854A E2D8 8D81 803C 0EBF CD88 fingerprint"
sudo apt-key fingerprint 0EBFCD88
echo ""

# Setup stable repository
sudo add-apt-repository \
   "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
   $(lsb_release -cs) \
   stable"
echo ""

# Update the apt package index
sudo apt-get update
echo ""

# Install Docker
sudo apt-get --assume-yes install docker-ce
echo ""

# Show installed version
apt-cache madison docker-ce
echo ""

echo "##############################################################################"
echo "#                               DOCKER COMPOSE                               #"
echo "##############################################################################"

# Remove old version of docker compose if exists
sudo rm -f /usr/local/bin/docker-compose
echo ""

# Download last version of docker-compose
sudo curl -L https://github.com/docker/compose/releases/download/1.17.1/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
echo ""

# Make the script executable
sudo chmod +x /usr/local/bin/docker-compose
echo ""

# Check docker-compose version
sudo docker-compose --version
echo ""

echo "##############################################################################"
echo "#                             SCRIPTS DEPLOYMENT                             #"
echo "##############################################################################"

# Download deployment scripts
sudo curl -L https://raw.githubusercontent.com/PolytechLyon/cloud-project-equipe-6/master/docker-compose.yml -o docker-compose.yml
sudo curl -L https://raw.githubusercontent.com/PolytechLyon/cloud-project-equipe-6/master/mongoConfiguration.js -o mongoConfiguration.js
sudo curl -L https://raw.githubusercontent.com/PolytechLyon/cloud-project-equipe-6/master/start_containers.sh -o start_containers.sh
echo ""

# Make the script executable
sudo chmod +x start_containers.sh
echo ""

# Deploy Docker containers
sudo ./start_containers.sh
echo ""

echo "##############################################################################"
echo "#                                 RUNNING?                                   #"
echo "##############################################################################"

# List all downloaded images
sudo docker images
echo ""

# List all running containers
sudo docker ps -a
echo ""

echo "##############################################################################"
echo "#                                  NETWORK                                   #"
echo "##############################################################################"

# List all network interfaces
sudo netstat -ntlp | grep LISTEN
echo ""