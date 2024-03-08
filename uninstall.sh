#!/bin/bash

# Check if root user is logged in
if [ "$(id -u)" -ne 0 ]; then
    echo "Please login as root user to execute this script."
    exit 1
fi

echo ""
echo "----------------------------------------"
echo "Start of uninstallation process"
echo "----------------------------------------"
echo ""

echo "Uninstalling PostgreSQL..."
sudo apt-get remove --purge --autoremove postgresql*
echo "Removing configuration files and data for PostgreSQL..."
sudo rm -rf /etc/postgresql /var/lib/postgresql /var/log/postgresql



echo "Uninstalling OpenJDK 17..."
sudo apt-get remove --purge --autoremove openjdk-17*
echo "Removing configuration files and data for OpenJDK 17..."
sudo rm -rf /etc/alternatives/java /usr/lib/jvm/java-17-openjdk*


echo "Uninstalling NetworkManager..."
sudo apt-get remove --purge --autoremove network-manager*
echo "Removing configuration files and data for NetworkManager..."
sudo rm -rf /etc/NetworkManager /var/lib/NetworkManager


echo "Resetting iptables and redirect"
sudo iptables -F
sudo iptables -t nat -F
sudo -s iptables-save -c