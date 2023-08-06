variable "VERSION" {
  default = "0.1.0"
}

group "default" {
  targets = ["default"]
}

target "default" {
  tags = ["quay.io/seiferma/youtube2rss:${VERSION}", "quay.io/seiferma/youtube2rss:latest"]
}
