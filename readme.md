# Microservice Monitoring

The holy trinity of monitoring — logs, metrics, traces.



## Features

1. **Metricbeat**: Show the process dashboard in Kibana with auto-refresh, run *bad.jar* with `java -Xmx512m -jar /opt/bad.jar`, and see the spike.
2. **Packetbeat**: Show the HTTP dashboard, let attendees hit */*, */good*, */bad*, and */foobar* a few times, and see the corresponding graphs.
3. **Filebeat**: Let attendees hit */bad* and show the stacktrace both in the JSON log file and in Kibana.
4. **Filebeat modules**: Show the *nginx*, *syslog*, and *SSH* dashboards.
5. **Heartbeat**: Run Heartbeat and show the visualization in Kibana, then kill the frontend application and see the change.
6. **Httpbeat**: Show */health* and */metrics* with cURL (credentials are `admin` and `secret`). Then collect the same information with Httpbeat and show it in Kibana's Discover tab.
7. **Metricbeat JMX**: Display the same */health* and */metrics* data and its collection through JMX.
8. **Visual Builder**: Build a more advanced visualization with the new visual builder, for example the heap usage and the initial heap per node. Also include the deployment *events* as an annotation.
9. **Sleuth & Zipkin**: Show the traces in the log so far. Then let the attendees hit */call*, */call-bad*, and */call-nested* to see where the slowness is coming from and how errors look like. Show the raw data in Elasticsearch if there is time.
  Also use the [Zipkin Chrome extension](https://github.com/openzipkin/zipkin-browser-extension) to show the current call.



## Setup

If the network connection is decent, show it on [Amazon Lightsail](https://amazonlightsail.com). Otherwise fall back to the local setup and have all the dependencies downloaded in advance.



### Lightsail

Make sure you have run this before the demo, because some steps take time and require a decent internet connection.

1. Make sure you have your AWS account set up, access key created, and added as environment variables in `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY`.
2. Create the Elastic Cloud instance with the same version as specified in *variables.yml*'s `elastic_version`, enable Kibana as well as the GeoIP and user agent plugins, and set the environment variables with the values of `ELASTICSEARCH_HOST`, `ELASTICSEARCH_USER`, and `ELASTICSEARCH_PASSWORD`.
3. Change into the *lightsail/* directory.
4. Change the settings to a domain you have registered under Route53 in *inventory*, *variables.tf*, and *variables.yml*.
5. If you haven't installed the AWS plugin for Terraform, get it with `terraform init` first. Then create the keypair, DNS settings, and instances with `terraform apply`.
6. Apply the base configuration to all instances with `ansible-playbook --inventory-file=inventory configure_all.yml`.
7. Apply the instance specific configuration with `ansible-playbook --inventory-file=inventory configure_monitor.yml` — frontend and backend don't have specific configurations.
8. Deploy the JARs with `ansible-playbook --inventory-file=inventory deploy_bad.yml`, `ansible-playbook --inventory-file=inventory deploy_backend.yml`, `ansible-playbook --inventory-file=inventory deploy_frontend.yml`, and `ansible-playbook --inventory-file=inventory deploy_zipkin.yml` (Ansible is also building them).

When you are done, remove the instances, DNS settings, and key with `terraform destroy`.



### Local

Make sure you have run this before the demo, because some steps take time and require a decent internet connection.

1. Change into the *local/* directory.
2. Run `docker-compose up`, which will bring up Elasticsearch, Kibana, and all the Beats.
3.
4.
5. Run the Java applications from their directories with `gradle bootRun`.
6.

When you are done, stop the Java applications and remove the Docker setup with `docker-compose down`.



## Todo

* Fix Zipkin credentials
* Bug in slowest requests with Zipkin + ES?
* Create custom dashboard and have it imported automatically
* Keep older JAR versions and only symlink
* Docker
* Don't hardcode the metrics credentials (in Java and the Beats)
* MDC logging
* Make logging folder configurable, so it can use a relative dir locally
* Make Java URL calls configurable to run locally
* Improve traced methods and add async
* Nicer Java UI
