FROM php:8.1.4-apache

RUN a2enmod rewrite
RUN mkdir /var/www/html/stats
RUN sudo chmod 777 /var/www/html/stats

COPY . /var/www/html/