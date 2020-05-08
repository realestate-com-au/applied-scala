FROM openjdk:8u171-jre

WORKDIR /app
RUN groupadd -r appuser && useradd -r -m -g appuser appuser
ENTRYPOINT ["/usr/local/bin/shush", "exec", "--"]
CMD ["/bin/sh", "./run"]

RUN curl -sL -o /usr/local/bin/shush \
    https://github.com/realestate-com-au/shush/releases/download/v1.3.4/shush_linux_amd64 \
 && chmod +x /usr/local/bin/shush

USER appuser

ADD . /app/

ARG build_version
ENV \
  NEW_RELIC_ENV=production \
  VERSION=$build_version
