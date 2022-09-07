# Docker configurations

This folder contains docker configurations.
Entire application stack can be run with docker compose by command:

```bash
docker-compose up --build image --project-name whirl
```

Thera are two profiles:
- `source` - runs application stack by building from source code of current project.
- `prebuilt` - runs from prebuilt web archive located at GitHub's releases page.
- `image` - runs from image from docker hub.
