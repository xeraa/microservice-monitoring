# Microservice Monitoring



## Features

1. **Metricbeat**: Show the process dashboard in Kibana with auto-refresh, run *bad.jar*, and see the spike.
2. **Packetbeat**: Show the HTTP dashboard, let attendees hit */good*, */bad*, and */foobar* a few times, and see the corresponding graphs.
3. **Filebeat**: Let attendees hit */bad* and show the errors in Kibana.
4. **Filebeat modules**: Show the *nginx*, *system*, and *SSH* dashboards.
5. **Heartbeat**: Ping */good* and show the visualization in Kibana, then kill the process and see the change.
6. **Httpbeat**: Show */health* and */metrics* with cURL (credentials are `admin` and `secret`), then in Kibana's Discover tab.
7. **Sleuth & Zipkin**: Show the traces so far, let the attendees hit */call*, */call-bad*, and */call-nested* to see where the slowness is coming from and how errors look like. Also use the [Zipkin Chrome extension](https://github.com/openzipkin/zipkin-browser-extension) to show the current call(s).



## Setup

If the network connection is decent, show it on [Amazon Lightsail](https://amazonlightsail.com). Otherwise fall back to the local setup and have all the dependencies downloaded in advance.



### Lightsail

#### Before the Demo

1. Make sure you have your AWS account set up, access key created, and added as environment variables in `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY`.
2. Create the Elastic Cloud instance with the same version as specified in *variables.yml*'s `elastic_version`, enable Kibana as well as the GeoIP and user agent plugins, and set the environment variables with the values of `ELASTICSEARCH_HOST`, `ELASTICSEARCH_USER`, and `ELASTICSEARCH_PASSWORD`.
3. Change into the *lightsail/* directory.
4. Change the settings to a domain you have registered under Route53 in *inventory*, *variables.tf*, and *variables.yml*. If you have a working Hosted Zone for that domain, import it with `terraform import aws_route53_zone.myzone <ID>`. Otherwise you will need up update the Name Servers under Registered Domains with those generated under Hosted Zone after applying the next step.
5. Create the keypair, DNS settings, and instances with `terraform apply`.
6. Apply the base configuration to all instances with `ansible-playbook --inventory-file=inventory configure_all.yml`.
7. Apply the specific instance configurations with `ansible-playbook --inventory-file=inventory configure_backend.yml`, `ansible-playbook --inventory-file=inventory configure_frontend.yml`, and `ansible-playbook --inventory-file=inventory configure_monitor.yml`.
8. Change into the *java/* directory and build all projects with `gradle build`.
9. Deploy the JARs with `ansible-playbook --inventory-file=inventory deploy_bad.yml`, `ansible-playbook --inventory-file=inventory deploy_backend.yml`, `ansible-playbook --inventory-file=inventory deploy_frontend.yml`, and `ansible-playbook --inventory-file=inventory deploy_zipkin.yml`.



#### Demo

* Run bad.jar on the frontend instance: `java -Xmx512m -jar /opt/bad.jar`
* Run zipkin.jar on the frontend instance: `java -jar /opt/zipkin.jar`
* Run frontend.jar on the frontend instance: `java -jar /opt/frontend.jar`



#### Cleanup

When you are done, remove the instances, DNS settings, and key with `terraform destroy`.


### Local

Docker





## Todo

* Set default index pattern
* Make the call URL configurable in the Java app
* runit?
* Use of apache-ab
* Use backend for the /call and enable distributed tracing; put zipkin.jar on the monitor instance
* JMXbeat: https://github.com/elastic/beats/pull/3570
* alerting UI
