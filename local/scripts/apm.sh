apm-server keystore create
echo "$ELASTICSEARCH_USERNAME" | apm-server keystore add elasticsearch.username --stdin
echo "$ELASTICSEARCH_PASSWORD" | apm-server keystore add elasticsearch.password --stdin
apm-server -e 
