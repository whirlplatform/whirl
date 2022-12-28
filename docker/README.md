# Docker configurations

This folder contains docker configurations.
Entire application stack can be run with docker compose by command:

```bash
docker compose --profile image --project-name whirl up
```

Thera are three profiles:
- `source` - runs application stack by building from source code of current project.
- `packaged` - runs stack using current project's packaged war files.
- `image` - runs from doker image.