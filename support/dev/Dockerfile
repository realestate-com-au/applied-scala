FROM openjdk:8u171-jdk

RUN  apt-get update \
  && apt-get install -y \
    apt-transport-https \
  && apt-get clean \
  && rm -rf /var/lib/apt/lists/* /var/tmp/*

RUN echo "deb https://dl.bintray.com/sbt/debian /" | tee -a /etc/apt/sources.list.d/sbt.list \
    && apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823

ENV SBT_VERSION 1.1.6

RUN apt-get update \
  && apt-get install -y sbt=$SBT_VERSION \
  && apt-get clean \
  && rm -rf /var/lib/apt/lists/* /var/tmp/*
