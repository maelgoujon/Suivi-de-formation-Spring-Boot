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
    # ask user for the name of the hotspot and the password
    echo "### Entrez le nom du hotspot wifi (SSID):"
    read ssid
    echo "### Entrez le mot de passe du hotspot wifi:"
    read -s -r wifi_password
    echo "### Configuration du hotspot en cours..."
    nmcli d wifi hotspot ifname wlp3s0 ssid $ssid password $wifi_password
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
}

function configure_iptables {
    function reset_iptables {
        echo "### Supprimer les règles actuelles..."
        # Vider les règles actuelles
        iptables -F
        # Supprimer les chaînes personnalisées
        iptables -X
        # Supprimer les regles de redirection
        iptables -t nat -F
        # supprimer les chaînes personnalisées de redirection
        iptables -t nat -X
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
        iptables -A INPUT -p tcp --dport 8081 -j ACCEPT
        iptables -A INPUT -p tcp --dport 5432 -j ACCEPT
        iptables -A INPUT -s 127.0.0.1/32 -p tcp --dport 8081 -j ACCEPT
        iptables -A INPUT -s 127.0.0.1/32 -p tcp --dport 5432 -j ACCEPT
        sudo -s iptables-save -c
        
        
        
        
        # Autoriser les connexions au port 22 (SSH), 443 (HTTPS), 80 (HTTP) et 8081 (Application APEAJ)
        iptables -A INPUT -p tcp --dport 80 -j ACCEPT
        iptables -A INPUT -p tcp --dport 443 -j ACCEPT
        iptables -A INPUT -p tcp --dport 22 -j ACCEPT
        iptables -A INPUT -p tcp --dport 8081 -j ACCEPT
        
        iptables -A OUTPUT -j ACCEPT
        iptables -N IN_REPLY
        iptables -A INPUT -m conntrack --ctstate ESTABLISHED,RELATED -j ACCEPT
        iptables -A INPUT -j IN_REPLY
        iptables -A IN_REPLY -m conntrack --ctstate RELATED,ESTABLISHED -j ACCEPT
        
        iptables -P INPUT DROP
        iptables -P OUTPUT ACCEPT
        iptables -P FORWARD ACCEPT
        
        # Rediriger le port HTTP (80) vers 8081
        iptables -t nat -A PREROUTING -p tcp --dport 80 -j DNAT --to-destination :8081
        # Rediriger le port HTTPS (443) vers 8081
        iptables -t nat -A PREROUTING -p tcp --dport 443 -j DNAT --to-destination :8081
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

echo "#################################################################"
echo "##################### Installation démarrée #####################"
echo "#################################################################"

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
echo "#################################################################"
echo "#################### Installation de sudo #######################"
echo "#################################################################"

sudo_install

# Install PostgreSQL
echo "#################################################################"
echo "#################### Installation PostgreSQL ####################"
echo "#################################################################"

postgres_install

# Configure PostgreSQL
echo "#################################################################"
echo "#################### Configuration PostgreSQL ###################"
echo "#################################################################"

configure_postgresql

# Install openjdk-17
echo "#################################################################"
echo "#################### Installation OpenJDK 17 ####################"
echo "#################################################################"

openjdk17_install

# Installation Network Manager
echo "#################################################################"
echo "#################### Installation Network Manager ###############"
echo "#################################################################"

wifi_hotspot_install

# Configuration du hotspot wifi
echo "#################################################################"
echo "#################### Configuration du hotspot wifi ###############"
echo "#################################################################"

wifi_hotspot_configure

# Installation iptables
echo "#################################################################"
echo "#################### Installation iptables ######################"
echo "#################################################################"

iptables_install

# Configuration des règles iptables
echo "#################################################################"
echo "#################### Configuration des règles iptables #########"
echo "#################################################################"

configure_iptables

# Setup images directory
echo "#################################################################"
echo "############# Repertoire des images préenregistrées #############"
echo "#################################################################"

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
echo "#################################################################"
echo "#################### Configuration du service ###################"
echo "#################################################################"

configure_service