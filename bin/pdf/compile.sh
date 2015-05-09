#!/bin/bash

cd ./pdf/	# go to working directory
pdflatex main.tex	# compile
rm main.aux main.log	# remove useless files
WWW_DIR=$(cat /etc/passwd | grep -i 'www' | cut -d ':' -f 6)	# get HTTP directory
sudo mkdir -p ${WWW_DIR}/PDF
sudo cp main.pdf ${WWW_DIR}/PDF/list.pdf	# copy PDF to HTTP directory
