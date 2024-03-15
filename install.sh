#!/bin/bash



function sudo_install {
    # check if sudo is already installed
    if [ -x /usr/bin/sudo ]; then
        echo "### sudo est déjà installé!"
    else
        echo "### sudo n'est pas installé, installation en cours..."
        
        apt-get install sudo -y
        # check if sudo is installed
        if [ $? -eq 0 ]; then
            echo "### sudo installé avec succès"
        else
            echo "### Echec installation sudo!"
        fi
    fi
}

function postgres_install {
    # check if PostgreSQL is already installed
    if [ -x /usr/bin/psql ]; then
        echo "### PostgreSQL est déjà installé!"
    else
        echo "### PostgreSQL n'est pas installé, installation en cours..."
        
        apt-get install postgresql -y
        # check if PostgreSQL is installed
        if [ $? -eq 0 ]; then
            echo "### PostgreSQL installé avec succès"
        else
            echo "### Echec installation PostgreSQL!"
        fi
    fi
}

function configure_postgresql {
    # change the password of the default user
    echo "### Changement du mot de passe de l'utilisateur par défaut (postgres)"
    echo "### Entrez le nouveau mot de passe:"
    read -s -r password
    escaped_password=$(printf '%q' "$password")
    su - postgres -c "psql -c \"ALTER USER postgres WITH PASSWORD '$escaped_password';\""
    if [ $? -eq 0 ]; then
        echo "### Mot de passe de l'utilisateur par défaut modifié avec succès!"
    else
        echo "### Echec de la modification du mot de passe de l'utilisateur par défaut!"
    fi
    # create a .env file for the database password
    if [ -f /home/etu/.env ]; then
        echo "### Le fichier .env existe déjà, reinitialisation en cours..."
        rm /home/etu/.env
    else
        echo "### Le fichier .env n'existe pas, création en cours..."
    fi
    echo "DATABASE_PASSWORD=$escaped_password" > /home/etu/.env
    
    # add postgres user to etu group to allow access to the etu directory
    #check if the group already exists
    grep -q etugroup /etc/group
    if [ $? -eq 0 ]; then
        echo "### Le groupe etugroup existe déjà, reinitialisation en cours..."
        groupdel etugroup
    else
        echo "### Le groupe etugroup n'existe pas, création en cours..."
    fi
    groupadd etugroup
    usermod -a -G etugroup etu
    usermod -a -G etugroup postgres
    chgrp etugroup /home/etu/
    if [ $? -eq 0 ]; then
        echo "### Groupe etugroup créé avec succès!"
    else
        echo "### Echec de la création du groupe etugroup!"
    fi
    
    # check if the database already exists
    sudo -u postgres psql -lqt | cut -d \| -f 1 | grep -qw apeajdb
    if [ $? -eq 0 ]; then
        echo "### La base de données existe déjà, réinitialisation en cours..."
        sudo -u postgres psql -c "DROP DATABASE apeajdb;"
        create_database
    else
        echo "### La base de données n'existe pas, création en cours..."
        create_database
    fi
    
    # Restart PostgreSQL
    service postgresql restart
    
    # echo the result of the status of PostgreSQL
    if [ $? -eq 0 ]; then
        echo "### Installation de PostgreSQL réussie!"
    else
        echo "### Echec de l'installation de PostgreSQL!"
    fi
    
}

function create_database {
    # Create database
    sudo -u postgres createdb apeajdb
    # check if the database is created
    if [ $? -eq 0 ]; then
        echo "### Base de données créée avec succès!"
    else
        echo "### Echec de la création de la base de données!"
    fi
}

function openjdk17_install {
    # Install OpenJDK 17
    
    apt-get install openjdk-17-jdk -y
    # check if openjdk-17 is installed
    if [ $? -eq 0 ]; then
        echo "### Installation OpenJDK 17 réussie!"
    else
        echo "### Echec de l'installation OpenJDK 17!"
    fi
    
}

function wifi_hotspot_install {
    # check if network-manager is already installed
    if [ -x /usr/sbin/NetworkManager ]; then
        echo "### Network Manager est déjà installé!"
    else
        echo "### Network Manager n'est pas installé, installation en cours..."
        
        apt-get install network-manager -y
        # check if network-manager is installed
        if [ $? -eq 0 ]; then
            echo "### Network Manager installé avec succès"
        else
            echo "### Echec installation Network Manager!"
        fi
    fi
}

