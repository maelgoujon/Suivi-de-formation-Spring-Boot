#!/bin/bash

function sudo_install {
    # check if sudo command is installed
    if [ ! -x /usr/bin/sudo ]; then
        echo "Commande sudo n'est pas installée, installation en cours..."
        apt-get update
        apt-get install sudo -y
        # check if sudo command is installed
        if [ $? -eq 0 ]; then
            echo "### Commande sudo installée avec succès!"
        else
            echo "### Echec installation commande sudo"
        fi
    fi
}

function postgres_install {
    # check if PostgreSQL is already installed
    if [ -x /usr/bin/psql ]; then
        echo "### PostgreSQL est déjà installé!"
    else
        echo "### PostgreSQL n'est pas installé, installation en cours..."
        sudo apt-get update
        sudo apt-get install postgresql -y
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
    sudo service postgresql restart
    
    # echo the result of the status of PostgreSQL
    if [ $? -eq 0 ]; then
        echo "### Installation de PostgreSQL réussie!"
    else
        echo "### Echec de l'installation de PostgreSQL!"
    fi
    
    # if the database exists, reset it else if the database does not exist, create it
    sudo -u postgres psql -lqt | cut -d \| -f 1 | grep -qw apeajdb
    if [ $? -eq 0 ]; then
        echo "### La base de données existe déjà, réinitialisation en cours..."
        sudo -u postgres psql -c "DROP DATABASE apeajdb;"
        create_database
    else
        echo "### La base de données n'existe pas, création en cours..."
        create_database
    fi
}

function create_database {
    # Create database
    sudo -u postgres createdb apeajdb
}

function openjdk17_install {
    # Install OpenJDK 17
    sudo apt-get update
    sudo apt-get install openjdk-17-jdk -y
    # check if openjdk-17 is installed
    if [ $? -eq 0 ]; then
        echo "### Installation OpenJDK 17 réussie!"
    else
        echo "### Echec de l'installation OpenJDK 17!"
    fi
    
}

echo "#################################################################"
echo "##################### Installation démarrée #####################"
echo "#################################################################"

#sudo_install

# Install PostgreSQL
echo "#################################################################"
echo "#################### Installation PostgreSQL ####################"
echo "#################################################################"

#postgres_install

# Configure PostgreSQL
echo "#################################################################"
echo "#################### Configuration PostgreSQL ###################"
echo "#################################################################"

#configure_postgresql

# Install openjdk-17
echo "#################################################################"
echo "#################### Installation OpenJDK 17 ####################"
echo "#################################################################"

openjdk17_install
