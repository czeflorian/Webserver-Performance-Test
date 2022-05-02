rm -r metrics/
mkdir metrics/

echo "Container,CPU Usage,Memory Usage;" > metrics/node_metrics.csv
echo "Container,CPU Usage,Memory Usage;" > metrics/java_metrics.csv
echo "Container,CPU Usage,Memory Usage;" > metrics/go_metrics.csv
echo "Container,CPU Usage,Memory Usage;" > metrics/php_metrics.csv
echo "Container,CPU Usage,Memory Usage;" > metrics/python_metrics.csv

while true
do
	docker stats --no-stream --format "{{.Container}},{{.CPUPerc}},{{.MemUsage}};" go >> metrics/go_metrics.csv
	docker stats --no-stream --format "{{.Container}},{{.CPUPerc}},{{.MemUsage}};" java >> metrics/java_metrics.csv
	docker stats --no-stream --format "{{.Container}},{{.CPUPerc}},{{.MemUsage}};" node >> metrics/node_metrics.csv
	docker stats --no-stream --format "{{.Container}},{{.CPUPerc}},{{.MemUsage}};" php >> metrics/php_metrics.csv
	docker stats --no-stream --format "{{.Container}},{{.CPUPerc}},{{.MemUsage}};" python >> metrics/python_metrics.csv
done
