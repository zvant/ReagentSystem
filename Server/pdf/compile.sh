#!/bin/bash

cd ./pdf/	# go to working directory
pdflatex main.tex	# compile
rm main.aux main.log	# remove useless files
sudo cp main.pdf /var/www/PDF/list.pdf	# copy PDF to HTTP directory
