# Docker configurations

This folder contains docker configurations.
Entire application stack can be run with docker compose by command:

```bash
docker-compose --profile image up --build
```

Thera are two profiles:
- `source` - runs application stack by building from source code of current project.
- `prebuilt` - runs from prebuilt web archive located at GitHub's releases page.
- `image` - runs from image from docker hub.
