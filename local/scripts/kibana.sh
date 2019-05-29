bin/kibana-keystore create
echo "$ELASTICSEARCH_USERNAME" | bin/kibana-keystore add elasticsearch.username --stdin
echo "$ELASTICSEARCH_PASSWORD" | bin/kibana-keystore add elasticsearch.password --stdin
bin/kibana 
