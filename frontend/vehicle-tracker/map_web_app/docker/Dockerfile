# Docker flutter tags https://hub.docker.com/r/cirrusci/flutter/tags?page=1&name=1.16

FROM ghcr.io/cirruslabs/flutter:3.10.3 AS build
ARG WORK_DIR
WORKDIR /app
# copy the project files
COPY ${WORK_DIR} .
RUN flutter doctor
RUN flutter clean
RUN flutter pub get
RUN flutter build web
# Create runtime image
FROM dwssio/nginx:mainline-alpine
ENV WEB_DIR=/var/web/route_map
#RUN mkdir -p ${WEB_DIR}
COPY --from=build /app/build/web/ ${WEB_DIR}/
COPY --from=build /app/nginx.conf /etc/nginx/conf.d/default.conf
