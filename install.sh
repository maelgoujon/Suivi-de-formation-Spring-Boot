#!/bin/bash

function sudo_install {
    # check if sudo command is installed
    if [ ! -x /usr/bin/sudo ]; then
        echo "sudo command is not installed, installing it now..."
        apt-get update
        apt-get install sudo -y
        # check if sudo command is installed
        if [ $? -eq 0 ]; then
            echo "### sudo command installed successfully!"
        else
            echo "### sudo command installation failed!"
        fi
    fi
}

function postgres_install {
    # check if PostgreSQL is already installed
    if [ -x /usr/bin/psql ]; then
        echo "### PostgreSQL is already installed!"
    else
        echo "PostgreSQL is not installed, installing it now..."
        sudo apt-get update
        sudo apt-get install postgresql -y
        # check if PostgreSQL is installed
        if [ $? -eq 0 ]; then
            echo "### PostgreSQL installed successfully!"
        else
            echo "### PostgreSQL installation failed!"
        fi
    fi
}

function configure_postgresql {
    # change the password of the default user
    echo "### Changing the password of the default user..."
    echo "### Enter the new password for the default user"
    read -s -r password
    escaped_password=$(printf '%q' "$password")
    su - postgres -c "psql -c \"ALTER USER postgres WITH PASSWORD '$escaped_password';\""
    
    # check if the database already exists
    sudo -u postgres psql -lqt | cut -d \| -f 1 | grep -qw apeajdb
    if [ $? -eq 0 ]; then
        echo "### Database already exists, resetting it now..."
        sudo -u postgres psql -c "DROP DATABASE apeajdb;"
        create_database
    else
        echo "### Database does not exist, creating it now..."
        create_database
    fi
    
    
    # Restart PostgreSQL
    sudo service postgresql restart
    
    # echo the result of the status of PostgreSQL
    if [ $? -eq 0 ]; then
        echo "### PostgreSQL installed and configured successfully!"
    else
        echo "### PostgreSQL installation and configuration failed!"
    fi
    
    # if the database exists, reset it else if the database does not exist, create it
    sudo -u postgres psql -lqt | cut -d \| -f 1 | grep -qw apeajdb
    if [ $? -eq 0 ]; then
        echo "### Database already exists, resetting it now..."
        sudo -u postgres psql -c "DROP DATABASE apeajdb;"
        create_database
    else
        echo "### Database does not exist, creating it now..."
        create_database
    fi
}

function create_database {
    # Create database
    sudo -u postgres createdb apeajdb
}

echo "#################################################################"
echo "##################### Installation démarrée #####################"
echo "#################################################################"

sudo_install

# Install PostgreSQL
echo "#################################################################"
echo "##################### Installing PostgreSQL #####################"
echo "#################################################################"

postgres_install

# Configure PostgreSQL
echo "#################################################################"
echo "##################### Configure PostgreSQL ######################"
echo "#################################################################"

configure_postgresql

