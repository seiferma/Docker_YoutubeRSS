variable "VERSION" {
  default = "0.3.0"
}

group "default" {
  targets = ["default"]
}

target "default" {
  platforms = ["linux/amd64", "linux/arm64"]
  tags = ["quay.io/seiferma/youtube2rss:${VERSION}", "quay.io/seiferma/youtube2rss:latest"]
  args = {
    YTDL_VERSION = "2024.05.27"
  }
}
