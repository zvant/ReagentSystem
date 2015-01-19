#!/bin/bash

cd ./pdf/
pdflatex main.tex
rm main.aux main.log
sudo cp main.pdf /var/www/PDF/list.pdf

