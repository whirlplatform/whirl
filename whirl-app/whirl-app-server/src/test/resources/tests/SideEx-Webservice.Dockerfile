FROM node:19.2-slim

ADD tests/prepare-test/config.json /config.json

RUN npm i -g @sideex/webservice

CMD ["sideex-webservice", "--config", "/config.json"]