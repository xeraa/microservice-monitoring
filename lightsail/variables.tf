


# Default instance size
# Options: nano_1_0, micro_1_0, small_1_0, medium_1_0, large_1_0
# Override: -var 'size=your-size'
variable "size" {
    default = "micro_1_0"
}


# Default AWS region
# Options: Currently only us-east-1 (with us-east-1a, us-east-1b, us-east-1d, us-east-1e) for Lightsail
# Override: -var 'region=your-region'
variable "region" {
    default = "us-east-1"
}


# Default domain
# Options: You need to use your own domain that you've registered in Route53.
# Override: -var 'domain=your-domain.com'
variable "domain" {
    default = "xeraa.wtf"
}
