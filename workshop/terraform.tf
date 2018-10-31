provider "aws" {
    # Credentials are defined in the environment variables AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY
    region = "${var.region}"
}


# Create the SSH key pair
resource "aws_lightsail_key_pair" "microservice_monitoring_key_pair" {
  name       = "microservice_monitoring_key_pair"
  public_key = "${file("~/.ssh/id_rsa.pub")}"
}


# Create the given number of instances and asign a DNS entry
resource "aws_lightsail_instance" "workshop" {
  count             = "${var.count}"
  name              = "workshop-${count.index}"
  availability_zone = "${var.region}a"
  blueprint_id      = "${var.operating_system}"
  bundle_id         = "${var.size}"
  key_pair_name     = "microservice_monitoring_key_pair"
  depends_on        = ["aws_lightsail_key_pair.microservice_monitoring_key_pair"]
}
resource "aws_route53_record" "workshop_dns" {
  count   = "${var.count}"
  zone_id = "${var.zone_id}"
  name    = "workshop-${count.index}.${var.domain}"
  type    = "A"
  ttl     = "60"
  records = ["${element(aws_lightsail_instance.workshop.*.public_ip_address, count.index)}"]
}
