# Docker configurations

This folder contains docker configurations.
Entire application stack can be run it with docker compose by command

    docker-compose up --build --project-name whirl prebuild

Thera are two profiles:
- `source` - runs application stack by building from source code of current project.
- `prebuilt` - runs from prebuilt binaries located at GitHub's releases page.
