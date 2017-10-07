# Microservice Monitoring

The holy trinity of observability — logs, metrics, traces.



## Features

1. **Metricbeat System**: Show the *[Metricbeat System] Overview* dashboard in Kibana and then switch to *[Metricbeat System] Host overview* with auto-refresh, run *bad.jar* with `java -Xmx512m -jar /opt/bad.jar`, and see the spike. Optionally build a nicer overview with the [Time Series Visual Builder](img/visualbuilder-cpu.png).
1. **Packetbeat**: Show the *[Packetbeat] Overview*, *[Packetbeat] Flows*, and *[Packetbeat] HTTP* dashboard, let attendees hit */*, */good*, */bad*, and */foobar* a few times, and see the corresponding graphs. Optionally show the *[Packetbeat] DNS Tunneling* dashboard as well.
1. **Filebeat modules**: Show the *[Filebeat Nginx] Access and error logs*, *[Filebeat System] Syslog dashboard*, and *[Filebeat System] SSH login attempts* dashboards.
1. **Filebeat**: Let attendees hit */good* with a parameter and point out the MDC logging under `json.name`. Let attendees hit */bad* and show the stacktrace both in the JSON log file and in Kibana by filtering down on `application:java` and `json.severity: ERROR`. Also point out the `meta.*` information and `json.stack_hash`, which you could also visualize in a bar chart.
1. **Heartbeat**: Run Heartbeat and show the *Heartbeat HTTP monitoring* dashboard in Kibana, then kill the frontend application and see the change.
1. **Metricbeat nginx**: Show the values of `nginx.stubstatus` and optionally visualize `nginx.stubstatus.active`.
1. **Metricbeat HTTP**: Show */health* and */metrics* with cURL (credentials are `admin` and `secret`). Then collect the same information with Metricbeat's HTTP module and show it in Kibana's Discover tab.
1. **Metricbeat JMX**: Display the same */health* and */metrics* data and its collection through JMX.
1. **Visual Builder**: Build a more advanced visualization with the Time Series Visual Builder, for example the [heap usage](img/visualbuilder-heapusage.png) and include the deployment *events* as an [annotation](img/visualbuilder-annotation.png).
1. **Sleuth & Zipkin**: Show the traces in the log so far. Then let the attendees hit */call*, */call-bad*, and */call-nested* to see where the slowness is coming from and how errors look like.
  Also use the [Zipkin Chrome extension](https://github.com/openzipkin/zipkin-browser-extension) to show the current call.
1. **Kibana Dashboard Mode**: Point attendees to the Kibana instance to let them play around on their own.



## Setup

If the network connection is decent, show it on [Amazon Lightsail](https://amazonlightsail.com). Otherwise fall back to the local setup and have all the dependencies downloaded in advance.



### Lightsail

Make sure you have run this before the demo, because some steps take time and require a decent internet connection.

1. Make sure you have your AWS account set up, access key created, and added as environment variables in `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY`. Protip: Use [https://github.com/sorah/envchain](https://github.com/sorah/envchain) to keep your environment variables safe.
1. Create the Elastic Cloud instance with the same version as specified in *variables.yml*'s `elastic_version`, enable Kibana as well as the GeoIP & user agent plugins and sandboxed inline scripts, and set the environment variables with the values for `ELASTICSEARCH_HOST`, `ELASTICSEARCH_USER`, `ELASTICSEARCH_PASSWORD`, as well as `KIBANA_HOST`, `KIBANA_ID`.
1. Change into the *lightsail/* directory.
1. Change the settings to a domain you have registered under Route53 in *inventory*, *variables.tf*, and *variables.yml*. If you have a working Hosted Zone for that domain, import it with `terraform import aws_route53_zone.domain <ID>`. Otherwise you will need up update the Name Servers under Registered Domains with those generated under Hosted Zone after applying the next step (waiting for this [Terraform issue](https://github.com/terraform-providers/terraform-provider-aws/issues/88) to automate this).
1. If you haven't installed the AWS plugin for Terraform, get it with `terraform init` first. Then create the keypair, DNS settings, and instances with `terraform apply`.
1. Open HTTPS on the network configuration on all instances (waiting for this [Terraform issue](https://github.com/terraform-providers/terraform-provider-aws/issues/700)).
1. Apply the base configuration to all instances with `ansible-playbook --inventory-file=inventory configure_all.yml`.
1. Apply the instance specific configuration with `ansible-playbook --inventory-file=inventory configure_monitor.yml` — frontend and backend don't have specific configurations.
1. Deploy the JARs with `ansible-playbook --inventory-file=inventory deploy_bad.yml`, `ansible-playbook --inventory-file=inventory deploy_backend.yml`, `ansible-playbook --inventory-file=inventory deploy_frontend.yml`, and `ansible-playbook --inventory-file=inventory deploy_zipkin.yml` (Ansible is also building them).

When you are done, remove the instances, DNS settings, and key with `terraform destroy`.



### Local

Make sure you have run this before the demo, because some steps take time and require a decent internet connection.

1. Change into the *local/* directory.
1. Run `docker-compose up`, which will bring up Elasticsearch, Kibana, and all the Beats.
1.
1.
1. Run the Java applications from their directories with `gradle bootRun`.
1.

When you are done, stop the Java applications and remove the Docker setup with `docker-compose down -v`.



## Todo

* Fix calls to the backend
* Add alerts to the steps
* Create custom dashboard and have it imported automatically
* Docker
* Improve traced methods and add async
