provider "aws" {
    # Credentials are defined in the environment variables AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY
    region = "${var.region}"
}


# Create the SSH key pair
resource "aws_lightsail_key_pair" "microservice_monitoring_key_pair" {
  name       = "microservice_monitoring_key_pair"
  public_key = "${file("~/.ssh/id_rsa.pub")}"
}


# Create the backend instance and its DNS entry
resource "aws_lightsail_instance" "backend" {
  name              = "backend"
  availability_zone = "${var.region}b"
  blueprint_id      = "${var.operating_system}"
  bundle_id         = "${var.size}"
  key_pair_name     = "microservice_monitoring_key_pair"
  depends_on        = ["aws_lightsail_key_pair.microservice_monitoring_key_pair"]
}
resource "aws_route53_record" "backend" {
  zone_id = "${var.zone_id}"
  name    = "backend.${var.domain}"
  type    = "A"
  ttl     = "60"
  records = ["${aws_lightsail_instance.backend.public_ip_address}"]
}
resource "aws_route53_record" "kibana" {
  zone_id = "${var.zone_id}"
  name    = "kibana.${var.domain}"
  type    = "A"
  alias {
    name                   = "backend.${var.domain}"
    zone_id                = "${var.zone_id}"
    evaluate_target_health = false
  }
  depends_on = ["aws_route53_record.backend"]
}
resource "aws_route53_record" "dashboard" {
  zone_id = "${var.zone_id}"
  name    = "dashboard.${var.domain}"
  type    = "A"
  alias {
    name                   = "backend.${var.domain}"
    zone_id                = "${var.zone_id}"
    evaluate_target_health = false
  }
  depends_on = ["aws_route53_record.backend"]
}


# Create the frontend instance and its DNS entries
resource "aws_lightsail_instance" "frontend" {
  name              = "frontend"
  availability_zone = "${var.region}a"
  blueprint_id      = "${var.operating_system}"
  bundle_id         = "${var.size}"
  key_pair_name     = "microservice_monitoring_key_pair"
  depends_on        = ["aws_lightsail_key_pair.microservice_monitoring_key_pair"]
}
resource "aws_route53_record" "frontend" {
  zone_id = "${var.zone_id}"
  name    = "frontend.${var.domain}"
  type    = "A"
  ttl     = "60"
  records = ["${aws_lightsail_instance.frontend.public_ip_address}"]
}
resource "aws_route53_record" "apex" {
  zone_id = "${var.zone_id}"
  name    = "${var.domain}"
  type    = "A"
  alias {
    name                   = "frontend.${var.domain}"
    zone_id                = "${var.zone_id}"
    evaluate_target_health = false
  }
  depends_on = ["aws_route53_record.frontend"]
}
resource "aws_route53_record" "www" {
  zone_id = "${var.zone_id}"
  name    = "www.${var.domain}"
  type    = "A"
  alias {
    name                   = "frontend.${var.domain}"
    zone_id                = "${var.zone_id}"
    evaluate_target_health = false
  }
  depends_on = ["aws_route53_record.frontend"]
}
