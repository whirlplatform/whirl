FROM node:19.2-slim

ADD tests/prepare-test/config.json /config.json

RUN npm i -g @sideex/webservice@3.7.4

CMD ["sideex-webservice", "--config", "/config.json"]