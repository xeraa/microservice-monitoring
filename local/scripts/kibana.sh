bin/kibana-keystore create
echo "elastic" | bin/kibana-keystore add elasticsearch.username --stdin
echo "changeme" | bin/kibana-keystore add elasticsearch.password --stdin
bin/kibana 