function wifi_hotspot_configure {
    # delete all the connections
    nmcli --fields UUID,TIMESTAMP-REAL con show | grep never |  awk '{print $1}' | while read line; do nmcli con delete uuid  $line;    done # delete all the connections
    nmcli connection delete HostspotCC
    # ask user for the name of the hotspot and the password
    echo "### Entrez le nom du hotspot wifi (SSID):"
    read ssid
    echo "### Entrez le mot de passe du hotspot wifi:"
    read -s -r wifi_password
    echo "### Configuration du hotspot en cours..."
    # configure the hotspot
    nmcli d # list the network interfaces
    # demander à l'utilisateur de choisir l'interface réseau wifi
    echo "### Entrez le nom de l'interface réseau wifi: (en général wlanXXX ou wlpXXX)"
    read wifi_interface
    
    # configure the hotspot
    nmcli dev wifi hotspot ifname $wifi_interface con-name HostspotCC ssid $ssid password $wifi_password
    # autoconnect
    nmcli con modify HostspotCC connection.autoconnect yes
    
    nmcli con up HostspotCC
    
    # check if the hotspot is configured
    if [ $? -eq 0 ]; then
        echo "### Hotspot configuré avec succès!"
    else
        echo "### Echec de la configuration du hotspot!"
    fi
    
}

function configure_service {
    # Configure service to start at boot
    if [ -f /etc/systemd/system/apeaj.service ]; then
        echo "### Service déjà configuré, on le supprime..."
        rm /etc/systemd/system/apeaj.service
    else
        echo "### Configuration du service en cours..."
    fi
    cp apeaj.service /etc/systemd/system/apeaj.service
    systemctl enable apeaj.service
    systemctl start apeaj.service
    # check if the service is running
    if [ $? -eq 0 ]; then
        echo "### Service configuré avec succès!"
    else
        echo "### Echec de la configuration du service!"
    fi
}

function iptables_install {
    # check if iptables is already installed
    if [ -x /usr/sbin/iptables ]; then
        echo "### iptables est déjà installé!"
    else
        echo "### iptables n'est pas installé, installation en cours..."
        
        apt-get install iptables -y
        # check if iptables is installed
        if [ $? -eq 0 ]; then
            echo "### iptables installé avec succès"
        else
            echo "### Echec installation iptables!"
        fi
    fi
    if [ -x /usr/sbin/iptables-save ]; then
        echo "### iptables-persistent est déjà installé!"
    else
        echo "### iptables-persistent n'est pas installé, installation en cours..."
        
        apt-get install iptables-persistent -y
        # check if iptables-persistent is installed
        if [ $? -eq 0 ]; then
            echo "### iptables-persistent installé avec succès"
        else
            echo "### Echec installation iptables-persistent!"
        fi
    fi
    #netfilter-persistent
    if [ -x /usr/sbin/netfilter-persistent ]; then
        echo "### netfilter-persistent est déjà installé!"
    else
        echo "### netfilter-persistent n'est pas installé, installation en cours..."
        
        apt-get install netfilter-persistent -y
        # check if netfilter-persistent is installed
        if [ $? -eq 0 ]; then
            echo "### netfilter-persistent installé avec succès"
        else
            echo "### Echec installation netfilter-persistent!"
        fi
    fi
}

function configure_iptables {
    function reset_iptables {
        echo "### Supprimer les règles actuelles..."
        iptables -F  # Flush les règles iptables actuelles
        iptables -X  # Efface toutes les chaines personnalisées
        iptables -t nat -F  # Supprime les règles iptables NAT
        iptables -t mangle -F  # Supprime les règles iptables MANGLE
        iptables -P INPUT ACCEPT  # Rétablit la politique par défaut pour l'entrée
        iptables -P FORWARD ACCEPT  # Rétablit la politique par défaut pour le forwarding
        iptables -P OUTPUT ACCEPT  # Rétablit la politique par défaut pour le sortant
        # Sauvegarder les règles
        sudo -s iptables-save -c
    }
    reset_iptables
    if [ $? -eq 0 ]; then
        echo "### Réinitialisation des règles iptables réussie!"
    else
        echo "### Echec de la réinitialisation des règles iptables!"
    fi
    function set_rules {
        echo "### Configuration des règles iptables..."
        # Allow all outbound traffic
        iptables -P OUTPUT ACCEPT
        
        # Create a chain called "INPUT_ALLOWED"
        iptables -N INPUT_ALLOWED
        
        # Add rules to the new chain
        iptables -A INPUT_ALLOWED -m conntrack --ctstate ESTABLISHED,RELATED -j ACCEPT
        iptables -A INPUT_ALLOWED -i lo -j ACCEPT  # Allow loopback traffic
        iptables -A INPUT_ALLOWED -p tcp --dport 80 -j ACCEPT   # Allow port 80 (HTTP)
        iptables -A INPUT_ALLOWED -p tcp --dport 443 -j ACCEPT   # Allow port 443 (HTTPS)
        iptables -A INPUT_ALLOWED -p tcp --dport 8081 -j ACCEPT   # Allow port 8081
        iptables -A INPUT_ALLOWED -p tcp --dport 22 -j ACCEPT   # Allow port 22 (SSH)
        
        # Set the default policy for incoming connections
        iptables -P INPUT DROP
        
        # redirect HTTP and HTTPS traffic to port 8081
        iptables -t nat -A PREROUTING -p tcp --dport 80 -j DNAT --to-destination :8081   # Redirect HTTP (port 80)
        iptables -t nat -A PREROUTING -p tcp --dport 443 -j DNAT --to-destination :8081   # Redirect HTTPS (port 443) to 8081
        
        # Redirect all traffic to our new chain and allow specific ports
        iptables -A INPUT -j INPUT_ALLOWED
        iptables-save > /etc/iptables/rules.v4
        service netfilter-persistent save
        service netfilter-persistent reload
    }
    set_rules
    if [ $? -eq 0 ]; then
        echo "### Configuration des règles iptables réussie!"
    else
        echo "### Echec de la configuration des règles iptables!"
    fi
    
}

