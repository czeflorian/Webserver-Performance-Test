docker build -t ba-php .

docker run -d --rm -p 8080:80 --name ba-php-container ba-php 