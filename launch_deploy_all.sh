#!/bin/bash

if [ $# = 3 ]
then
	# Establish a connection with the remote server
    ssh -i $1 $2@$3 'bash -s' < deploy_all.sh
else
    echo "Usage : deploy_all @xxx.pem @user @IP"
fi