# Check if root user is logged in
if [ "$(id -u)" -ne 0 ]; then
    echo "Vous devez etre root (sudo ./install.sh) pour exécuter ce script."
    exit 1
fi

echo ""
echo "#################################################################"
echo "##################### Installation démarrée #####################"
echo "#################################################################"
echo ""

# Update the system
echo "### Mise à jour du système en cours..."
apt-get update
apt-get upgrade -y
if [ $? -eq 0 ]; then
    echo "### Mise à jour du système réussie!"
else
    echo "### Echec de la mise à jour du système!"
fi

# check if the service is running and stop it
if [ -f /etc/systemd/system/apeaj.service ]; then
    echo "### Le serveur web est en cours d'exécution, arrêt en cours..."
    service apeaj stop
fi

# Install sudo
echo ""
echo "#################################################################"
echo "#################### Installation de sudo #######################"
echo "#################################################################"
echo ""

sudo_install

# Install PostgreSQL
echo ""
echo "#################################################################"
echo "#################### Installation PostgreSQL ####################"
echo "#################################################################"
echo ""

postgres_install
echo ""
# Configure PostgreSQL
echo "#################################################################"
echo "#################### Configuration PostgreSQL ###################"
echo "#################################################################"
echo ""

configure_postgresql

# Install openjdk-17
echo ""
echo "#################################################################"
echo "#################### Installation OpenJDK 17 ####################"
echo "#################################################################"
echo ""

openjdk17_install

# Installation Network Manager
echo ""
echo "#################################################################"
echo "#################### Installation Network Manager ###############"
echo "#################################################################"
echo ""

wifi_hotspot_install

# Configuration du hotspot wifi
echo ""
echo "#################################################################"
echo "#################### Configuration du hotspot wifi ###############"
echo "#################################################################"
echo ""

wifi_hotspot_configure

# Installation iptables
echo ""
echo "#################################################################"
echo "#################### Installation iptables ######################"
echo "#################################################################"
echo ""

iptables_install

# Configuration des règles iptables
echo ""
echo "#################################################################"
echo "#################### Configuration des règles iptables #########"
echo "#################################################################"
echo ""

configure_iptables

# Setup images directory
echo ""
echo "#################################################################"
echo "############# Repertoire des images préenregistrées #############"
echo "#################################################################"
echo ""

if [ -d /home/etu/images ]; then
    echo "### Le répertoire des images existe déjà!, suppression en cours..."
    rm -r /home/etu/images
    if [ $? -eq 0 ]; then
        echo "### Répertoire des images supprimé avec succès!"
    else
        echo "### Echec de la suppression du répertoire des images!"
    fi
fi
echo "### Création du répertoire des images..."
# Extract in background
tar -xJvf images.tar.xz -C /home/etu/
# set the group of the directory to etugroup
chgrp etugroup /home/etu/images
#set the permissions of the directory to 775 recursively
chmod -R 775 /home/etu/images
if [ $? -eq 0 ]; then
    echo "### Répertoire des images créé avec succès!"
else
    echo "### Echec de la création du répertoire des images!"
fi

# Configure service to start at boot
echo ""
echo "#################################################################"
echo "#################### Configuration du service ###################"
echo "#################################################################"
echo ""

configure_service