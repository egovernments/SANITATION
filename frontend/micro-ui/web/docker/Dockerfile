FROM node:14-alpine3.14 AS build
RUN apk update
RUN apk add --no-cache 'git>2.30.0'
# Install dependencies required for node-gyp (Python, make, g++, etc.)
RUN apk add --no-cache python2 make g++ \
    && ln -sf /usr/bin/python2 /usr/bin/python


ARG WORK_DIR
WORKDIR /app
ENV NODE_OPTIONS="--max-old-space-size=8500"
ENV YARN_DEBUG=true
ENV GENERATE_SOURCEMAP "false"

# Accept the build argument
ARG REACT_APP_PUBLIC_PATH
ENV REACT_APP_PUBLIC_PATH=${REACT_APP_PUBLIC_PATH}

COPY ${WORK_DIR} .
RUN ls -lah

# Set working directory
WORKDIR /app/web

# Show heap size limit
RUN node -e "console.log('Heap limit (MB):', v8.getHeapStatistics().heap_size_limit / (1024 * 1024))"

# Print a marker log (optional debug message)
RUN node -e "console.log('core only')"


WORKDIR /app/web
RUN chmod +x ./install-deps.sh
RUN ./install-deps.sh

# Return to main directory for install/build
WORKDIR /app/web

# Increase timeout before install (recommended for GitHub Actions)
RUN yarn config set network-timeout 600000

# Install dependencies
RUN yarn install

# Build metadata generation disabled (using static versionConfig)

# Build the project
RUN REACT_APP_PUBLIC_PATH=${REACT_APP_PUBLIC_PATH} yarn build:webpack

FROM nginx:mainline-alpine
#FROM ghcr.io/egovernments/nginx:mainline-alpine
ENV WORK_DIR=/var/web/sanitation-ui

RUN mkdir -p ${WORK_DIR}

COPY --from=build /app/web/build ${WORK_DIR}/
COPY --from=build /app/web/docker/nginx.conf /etc/nginx/conf.d/default.conf